package com.yourdomain.common.cache.service;

import com.yourdomain.common.secretmanager.model.SecretSnapshot;
import com.yourdomain.common.secretmanager.model.ValkeySecret;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class ValkeyTemplateManager {

    private final AtomicReference<State> state = new AtomicReference<>();
    private final ValkeySecretMapper valkeySecretMapper;

    public ValkeyTemplateManager(ValkeySecretMapper valkeySecretMapper) {
        this.valkeySecretMapper = valkeySecretMapper;
    }

    public RedisTemplate<String, String> currentTemplate() {
        State current = state.get();
        if (current == null) {
            throw new IllegalStateException("Valkey template not initialized");
        }
        return current.template();
    }

    public synchronized boolean applyIfChanged(SecretSnapshot snapshot) {
        State current = state.get();
        if (current != null && Objects.equals(current.version(), snapshot.version())) {
            // Same version => no rotate needed, keep current template.
            return false;
        }

        // Version changed => rotate template to use newest secret config.
        ValkeySecret secret = valkeySecretMapper.map(snapshot);
        RedisTemplate<String, String> template = buildTemplate(secret);
        state.set(new State(snapshot.version(), template));
        return true;
    }

    private RedisTemplate<String, String> buildTemplate(ValkeySecret secret) {
        RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration(secret.host(), secret.port());
        standalone.setDatabase(secret.database());
        if (secret.username() != null && !secret.username().isBlank()) {
            standalone.setUsername(secret.username());
        }
        if (secret.password() != null && !secret.password().isBlank()) {
            standalone.setPassword(RedisPassword.of(secret.password()));
        }

        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientBuilder =
                LettuceClientConfiguration.builder().commandTimeout(Duration.ofSeconds(3));
        if (secret.ssl()) {
            clientBuilder.useSsl();
        }

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(standalone, clientBuilder.build());
        connectionFactory.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    private record State(String version, RedisTemplate<String, String> template) {
    }
}
