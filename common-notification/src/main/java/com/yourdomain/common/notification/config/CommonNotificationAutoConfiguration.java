package com.yourdomain.common.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourdomain.common.notification.history.service.CommonNotificationHistoryService;
import com.yourdomain.common.notification.service.CommonNotificationService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;

/**
 * Auto-configuration cho module {@code common-notification}.
 *
 * <p>Module này tạo các bean cần thiết để gửi notification qua HTTP provider
 * hoặc AWS SES, đồng thời tích hợp với {@code common-notification-history}.
 */
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
@EnableConfigurationProperties(CommonNotificationProperties.class)
@ConditionalOnProperty(prefix = "common.notification", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonNotificationAutoConfiguration {

    /**
     * Tạo {@link WebClient.Builder} mặc định dùng cho các provider HTTP.
     *
     * @return web client builder có thể được service clone và gắn base URL riêng theo từng channel
     */
    @Bean
    @ConditionalOnMissingBean(WebClient.Builder.class)
    public WebClient.Builder notificationWebClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Tạo {@link SesClient} mặc định cho channel AWS mail.
     *
     * @param properties cấu hình runtime của module notification
     * @return SES client sẵn sàng gửi email qua AWS SES
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(SesClient.class)
    public SesClient sesClient(CommonNotificationProperties properties) {
        SesClientBuilder builder = SesClient.builder();
        String region = properties.getAwsMail().getRegion();
        if (region != null && !region.isBlank()) {
            builder.region(Region.of(region));
        }
        return builder.build();
    }

    /**
     * Tạo {@link ObjectMapper} mặc định dùng để parse response provider và serialize payload result.
     *
     * @return object mapper mặc định cho notification module
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper notificationObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * Tạo service facade gửi notification qua các channel được hỗ trợ.
     *
     * @param notificationWebClientBuilder web client builder cho các provider HTTP
     * @param sesClient client AWS SES cho email channel
     * @param properties cấu hình runtime của module
     * @param historyService service ghi lịch sử gửi notification
     * @param objectMapper object mapper cho parse/serialize payload
     * @return service gửi notification dùng chung
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommonNotificationHistoryService.class)
    public CommonNotificationService commonNotificationService(
            WebClient.Builder notificationWebClientBuilder,
            SesClient sesClient,
            CommonNotificationProperties properties,
            CommonNotificationHistoryService historyService,
            ObjectMapper objectMapper
    ) {
        return new CommonNotificationService(
                notificationWebClientBuilder,
                sesClient,
                properties,
                historyService,
                objectMapper);
    }
}
