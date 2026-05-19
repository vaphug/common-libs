package com.yourdomain.common.cache.config;

import com.yourdomain.common.cache.service.CacheRotationScheduler;
import com.yourdomain.common.cache.service.RotatingValkeyCacheService;
import com.yourdomain.common.cache.service.ValkeySecretMapper;
import com.yourdomain.common.cache.service.ValkeyTemplateManager;
import com.yourdomain.common.secretmanager.service.SecretRefreshService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@AutoConfiguration
@EnableScheduling
@ConditionalOnClass(SecretRefreshService.class)
@EnableConfigurationProperties(CommonCacheProperties.class)
@ConditionalOnProperty(prefix = "common.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ValkeySecretMapper valkeySecretMapper() {
        return new ValkeySecretMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public ValkeyTemplateManager valkeyTemplateManager(ValkeySecretMapper mapper) {
        return new ValkeyTemplateManager(mapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RotatingValkeyCacheService rotatingValkeyCacheService(
            ValkeyTemplateManager templateManager,
            SecretRefreshService secretRefreshService,
            CommonCacheProperties cacheProperties
    ) {
        RotatingValkeyCacheService service = new RotatingValkeyCacheService(
                templateManager,
                secretRefreshService,
                cacheProperties
        );
        // Build initial template at startup from the latest secret.
        service.refreshFromLatestSecret();
        return service;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheRotationScheduler cacheRotationScheduler(RotatingValkeyCacheService cacheService) {
        return new CacheRotationScheduler(cacheService);
    }

    @Bean
    @ConditionalOnMissingBean(name = "commonCacheRotationJob")
    public Runnable commonCacheRotationJob(CacheRotationScheduler scheduler, CommonCacheProperties properties) {
        return new Runnable() {
            @Override
            // Periodic scheduler path (default 20s):
            // poll secret version and rotate template when changed.
            @Scheduled(fixedDelayString = "#{@commonCacheProperties.scanInterval.toMillis()}")
            public void run() {
                scheduler.refresh();
            }
        };
    }

    @Bean("commonCacheProperties")
    @ConditionalOnMissingBean(name = "commonCacheProperties")
    public CommonCacheProperties commonCachePropertiesBean(CommonCacheProperties properties) {
        return properties;
    }
}
