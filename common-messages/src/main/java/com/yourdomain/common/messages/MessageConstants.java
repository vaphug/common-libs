package com.yourdomain.common.messages;

/**
 * Nơi khai báo tập trung các message key dùng chung giữa các module.
 * <p>
 * Mục đích:
 * <p>
 * - Tránh hard-code string key ở nhiều nơi.
 * <p>
 * - Giảm lỗi typo key khi dùng trong validation/business logic.
 * <p>
 * - Dễ refactor và theo dõi mapping key -> nội dung i18n.
 */
public final class MessageConstants {
    private MessageConstants() {}

    // Các key lỗi/validation dùng chung.
    public static final String ERR_USER_NOT_FOUND = "err.user.not.found";
    public static final String ERR_SYSTEM_ERROR = "err.system.error";
    public static final String VALID_PHONE_REQUIRED = "valid.phone.required";

    // Các key phục vụ nghiệp vụ SQS (có thể được override bởi module consumer nếu cần).
    public static final String SQS_SEND_FAILED = "sqs.send.failed";
    public static final String SQS_QUEUE_NOT_FOUND = "sqs.queue.not.found";
}
