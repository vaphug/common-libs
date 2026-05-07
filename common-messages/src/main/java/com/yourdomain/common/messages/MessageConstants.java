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

    // Các key cho ItemValidate (validation theo rule data-driven từ JSON).
    public static final String VALIDATION_ITEM_INVALID = "validation.item.invalid";
    public static final String VALIDATION_CURRENCY_POSITIVE = "validation.currency.positive";
    public static final String VALIDATION_CURRENCY_DENOMINATION = "validation.currency.denomination";
    public static final String VALIDATION_FULLWIDTH_REQUIRED = "validation.fullwidth.required";
    public static final String VALIDATION_HALFWIDTH_REQUIRED = "validation.halfwidth.required";
    public static final String VALIDATION_FULL_AND_HALF_REQUIRED = "validation.fullandhalf.required";
    public static final String VALIDATION_DECIMAL_REQUIRED = "validation.decimal.required";
}
