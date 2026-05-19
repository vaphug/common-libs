package com.yourdomain.common.notification.template.config;

import com.yourdomain.common.notification.template.service.CommonNotificationTemplateService;
import com.yourdomain.common.s3file.service.CommonS3FileService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration cho module template notification lưu trên S3.
 */
@AutoConfiguration
@EnableConfigurationProperties(CommonNotificationTemplateProperties.class)
@ConditionalOnProperty(prefix = "common.notification.template", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonNotificationTemplateAutoConfiguration {

    /**
     * Tạo service quản lý template khi ứng dụng đã có {@link CommonS3FileService}.
     *
     * @param s3FileService service thao tác S3 dùng để đọc và ghi file CSV template
     * @param properties cấu hình bucket, object key, và charset của module template
     * @return service CRUD template lưu trên S3
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommonS3FileService.class)
    public CommonNotificationTemplateService commonNotificationTemplateService(
            CommonS3FileService s3FileService,
            CommonNotificationTemplateProperties properties
    ) {
        return new CommonNotificationTemplateService(s3FileService, properties);
    }
}
