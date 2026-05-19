package com.yourdomain.common.notification.history.repository;

import com.yourdomain.common.notification.history.model.NotificationHistoryRecord;
import com.yourdomain.common.notification.history.model.NotificationHistoryStatus;

/**
 * Contract persist lịch sử gửi notification.
 */
public interface NotificationHistoryRepository {

    /**
     * Ghi mới một bản ghi history ở trạng thái ban đầu.
     *
     * @param record bản ghi đầy đủ cần insert vào store
     */
    void insert(NotificationHistoryRecord record);

    /**
     * Cập nhật trạng thái thực thi của một history record đã tồn tại.
     *
     * @param id khóa định danh của history record
     * @param status trạng thái mới sau khi gửi provider
     * @param responsePayload payload response đã serialize từ provider hoặc từ service
     * @param providerMessageId message ID trả về từ provider, có thể {@code null}
     * @param errorMessage thông tin lỗi nếu gửi thất bại, có thể {@code null}
     */
    void updateStatus(
            String id,
            NotificationHistoryStatus status,
            String responsePayload,
            String providerMessageId,
            String errorMessage
    );
}
