package com.yourdomain.common.notification.template.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Thực thể template notification sau khi đã được lưu hoặc đọc từ CSV.
 */
@Getter
@Setter
public class NotificationTemplate {

    /** Định danh duy nhất của template trong file CSV. */
    private String templateId;
    /** Loại template, ví dụ MAIL, SMS, PUSH, hoặc domain-specific type khác. */
    private String type;
    /** Subject dùng cho email hoặc kênh có hỗ trợ subject. */
    private String subject;
    /** Tiêu đề hiển thị của notification. */
    private String title;
    /** Nội dung text thuần của template. */
    private String text;
    /** Nội dung HTML của template, thường dùng cho email. */
    private String html;
    /** Thời điểm tạo template ở dạng chuỗi ISO-8601. */
    private String createdAt;
    /** Người tạo template. */
    private String createdBy;
    /** Thời điểm cập nhật gần nhất ở dạng chuỗi ISO-8601. */
    private String updatedAt;
    /** Người cập nhật gần nhất. */
    private String updatedBy;
}
