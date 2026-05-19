package com.yourdomain.common.notification.history.repository;

import com.yourdomain.common.notification.history.config.CommonNotificationHistoryProperties;
import com.yourdomain.common.notification.history.model.NotificationHistoryRecord;
import com.yourdomain.common.notification.history.model.NotificationHistoryStatus;
import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * JDBC implementation mặc định của {@link NotificationHistoryRepository}.
 *
 * <p>Repository này giả định application đã tự tạo schema và bảng history tương ứng.
 */
public class JdbcNotificationHistoryRepository implements NotificationHistoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;

    public JdbcNotificationHistoryRepository(
            JdbcTemplate jdbcTemplate,
            CommonNotificationHistoryProperties properties
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = validateTableName(properties.getTableName());
    }

    /**
     * Insert một history record mới vào database.
     *
     * @param record bản ghi history đầy đủ cần persist
     */
    @Override
    public void insert(NotificationHistoryRecord record) {
        jdbcTemplate.update("""
                        INSERT INTO %s (
                            id, channel, recipient, template_id, request_payload, response_payload,
                            status, provider_message_id, error_message, created_at
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """.formatted(tableName),
                record.getId(),
                record.getChannel(),
                record.getRecipient(),
                record.getTemplateId(),
                record.getRequestPayload(),
                record.getResponsePayload(),
                record.getStatus().name(),
                record.getProviderMessageId(),
                record.getErrorMessage(),
                Timestamp.valueOf(record.getCreatedAt()));
    }

    /**
     * Update trạng thái và response information của một history record.
     *
     * @param id ID bản ghi cần update
     * @param status trạng thái mới cần lưu
     * @param responsePayload raw response payload đã serialize
     * @param providerMessageId message ID trả về từ provider, có thể {@code null}
     * @param errorMessage thông tin lỗi nếu thất bại, có thể {@code null}
     */
    @Override
    public void updateStatus(
            String id,
            NotificationHistoryStatus status,
            String responsePayload,
            String providerMessageId,
            String errorMessage
    ) {
        jdbcTemplate.update("""
                        UPDATE %s
                        SET status = ?, response_payload = ?, provider_message_id = ?, error_message = ?
                        WHERE id = ?
                        """.formatted(tableName),
                status.name(),
                responsePayload,
                providerMessageId,
                errorMessage,
                id);
    }

    /**
     * Validate tên bảng để tránh SQL injection qua configuration.
     *
     * @param value tên bảng cấu hình từ application properties
     * @return tên bảng hợp lệ chỉ chứa ký tự an toàn cho SQL identifier
     */
    private String validateTableName(String value) {
        if (value == null || !value.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("common.notification.history.table-name is invalid");
        }
        return value;
    }
}
