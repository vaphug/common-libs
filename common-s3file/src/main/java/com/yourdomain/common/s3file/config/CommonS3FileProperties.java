package com.yourdomain.common.s3file.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình runtime cho module {@code common-s3file}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.s3file")
public class CommonS3FileProperties {

    /** Bật hoặc tắt auto-configuration của module S3 file. */
    private boolean enabled = true;
    /** Region AWS để khởi tạo {@code S3Client} và {@code S3Presigner}. */
    private String region;
    /** Bucket mặc định khi caller không truyền bucket ở từng request. */
    private String defaultBucket;
    /** TTL mặc định cho presigned URL khi caller không truyền TTL riêng. */
    private Duration presignedUrlTtl = Duration.ofMinutes(15);
}
