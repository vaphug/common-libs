package com.yourdomain.common.notification.history.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourdomain.common.notification.history.repository.JdbcNotificationHistoryRepository;
import com.yourdomain.common.notification.history.repository.NotificationHistoryRepository;
import com.yourdomain.common.notification.history.service.CommonNotificationHistoryService;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Auto-configuration cho module lưu lịch sử notification.
 *
 * <p>Khi ứng dụng có {@link DataSource}, module sẽ tạo {@link JdbcTemplate},
 * JDBC repository mặc định, và service facade để ghi lịch sử gửi notification.
 */
@AutoConfiguration
@EnableConfigurationProperties(CommonNotificationHistoryProperties.class)
@ConditionalOnProperty(prefix = "common.notification.history", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonNotificationHistoryAutoConfiguration {

    /**
     * Tạo {@link JdbcTemplate} dùng riêng cho module history khi application đã có datasource.
     *
     * @param dataSource datasource dùng để ghi bảng lịch sử
     * @return {@link JdbcTemplate} thao tác với database hiện tại
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DataSource.class)
    public JdbcTemplate notificationHistoryJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Tạo repository JDBC mặc định cho bảng history.
     *
     * @param jdbcTemplate template JDBC để thực thi SQL
     * @param properties cấu hình runtime của module history
     * @return repository ghi và cập nhật trạng thái notification history
     */
    @Bean
    @ConditionalOnMissingBean(NotificationHistoryRepository.class)
    @ConditionalOnBean(JdbcTemplate.class)
    public NotificationHistoryRepository notificationHistoryRepository(
            JdbcTemplate jdbcTemplate,
            CommonNotificationHistoryProperties properties
    ) {
        return new JdbcNotificationHistoryRepository(jdbcTemplate, properties);
    }

    /**
     * Tạo {@link ObjectMapper} mặc định dùng để serialize request/response payload vào history.
     *
     * @return object mapper mặc định cho module history
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper notificationHistoryObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * Tạo service facade ghi lịch sử notification.
     *
     * @param repository repository persist dữ liệu history
     * @param objectMapper object mapper dùng để serialize payload
     * @return service ghi record requested, success, và failed
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(NotificationHistoryRepository.class)
    public CommonNotificationHistoryService commonNotificationHistoryService(
            NotificationHistoryRepository repository,
            ObjectMapper objectMapper
    ) {
        return new CommonNotificationHistoryService(repository, objectMapper);
    }
}
