package com.yourdomain.common.database.domain;

import java.util.Collections;
import java.util.Map;

/**
 * Mô tả dữ liệu ghi (insert/update) theo dạng map dynamic.
 */
public record WriteCommand(
        Map<String, Object> fields,
        Object expectedModifiedAt,
        String actor
) {
    public WriteCommand {
        fields = fields == null ? Collections.emptyMap() : fields;
        actor = actor == null || actor.isBlank() ? "system" : actor.trim();
    }
}
