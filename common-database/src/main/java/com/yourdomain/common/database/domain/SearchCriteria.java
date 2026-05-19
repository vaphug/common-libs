package com.yourdomain.common.database.domain;

import java.util.Collections;
import java.util.Map;

/**
 * Điều kiện tìm kiếm dynamic cho repository generic.
 */
public record SearchCriteria(
        Map<String, Object> equalsFilters,
        Integer offset,
        Integer limit,
        boolean includeDeleted
) {
    public SearchCriteria {
        equalsFilters = equalsFilters == null ? Collections.emptyMap() : equalsFilters;
        offset = offset == null ? 0 : offset;
        limit = limit == null ? 100 : limit;
    }
}
