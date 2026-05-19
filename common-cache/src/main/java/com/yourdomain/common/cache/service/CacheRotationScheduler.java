package com.yourdomain.common.cache.service;

public class CacheRotationScheduler {

    private final RotatingValkeyCacheService cacheService;

    public CacheRotationScheduler(RotatingValkeyCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void refresh() {
        cacheService.refreshIfRotated();
    }
}
