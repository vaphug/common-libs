# common-sqs

## Tom tat
Service gửi/nhận/xóa message SQS, hỗ trợ xử lý batch và idempotency.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:36:public class CommonSqsService {
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:45:    public CommonSqsService(
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:61:    public String resolveQueueUrl(String queueName) {
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:81:    public String sendMessageStandard(String queueName, String body, Map<String, MessageAttributeValue> attributes) {
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:102:    public String sendMessageFifo(String queueName, String body, String messageGroupId, String deduplicationId,
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:128:    public List<Message> receiveMessages(String queueName, Integer maxMessages, Integer waitTimeSeconds,
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:152:    public void deleteMessage(String queueName, String receiptHandle) {
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:167:    public void changeMessageVisibility(String queueName, String receiptHandle, int visibilityTimeoutSeconds) {
common-sqs/src/main/java/com/yourdomain/common/sqs/service/CommonSqsService.java:190:    public int processMessages(String queueName, ProcessMessageOptions options, SqsMessageHandler handler) {
common-sqs/src/main/java/com/yourdomain/common/sqs/service/SqsMessageHandler.java:10:public interface SqsMessageHandler {
common-sqs/src/main/java/com/yourdomain/common/sqs/model/ProcessMessageOptions.java:15:public class ProcessMessageOptions {
common-sqs/src/main/java/com/yourdomain/common/sqs/idempotency/MessageIdempotencyStore.java:10:public interface MessageIdempotencyStore {
common-sqs/src/main/java/com/yourdomain/common/sqs/idempotency/InMemoryMessageIdempotencyStore.java:13:public class InMemoryMessageIdempotencyStore implements MessageIdempotencyStore {
common-sqs/src/main/java/com/yourdomain/common/sqs/idempotency/InMemoryMessageIdempotencyStore.java:19:    public StartResult tryStart(String key, Duration lockTtl) {
common-sqs/src/main/java/com/yourdomain/common/sqs/idempotency/InMemoryMessageIdempotencyStore.java:34:    public void markSuccess(String key, Duration completedTtl) {
common-sqs/src/main/java/com/yourdomain/common/sqs/idempotency/InMemoryMessageIdempotencyStore.java:39:    public void markFailed(String key) {
common-sqs/src/main/java/com/yourdomain/common/sqs/config/CommonSqsProperties.java:14:public class CommonSqsProperties {
common-sqs/src/main/java/com/yourdomain/common/sqs/config/CommonSqsAutoConfiguration.java:21:public class CommonSqsAutoConfiguration {
common-sqs/src/main/java/com/yourdomain/common/sqs/config/CommonSqsAutoConfiguration.java:30:    public SqsClient sqsClient() {
common-sqs/src/main/java/com/yourdomain/common/sqs/config/CommonSqsAutoConfiguration.java:41:    public MessageIdempotencyStore messageIdempotencyStore() {
common-sqs/src/main/java/com/yourdomain/common/sqs/config/CommonSqsAutoConfiguration.java:55:    public CommonSqsService commonSqsService(
```

## 3rd-party API / thu vien lien quan
- AWS SQS API: https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/Welcome.html

## Module lien quan
- [common-message-core](../common-message-core)
- [common-validation](../common-validation)
