package com.yourdomain.common.cache.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình runtime cho module {@code common-cache}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.cache")
public class CommonCacheProperties {

    /** Bật hoặc tắt auto-configuration của cache module. */
    private boolean enabled = true;
    /** Prefix mặc định được thêm vào key cache khi service có áp dụng namespacing. */
    private String keyPrefix = "";
    /** Chu kỳ quét secret/cache metadata để phát hiện rotation. */
    private Duration scanInterval = Duration.ofSeconds(20);
}
