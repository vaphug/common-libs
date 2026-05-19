package com.yourdomain.common.secretmanager.model;

public record ValkeySecret(
        String host,
        int port,
        String username,
        String password,
        int database,
        boolean ssl
) {
}
