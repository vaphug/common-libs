package com.yourdomain.common.notification.template.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình runtime cho module {@code common-notification-template}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.notification.template")
public class CommonNotificationTemplateProperties {

    /** Bật hoặc tắt auto-configuration của module template. */
    private boolean enabled = true;
    /** Bucket S3 chứa file CSV template. */
    private String bucket;
    /** Object key S3 của file CSV template. */
    private String key = "notification/templates.csv";
    /** Charset dùng để encode và decode nội dung CSV. */
    private Charset csvCharset = StandardCharsets.UTF_8;
}
