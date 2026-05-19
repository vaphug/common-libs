package com.yourdomain.common.sqs.model;

import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.sqs.model.Message;

/**
 * Tùy chọn xử lý message.
 *
 * <p>Nếu không set hoặc <= 0, service sẽ dùng default trong CommonSqsProperties.
 */
@Getter
@Setter
public class ProcessMessageOptions {

    /** Thời gian long polling khi gọi receive message. */
    private int waitTimeSeconds;
    /** Visibility timeout áp dụng trong lượt xử lý hiện tại. */
    private int visibilityTimeoutSeconds;
    /** Số lượng message tối đa cần poll trong một lượt receive. */
    private int maxMessages;
    /** Chu kỳ heartbeat dùng để extend visibility khi handler chạy lâu. */
    private int heartbeatIntervalSeconds;
    /** Hàm tùy biến để trích idempotency key từ message. */
    private Function<Message, String> idempotencyKeyExtractor;
}
