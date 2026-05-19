package com.yourdomain.common.notification.model;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Request chuẩn hóa cho mọi channel gửi notification.
 *
 * <p>Các channel cụ thể có thể dùng toàn bộ field chung này hoặc chỉ dùng một phần.
 * Phần dữ liệu đặc thù có thể được truyền thêm qua {@code payload} và {@code headers}.
 */
@Getter
@Setter
public class NotificationSendRequest {

    /** Đích nhận notification, ví dụ email, phone number, device token, hoặc LINE user ID. */
    private String recipient;
    /** Người gửi hoặc sender override nếu channel cho phép cấu hình theo từng request. */
    private String sender;
    /** Template ID tham chiếu nếu request này được render từ template trước đó. */
    private String templateId;
    /** Subject của email hoặc phần tiêu đề logic cho channel cần subject. */
    private String subject;
    /** Tiêu đề hiển thị cho push, LINE, hoặc fallback cho email subject. */
    private String title;
    /** Nội dung text thuần của notification. */
    private String text;
    /** Nội dung HTML của notification, chủ yếu dùng cho email. */
    private String html;
    /** Payload nghiệp vụ bổ sung cần merge vào request body của provider. */
    private Map<String, Object> payload = new LinkedHashMap<>();
    /** Header override do caller truyền vào cho từng request. */
    private Map<String, String> headers = new LinkedHashMap<>();
}
