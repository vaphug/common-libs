package com.yourdomain.common.database.config;

import com.yourdomain.common.secretmanager.model.SecretSnapshot;
import java.util.Map;

public class DatabaseSecretMapper {

    public DatabaseSecret map(SecretSnapshot snapshot) {
        return map(snapshot.payload());
    }

    public DatabaseSecret map(Map<String, Object> payload) {
        String host = stringOrDefault(payload, "dbHost", stringOrDefault(payload, "host", "localhost"));
        int port = intOrDefault(payload, "dbPort", intOrDefault(payload, "port", 5432));
        String name = stringOrDefault(payload, "dbName", stringOrDefault(payload, "database", "inventory_db"));
        String schema = stringOrDefault(payload, "schema", "public");
        String username = stringOrDefault(payload, "username", "postgres");
        String password = stringOrDefault(payload, "password", "postgres");
        return new DatabaseSecret(host, port, name, schema, username, password);
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
}
