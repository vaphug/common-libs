package com.yourdomain.common.cache.service;

import com.yourdomain.common.secretmanager.model.SecretSnapshot;
import com.yourdomain.common.secretmanager.model.ValkeySecret;
import java.util.Map;

public class ValkeySecretMapper {

    public ValkeySecret map(SecretSnapshot snapshot) {
        return map(snapshot.payload());
    }

    public ValkeySecret map(Map<String, Object> payload) {
        String host = stringOrDefault(payload, "host", stringOrDefault(payload, "endpoint", null));
        int port = intOrDefault(payload, "port", 6379);
        String username = stringOrDefault(payload, "username", null);
        String password = stringOrDefault(payload, "password", null);
        int database = intOrDefault(payload, "database", 0);
        boolean ssl = boolOrDefault(payload, "ssl", true);

        if (host == null || host.isBlank()) {
            throw new IllegalStateException("Secret must contain host/endpoint for valkey");
        }
        return new ValkeySecret(host, port, username, password, database, ssl);
    }

    private String stringOrDefault(Map<String, Object> payload, String key, String fallback) {
        Object value = payload.get(key);
        return value == null ? fallback : String.valueOf(value);
    }

    private int intOrDefault(Map<String, Object> payload, String key, int fallback) {
        Object value = payload.get(key);
        if (value == null) {
            return fallback;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private boolean boolOrDefault(Map<String, Object> payload, String key, boolean fallback) {
        Object value = payload.get(key);
        if (value == null) {
            return fallback;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
