package com.yourdomain.common.s3file.config;

import com.yourdomain.common.s3file.service.CommonS3FileService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.S3Presigner.Builder;

/**
 * Auto-configuration cho {@code common-s3file}.
 *
 * <p>Class này khởi tạo các bean AWS S3 mặc định khi ứng dụng chưa tự cung cấp
 * {@link S3Client}, {@link S3Presigner}, hoặc {@link CommonS3FileService}.
 */
@AutoConfiguration
@ConditionalOnClass(S3Client.class)
@EnableConfigurationProperties(CommonS3FileProperties.class)
@ConditionalOnProperty(prefix = "common.s3file", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonS3FileAutoConfiguration {

    /**
     * Tạo {@link S3Client} mặc định cho các thao tác đồng bộ với S3.
     *
     * @param properties cấu hình {@code common.s3file} đã bind từ application properties
     * @return {@link S3Client} sẵn sàng để upload, download, head object, và delete object
     */
    @Bean
    @ConditionalOnMissingBean
    public S3Client s3Client(CommonS3FileProperties properties) {
        S3ClientBuilder builder = S3Client.builder();
        if (properties.getRegion() != null && !properties.getRegion().isBlank()) {
            builder.region(Region.of(properties.getRegion()));
        }
        return builder.build();
    }

    /**
     * Tạo {@link S3Presigner} mặc định để sinh presigned URL tải file.
     *
     * @param properties cấu hình {@code common.s3file} đã bind từ application properties
     * @return {@link S3Presigner} dùng để ký URL GET object với TTL tùy chọn
     */
    @Bean
    @ConditionalOnMissingBean
    public S3Presigner s3Presigner(CommonS3FileProperties properties) {
        Builder builder = S3Presigner.builder();
        if (properties.getRegion() != null && !properties.getRegion().isBlank()) {
            builder.region(Region.of(properties.getRegion()));
        }
        return builder.build();
    }

    /**
     * Tạo facade service cho các thao tác S3 dùng chung.
     *
     * @param s3Client S3 client đồng bộ
     * @param s3Presigner presigner dùng để sinh URL có chữ ký
     * @param properties cấu hình runtime của module
     * @return service wrapper để caller không phải thao tác trực tiếp với AWS SDK
     */
    @Bean
    @ConditionalOnMissingBean
    public CommonS3FileService commonS3FileService(
            S3Client s3Client,
            S3Presigner s3Presigner,
            CommonS3FileProperties properties
    ) {
        return new CommonS3FileService(s3Client, s3Presigner, properties);
    }
}
