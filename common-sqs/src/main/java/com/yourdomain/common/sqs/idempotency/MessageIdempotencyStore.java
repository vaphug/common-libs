package com.yourdomain.common.sqs.idempotency;

import java.time.Duration;

/**
 * Contract chống xử lý trùng message.
 *
 * <p>Khuyến nghị production: thay implementation in-memory bằng Redis/DB để hoạt động đa instance.
 */
public interface MessageIdempotencyStore {

    /**
     * Trạng thái trả về khi service thử chiếm quyền xử lý một idempotency key.
     */
    enum StartResult {
        /** Worker hiện tại vừa chiếm được quyền xử lý key này. */
        STARTED,
        /** Key đã được xử lý xong trước đó và vẫn còn trong completed TTL. */
        ALREADY_COMPLETED,
        /** Key đang được worker khác hoặc lượt xử lý trước giữ trạng thái in-progress. */
        ALREADY_IN_PROGRESS
    }

    /**
     * Thử đánh dấu bắt đầu xử lý key trong một khoảng lock TTL.
     *
     * @param key idempotency key của message
     * @param lockTtl thời gian giữ trạng thái in-progress
     * @return kết quả bắt đầu xử lý (mới bắt đầu, đã hoàn tất, hoặc đang xử lý)
     */
    StartResult tryStart(String key, Duration lockTtl);

    /**
     * Đánh dấu xử lý thành công và giữ trạng thái completed trong một khoảng TTL.
     *
     * @param key idempotency key của message
     * @param completedTtl thời gian giữ trạng thái completed để chống xử lý trùng
     */
    void markSuccess(String key, Duration completedTtl);

    /**
     * Đánh dấu xử lý thất bại để cho phép retry.
     *
     * @param key idempotency key của message
     */
    void markFailed(String key);
}
