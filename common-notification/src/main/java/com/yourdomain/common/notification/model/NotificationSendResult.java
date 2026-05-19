package com.yourdomain.common.notification.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Kết quả chuẩn hóa sau khi gửi notification qua một channel bất kỳ.
 */
@Getter
@Setter
public class NotificationSendResult {

    /** Cờ thành công cuối cùng sau khi đánh giá response hoặc exception từ provider. */
    private boolean success;
    /** Channel thực tế đã được dùng để gửi notification. */
    private NotificationChannel channel;
    /** Đích nhận notification đã được gửi đến. */
    private String recipient;
    /** ID của history record được tạo trước khi gọi provider. */
    private String historyId;
    /** Provider message ID nếu response của provider có trả về. */
    private String providerMessageId;
    /** HTTP status code hoặc status logic tương đương từ provider. */
    private int statusCode;
    /** Raw response body trả về từ provider sau khi gửi. */
    private String responseBody;
    /** Thông tin lỗi cuối cùng nếu request thất bại. */
    private String errorMessage;
}
