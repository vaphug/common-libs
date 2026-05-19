package com.yourdomain.common.sqs.service;

import com.yourdomain.common.sqs.config.CommonSqsProperties;
import com.yourdomain.common.sqs.idempotency.MessageIdempotencyStore;
import com.yourdomain.common.sqs.model.ProcessMessageOptions;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ChangeMessageVisibilityRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Service common cho thao tác SQS.
 *
 * <p>Phạm vi:
 * - Resolve queue URL từ queue name
 * - Gửi/nhận/xóa message
 * - process message với heartbeat visibility + idempotency để giảm xử lý trùng
 */
public class CommonSqsService {

    private final SqsClient sqsClient;
    private final CommonSqsProperties properties;
    private final MessageIdempotencyStore idempotencyStore;

    /** Cache queueUrl theo queueName để giảm số lần gọi GetQueueUrl API. */
    private final Map<String, QueueUrlEntry> queueUrlCache = new ConcurrentHashMap<>();

    public CommonSqsService(
            SqsClient sqsClient,
            CommonSqsProperties properties,
            MessageIdempotencyStore idempotencyStore
    ) {
        this.sqsClient = sqsClient;
        this.properties = properties;
        this.idempotencyStore = idempotencyStore;
    }

    /**
     * Lấy queueUrl theo queueName, có cache TTL.
     *
     * @param queueName tên queue trong SQS (không phải queue URL)
     * @return queue URL đầy đủ để gọi các API SQS khác
     */
    public String resolveQueueUrl(String queueName) {
        QueueUrlEntry cached = queueUrlCache.get(queueName);
        Instant now = Instant.now();
        if (cached != null && cached.expireAt().isAfter(now)) {
            return cached.queueUrl();
        }

        String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();
        queueUrlCache.put(queueName, new QueueUrlEntry(queueUrl, now.plus(properties.getQueueUrlCacheTtl())));
        return queueUrl;
    }

    /**
     * Gửi message cho Standard Queue.
     *
     * @param queueName tên queue
     * @param body nội dung message
     * @param attributes message attributes, có thể null
     * @return messageId do SQS trả về
     */
    public String sendMessageStandard(String queueName, String body, Map<String, MessageAttributeValue> attributes) {
        String queueUrl = resolveQueueUrl(queueName);
        SendMessageRequest.Builder builder = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(body);
        if (attributes != null && !attributes.isEmpty()) {
            builder.messageAttributes(attributes);
        }
        return sqsClient.sendMessage(builder.build()).messageId();
    }

    /**
     * Gửi message cho FIFO Queue.
     *
     * @param queueName tên queue FIFO (đuôi .fifo)
     * @param body nội dung message
     * @param messageGroupId group id bắt buộc cho FIFO để giữ ordering theo group
     * @param deduplicationId dedup id tùy chọn; nếu rỗng sẽ tự sinh UUID
     * @param attributes message attributes, có thể null
     * @return messageId do SQS trả về
     */
    public String sendMessageFifo(String queueName, String body, String messageGroupId, String deduplicationId,
            Map<String, MessageAttributeValue> attributes) {
        String queueUrl = resolveQueueUrl(queueName);
        SendMessageRequest.Builder builder = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(body)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId == null || deduplicationId.isBlank()
                        ? UUID.randomUUID().toString()
                        : deduplicationId);

        if (attributes != null && !attributes.isEmpty()) {
            builder.messageAttributes(attributes);
        }
        return sqsClient.sendMessage(builder.build()).messageId();
    }

    /**
     * Nhận danh sách message từ queue.
     *
     * @param queueName tên queue
     * @param maxMessages số message tối đa; null thì dùng default config
     * @param waitTimeSeconds thời gian long-poll; null thì dùng default config
     * @param visibilityTimeoutSeconds visibility timeout; null thì dùng default config
     * @return danh sách message nhận được (có thể rỗng)
     */
    public List<Message> receiveMessages(String queueName, Integer maxMessages, Integer waitTimeSeconds,
            Integer visibilityTimeoutSeconds) {
        String queueUrl = resolveQueueUrl(queueName);

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages == null ? properties.getDefaultMaxMessages() : maxMessages)
                .waitTimeSeconds(waitTimeSeconds == null ? properties.getDefaultWaitTimeSeconds() : waitTimeSeconds)
                .visibilityTimeout(visibilityTimeoutSeconds == null
                        ? properties.getDefaultVisibilityTimeoutSeconds()
                        : visibilityTimeoutSeconds)
                .messageAttributeNames("All")
                .attributeNamesWithStrings("All")
                .build();

        return sqsClient.receiveMessage(request).messages();
    }

    /**
     * Xóa message khỏi queue khi xử lý thành công.
     *
     * @param queueName tên queue
     * @param receiptHandle receipt handle của message cần xóa
     */
    public void deleteMessage(String queueName, String receiptHandle) {
        String queueUrl = resolveQueueUrl(queueName);
        sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build());
    }

    /**
     * Gia hạn visibility timeout cho message đang xử lý lâu.
     *
     * @param queueName tên queue
     * @param receiptHandle receipt handle của message
     * @param visibilityTimeoutSeconds timeout mới (giây)
     */
    public void changeMessageVisibility(String queueName, String receiptHandle, int visibilityTimeoutSeconds) {
        String queueUrl = resolveQueueUrl(queueName);
        sqsClient.changeMessageVisibility(ChangeMessageVisibilityRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .visibilityTimeout(visibilityTimeoutSeconds)
                .build());
    }

    /**
     * Luồng xử lý message an toàn:
     * 1) receive
     * 2) check idempotency
     * 3) start heartbeat extend visibility
     * 4) gọi handler business
     * 5) success -> delete + mark success
     * 6) fail -> mark failed (không delete để SQS retry/DLQ)
     *
     * @param queueName tên queue cần poll
     * @param options tùy chọn xử lý; có thể null để dùng default
     * @param handler handler business cho từng message
     * @return số message xử lý thành công trong lượt gọi này
     */
    public int processMessages(String queueName, ProcessMessageOptions options, SqsMessageHandler handler) {
        // B1) Chuẩn hóa options để mọi nhánh bên dưới đều dùng một bộ tham số đầy đủ và hợp lệ.
        ProcessMessageOptions effective = mergeOptions(options);
        // B2) Poll message từ queue với long polling và visibility timeout đã được hợp nhất.
        List<Message> messages = receiveMessages(
                queueName,
                effective.getMaxMessages(),
                effective.getWaitTimeSeconds(),
                effective.getVisibilityTimeoutSeconds());

        if (messages.isEmpty()) {
            return 0;
        }

        int processed = 0;
        for (Message message : new ArrayList<>(messages)) {
            // B3) Tính idempotency key trước khi gọi handler để chống xử lý trùng giữa các lần poll.
            String key = extractIdempotencyKey(effective, message);
            MessageIdempotencyStore.StartResult start = idempotencyStore.tryStart(
                    key,
                    Duration.ofSeconds(effective.getVisibilityTimeoutSeconds()));

            // B4a) Message đã completed trước đó thì xóa luôn khỏi queue để tránh re-delivery lặp lại.
            if (start == MessageIdempotencyStore.StartResult.ALREADY_COMPLETED) {
                deleteMessage(queueName, message.receiptHandle());
                continue;
            }
            // B4b) Message đang in-progress ở worker khác thì nhường lượt xử lý hiện tại và dời visibility ngắn.
            if (start == MessageIdempotencyStore.StartResult.ALREADY_IN_PROGRESS) {
                changeMessageVisibility(queueName, message.receiptHandle(),
                        Math.max(1, effective.getHeartbeatIntervalSeconds()));
                continue;
            }

            // B5) Start heartbeat để extend visibility định kỳ khi business xử lý lâu hơn timeout ban đầu.
            ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> heartbeatFuture = heartbeatExecutor.scheduleAtFixedRate(() ->
                            changeMessageVisibility(queueName, message.receiptHandle(), effective.getVisibilityTimeoutSeconds()),
                    effective.getHeartbeatIntervalSeconds(),
                    effective.getHeartbeatIntervalSeconds(),
                    TimeUnit.SECONDS);

            try {
                // B6) Chỉ khi idempotency lock đã được chiếm thành công mới gọi business handler thật.
                handler.handle(message);
                // B7a) Thành công thì xóa message và giữ completed TTL để chặn lượt xử lý trùng tiếp theo.
                deleteMessage(queueName, message.receiptHandle());
                idempotencyStore.markSuccess(key, Duration.ofHours(1));
                processed++;
            } catch (Exception ex) {
                // B7b) Thất bại thì không delete để SQS tự retry hoặc đi DLQ, đồng thời bỏ in-progress lock.
                idempotencyStore.markFailed(key);
            } finally {
                // B8) Luôn dừng heartbeat để tránh thread rò rỉ sau khi message đã kết thúc lượt xử lý.
                heartbeatFuture.cancel(true);
                heartbeatExecutor.shutdownNow();
            }
        }

        return processed;
    }

    /**
     * Hợp nhất option caller với default config của common.sqs.
     *
     * @param options options do caller truyền vào (có thể null)
     * @return options hiệu lực sau khi fill default
     */
    private ProcessMessageOptions mergeOptions(ProcessMessageOptions options) {
        ProcessMessageOptions effective = options == null ? new ProcessMessageOptions() : options;
        if (effective.getMaxMessages() <= 0) {
            effective.setMaxMessages(properties.getDefaultMaxMessages());
        }
        if (effective.getWaitTimeSeconds() <= 0) {
            effective.setWaitTimeSeconds(properties.getDefaultWaitTimeSeconds());
        }
        if (effective.getVisibilityTimeoutSeconds() <= 0) {
            effective.setVisibilityTimeoutSeconds(properties.getDefaultVisibilityTimeoutSeconds());
        }
        if (effective.getHeartbeatIntervalSeconds() <= 0) {
            effective.setHeartbeatIntervalSeconds(
                    Math.min(properties.getHeartbeatIntervalSeconds(), effective.getVisibilityTimeoutSeconds() / 2));
            if (effective.getHeartbeatIntervalSeconds() <= 0) {
                effective.setHeartbeatIntervalSeconds(1);
            }
        }
        return effective;
    }

    /**
     * Lấy khóa idempotency:
     * - ưu tiên extractor tùy biến
     * - sau đó fallback dedupId (FIFO)
     * - cuối cùng fallback messageId
     *
     * @param options options đang dùng cho lượt xử lý hiện tại
     * @param message message SQS hiện tại
     * @return idempotency key không rỗng để kiểm soát xử lý trùng
     */
    private String extractIdempotencyKey(ProcessMessageOptions options, Message message) {
        if (options.getIdempotencyKeyExtractor() != null) {
            String extracted = options.getIdempotencyKeyExtractor().apply(message);
            if (extracted != null && !extracted.isBlank()) {
                return extracted;
            }
        }

        String dedupId = message.attributes().get(MessageSystemAttributeName.MESSAGE_DEDUPLICATION_ID);
        return Objects.requireNonNullElse(dedupId, message.messageId());
    }

    private record QueueUrlEntry(String queueUrl, Instant expireAt) {
    }
}
