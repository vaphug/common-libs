package com.yourdomain.common.sqs.idempotency;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Idempotency store dạng in-memory.
 *
 * <p>Phù hợp local/dev hoặc single instance. Không phù hợp làm cơ chế chống trùng cuối cùng cho multi-instance.
 */
public class InMemoryMessageIdempotencyStore implements MessageIdempotencyStore {

    /** Trạng thái idempotency hiện tại theo từng key trong memory của process hiện tại. */
    private final Map<String, Entry> state = new ConcurrentHashMap<>();

    @Override
    public StartResult tryStart(String key, Duration lockTtl) {
        Instant now = Instant.now();
        Entry current = state.get(key);
        if (current != null && current.expiresAt().isAfter(now)) {
            if (current.completed()) {
                return StartResult.ALREADY_COMPLETED;
            }
            return StartResult.ALREADY_IN_PROGRESS;
        }

        state.put(key, new Entry(false, now.plus(lockTtl)));
        return StartResult.STARTED;
    }

    @Override
    public void markSuccess(String key, Duration completedTtl) {
        state.put(key, new Entry(true, Instant.now().plus(completedTtl)));
    }

    @Override
    public void markFailed(String key) {
        state.remove(key);
    }

    private record Entry(boolean completed, Instant expiresAt) {
    }
}
