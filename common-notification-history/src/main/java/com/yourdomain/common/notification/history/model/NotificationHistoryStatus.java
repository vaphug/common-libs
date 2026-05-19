package com.yourdomain.common.notification.history.model;

/**
 * Trạng thái lifecycle của một history record.
 */
public enum NotificationHistoryStatus {
    /** Request đã được ghi nhận nhưng chưa có kết quả cuối cùng từ provider. */
    REQUESTED,
    /** Provider đã phản hồi thành công và request được xem là hoàn tất. */
    SUCCESS,
    /** Request thất bại do provider trả lỗi hoặc do exception trong service. */
    FAILED
}
