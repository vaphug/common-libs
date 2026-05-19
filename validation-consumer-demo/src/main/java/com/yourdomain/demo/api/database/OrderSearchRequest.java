package com.yourdomain.demo.api.database;

import java.util.Map;

/**
 * Request body cho search order.
 */
public record OrderSearchRequest(
        Map<String, Object> equalsFilters,
        Integer offset,
        Integer limit,
        boolean includeDeleted
) {
}
