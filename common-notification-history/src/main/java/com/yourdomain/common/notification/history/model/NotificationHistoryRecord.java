package com.yourdomain.common.notification.history.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Bản ghi lịch sử gửi notification dùng để persist vào database.
 */
@Getter
@Setter
public class NotificationHistoryRecord {

    /** ID duy nhất của bản ghi history. */
    private String id;
    /** Tên channel đã được dùng để gửi notification. */
    private String channel;
    /** Người nhận đích của notification. */
    private String recipient;
    /** Template ID liên quan nếu request dùng template. */
    private String templateId;
    /** Request payload đã được serialize để phục vụ audit/debug. */
    private String requestPayload;
    /** Response payload đã được serialize sau khi provider phản hồi. */
    private String responsePayload;
    /** Trạng thái xử lý cuối cùng của bản ghi history. */
    private NotificationHistoryStatus status;
    /** Mã định danh message do provider trả về nếu có. */
    private String providerMessageId;
    /** Mô tả lỗi cuối cùng nếu request thất bại. */
    private String errorMessage;
    /** Thời điểm tạo bản ghi history. */
    private LocalDateTime createdAt;
}
