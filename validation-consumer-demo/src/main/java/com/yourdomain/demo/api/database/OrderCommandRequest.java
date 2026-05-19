package com.yourdomain.demo.api.database;

import java.util.Map;

/**
 * Request body cho thao tác insert/update order.
 */
public record OrderCommandRequest(
        String actor,
        String expectedModifiedAt,
        Map<String, Object> fields
) {
}
