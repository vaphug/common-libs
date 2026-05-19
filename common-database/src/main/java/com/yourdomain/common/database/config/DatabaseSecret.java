package com.yourdomain.common.database.config;

public record DatabaseSecret(
        String host,
        int port,
        String name,
        String schema,
        String username,
        String password
) {
    public String jdbcUrl() {
        return "jdbc:postgresql://" + host + ":" + port + "/" + name;
    }
}
