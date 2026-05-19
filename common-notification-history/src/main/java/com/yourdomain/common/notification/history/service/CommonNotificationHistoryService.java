package com.yourdomain.common.notification.history.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourdomain.common.notification.history.model.NotificationHistoryRecord;
import com.yourdomain.common.notification.history.model.NotificationHistoryStatus;
import com.yourdomain.common.notification.history.repository.NotificationHistoryRepository;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service facade cho luồng ghi lịch sử gửi notification.
 *
 * <p>Service này chịu trách nhiệm:
 * - tạo record trạng thái {@code REQUESTED} trước khi gọi provider
 * - cập nhật sang {@code SUCCESS} khi provider trả về thành công
 * - cập nhật sang {@code FAILED} khi provider hoặc service gặp lỗi
 */
public class CommonNotificationHistoryService {

    private final NotificationHistoryRepository repository;
    private final ObjectMapper objectMapper;

    public CommonNotificationHistoryService(NotificationHistoryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Ghi mới một history record trước khi bắt đầu gọi provider.
     *
     * @param channel tên channel đang gửi, ví dụ {@code SMS} hoặc {@code AWS_MAIL}
     * @param recipient người nhận đích của notification
     * @param templateId template đang dùng; có thể {@code null} nếu gửi trực tiếp không qua template
     * @param requestPayload object request gốc cần serialize vào history
     * @return ID duy nhất của history record vừa được tạo
     */
    public String recordRequested(String channel, String recipient, String templateId, Object requestPayload) {
        NotificationHistoryRecord record = new NotificationHistoryRecord();
        record.setId(UUID.randomUUID().toString());
        record.setChannel(channel);
        record.setRecipient(recipient);
        record.setTemplateId(templateId);
        record.setRequestPayload(toJson(requestPayload));
        record.setStatus(NotificationHistoryStatus.REQUESTED);
        record.setCreatedAt(LocalDateTime.now());
        repository.insert(record);
        return record.getId();
    }

    /**
     * Đánh dấu một history record là gửi thành công.
     *
     * @param id ID của history record cần cập nhật
     * @param responsePayload object response cần serialize vào history
     * @param providerMessageId mã định danh message do provider trả về, có thể {@code null}
     */
    public void markSuccess(String id, Object responsePayload, String providerMessageId) {
        repository.updateStatus(id, NotificationHistoryStatus.SUCCESS, toJson(responsePayload), providerMessageId, null);
    }

    /**
     * Đánh dấu một history record là gửi thất bại.
     *
     * @param id ID của history record cần cập nhật
     * @param responsePayload object response hoặc result cần serialize vào history
     * @param errorMessage mô tả lỗi cuối cùng dùng để điều tra sự cố
     */
    public void markFailed(String id, Object responsePayload, String errorMessage) {
        repository.updateStatus(id, NotificationHistoryStatus.FAILED, toJson(responsePayload), null, errorMessage);
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to serialize notification history payload", ex);
        }
    }
}
