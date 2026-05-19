package com.yourdomain.common.sqs.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình mặc định cho module {@code common-sqs}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.sqs")
public class CommonSqsProperties {

    /** Bật/tắt auto-config common-sqs. */
    private boolean enabled = true;
    /** TTL cache queueUrl theo queueName. */
    private Duration queueUrlCacheTtl = Duration.ofMinutes(5);
    /** Long polling wait time mặc định khi receive. */
    private int defaultWaitTimeSeconds = 10;
    /** Visibility timeout mặc định khi receive/process. */
    private int defaultVisibilityTimeoutSeconds = 30;
    /** Số message tối đa mỗi lượt receive. */
    private int defaultMaxMessages = 10;
    /** Chu kỳ heartbeat extend visibility khi xử lý lâu. */
    private int heartbeatIntervalSeconds = 10;
}
