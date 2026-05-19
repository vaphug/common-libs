package com.yourdomain.common.secretmanager.model;

import java.time.Instant;
import java.util.Map;

public record SecretSnapshot(
        String version,
        Map<String, Object> payload,
        Instant loadedAt
) {
}
