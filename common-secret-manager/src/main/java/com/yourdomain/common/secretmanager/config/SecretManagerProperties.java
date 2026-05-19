package com.yourdomain.common.secretmanager.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình runtime cho module {@code common-secret-manager}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.secret-manager")
public class SecretManagerProperties {

    /** Bật hoặc tắt auto-configuration của secret manager module. */
    private boolean enabled = true;
    /** Secret ID chính dùng để đọc payload từ secret provider. */
    private String secretId;
    /** AWS region mặc định khi khởi tạo client đọc secret. */
    private String region = "ap-northeast-1";
    /** Chu kỳ refresh secret định kỳ nếu scheduler đang bật. */
    private Duration refreshInterval = Duration.ofSeconds(20);
}
