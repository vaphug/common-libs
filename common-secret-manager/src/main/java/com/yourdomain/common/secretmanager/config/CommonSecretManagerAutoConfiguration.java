package com.yourdomain.common.secretmanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourdomain.common.secretmanager.service.AwsSecretsManagerSecretProvider;
import com.yourdomain.common.secretmanager.service.SecretProvider;
import com.yourdomain.common.secretmanager.service.SecretRefreshService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@AutoConfiguration
@ConditionalOnClass(SecretsManagerClient.class)
@EnableConfigurationProperties(SecretManagerProperties.class)
@ConditionalOnProperty(prefix = "common.secret-manager", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonSecretManagerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper commonSecretManagerObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean(SecretProvider.class)
    public SecretProvider secretProvider(SecretManagerProperties properties, ObjectMapper objectMapper) {
        return new AwsSecretsManagerSecretProvider(properties, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecretRefreshService secretRefreshService(SecretProvider secretProvider) {
        SecretRefreshService service = new SecretRefreshService(secretProvider);
        service.forceRefresh();
        return service;
    }
}
