package com.yourdomain.common.secretmanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourdomain.common.secretmanager.config.SecretManagerProperties;
import com.yourdomain.common.secretmanager.model.SecretSnapshot;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class AwsSecretsManagerSecretProvider implements SecretProvider {

    private final SecretManagerProperties properties;
    private final ObjectMapper objectMapper;

    public AwsSecretsManagerSecretProvider(SecretManagerProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public SecretSnapshot fetchCurrent() {
        if (properties.getSecretId() == null || properties.getSecretId().isBlank()) {
            throw new IllegalStateException("common.secret-manager.secret-id is required");
        }

        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(properties.getRegion()))
                .build()) {
            GetSecretValueResponse response = client.getSecretValue(GetSecretValueRequest.builder()
                    .secretId(properties.getSecretId())
                    .build());

            Map<String, Object> payload = objectMapper.readValue(
                    response.secretString(),
                    new TypeReference<>() {
                    });

            return new SecretSnapshot(response.versionId(), payload, Instant.now());
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to parse secret payload", ex);
        }
    }
}
