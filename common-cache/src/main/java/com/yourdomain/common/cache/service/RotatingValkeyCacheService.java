package com.yourdomain.common.cache.service;

import com.yourdomain.common.cache.config.CommonCacheProperties;
import com.yourdomain.common.secretmanager.service.SecretRefreshService;
import java.time.Duration;

public class RotatingValkeyCacheService {

    private final ValkeyTemplateManager templateManager;
    private final SecretRefreshService secretRefreshService;
    private final CommonCacheProperties cacheProperties;

    public RotatingValkeyCacheService(
            ValkeyTemplateManager templateManager,
            SecretRefreshService secretRefreshService,
            CommonCacheProperties cacheProperties
    ) {
        this.templateManager = templateManager;
        this.secretRefreshService = secretRefreshService;
        this.cacheProperties = cacheProperties;
    }

    public String get(String key) {
        String cacheKey = toCacheKey(key);
        return withFallback(() -> templateManager.currentTemplate().opsForValue().get(cacheKey));
    }

    public void set(String key, String value, Duration ttl) {
        String cacheKey = toCacheKey(key);
        withFallback(() -> {
            // TTL missing/invalid => store as persistent key (no expiration).
            if (ttl == null || ttl.isZero() || ttl.isNegative()) {
                templateManager.currentTemplate().opsForValue().set(cacheKey, value);
            } else {
                templateManager.currentTemplate().opsForValue().set(cacheKey, value, ttl);
            }
            return null;
        });
    }

    public void refreshFromLatestSecret() {
        // Initial bootstrap: build Redis template from the latest secret snapshot.
        templateManager.applyIfChanged(secretRefreshService.current());
    }

    public void refreshIfRotated() {
        // Rotation check path: if secret version changed, rebuild template immediately.
        boolean rotated = secretRefreshService.refreshIfVersionChanged();
        if (rotated) {
            templateManager.applyIfChanged(secretRefreshService.current());
        }
    }

    private <T> T withFallback(CacheAction<T> action) {
        try {
            return action.execute();
        } catch (RuntimeException ex) {
            // Fallback path for get/set failure:
            // 1) force refresh latest secret
            // 2) rebuild template if version changed
            // 3) retry once with the updated template
            secretRefreshService.forceRefresh();
            templateManager.applyIfChanged(secretRefreshService.current());
            return action.execute();
        }
    }

    private String toCacheKey(String key) {
        return cacheProperties.getKeyPrefix() + key;
    }

    @FunctionalInterface
    private interface CacheAction<T> {
        T execute();
    }
}
