package com.yourdomain.common.notification.history.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình runtime cho module {@code common-notification-history}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.notification.history")
public class CommonNotificationHistoryProperties {

    /** Bật hoặc tắt auto-configuration của history module. */
    private boolean enabled = true;
    /** Tên bảng database dùng để lưu các bản ghi lịch sử gửi notification. */
    private String tableName = "notification_history";
}
