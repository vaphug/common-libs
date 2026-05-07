package com.yourdomain.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Auto-configuration dùng chung cho hệ thống message và bean validation.
 * <p>
 * Mục tiêu:
 * <p>
 * - Khởi tạo {@link MessageSource} một lần ở thư viện core để các module consumer dùng lại.
 * <p>
 * - Gắn {@link MessageSource} vào {@link LocalValidatorFactoryBean} để annotation validation
 * tự resolve message theo i18n properties.
 * <p>
 * Giải thích annotation:
 * <p>
 * - {@link AutoConfiguration}: đánh dấu đây là class cấu hình tự động của Spring Boot.
 * Khi module được đưa vào classpath và được khai báo trong
 * META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports,
 * Spring Boot sẽ tự nạp class này mà không cần app consumer tự @Import.
 * <p>
 * - {@link EnableConfigurationProperties}: yêu cầu Spring tạo bean properties và bind
 * giá trị từ application.yml/application.properties vào class MessageCoreProperties.
 * Nhờ đó module consumer có thể mở rộng danh sách file message qua key common.message.*.
 * <p>
 * Mặc định thư viện đã nạp sẵn bundle lõi, nên app consumer chỉ cần thêm dependency
 * common-message-core là đã đọc được message chung mà không cần khai báo thêm.
 */
@AutoConfiguration
@EnableConfigurationProperties(MessageCoreAutoConfig.MessageCoreProperties.class)
public class MessageCoreAutoConfig {

    /**
     * Tạo {@link MessageSource} dùng cho toàn ứng dụng (nếu app chưa tự định nghĩa bean khác).
     * <p>
     * Dùng {@link ReloadableResourceBundleMessageSource} vì:
     * <p>
     * - Hỗ trợ đọc nhiều basename (nhiều file message từ nhiều module/jar).
     * <p>
     * - Hỗ trợ cơ chế ResourceBundle của Spring cho locale fallback (vi, en, ...).
     * <p>
     * - Có khả năng reload theo cache duration (nếu cần bật thêm cacheSeconds).
     * <p>
     * Đây là lựa chọn phù hợp cho thư viện dùng chung vì module consumer chỉ cần thêm file
     * i18n và khai báo basename, không phải tự tạo MessageSource mới.
     */
    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource(MessageCoreProperties properties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // Tự scan bundle dưới i18n + cho phép app add/override qua common.message.basenames.
        Set<String> basenames = new LinkedHashSet<>(discoverI18nBasenames());
        basenames.addAll(properties.getBasenames());
        messageSource.setBasenames(basenames.toArray(String[]::new));
        messageSource.setDefaultEncoding("UTF-8");
        // Locale mặc định khi không có locale từ request/context.
        messageSource.setDefaultLocale(properties.toDefaultLocale());
        messageSource.setFallbackToSystemLocale(false);
        // Nếu thiếu key thì trả về chính code để dễ phát hiện cấu hình thiếu message.
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    /**
     * Nối MessageSource vào Bean Validation để các message key trong annotation
     * (ví dụ: {valid.phone.required}) được resolve theo locale hiện tại.
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    private List<String> discoverI18nBasenames() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<String> discovered = new LinkedHashSet<>();
        try {
            Resource[] resources = resolver.getResources("classpath*:i18n/*.properties");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null || !filename.endsWith(".properties")) {
                    continue;
                }
                String baseName = filename.substring(0, filename.length() - ".properties".length());
                // Bỏ hậu tố locale để lấy đúng basename: common-messages_vi -> common-messages
                baseName = baseName.replaceFirst("_[a-z]{2}(_[A-Z]{2})?$", "");
                discovered.add("classpath:i18n/" + baseName);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Khong the quet i18n bundles tu classpath", ex);
        }
        return new ArrayList<>(discovered);
    }

    /**
     * Properties cho phần message core.
     * <p>
     * Cấu hình trong application.yml:
     * <p>
     * common.message.basenames:
     * <p>
     * - classpath:i18n/common-messages
     * <p>
     * - classpath:i18n/module-custom-messages
     * <p>
     * common.message.default-locale: vi_VN
     */
    @ConfigurationProperties(prefix = "common.message")
    public static class MessageCoreProperties {
        /**
         * Danh sách basename bổ sung/ghi đè ngoài phần auto-scan i18n.
         * <p>
         * Có thể để trống khi chỉ dùng cơ chế quét tự động classpath*:i18n/*.properties.
         * <p>
         * Ví dụ một phần cấu hình:
         * <p>
         * common:
         *   message:
         *     basenames:
         *       - classpath:i18n/common-messages
         *       - classpath:i18n/sqs-messages
         */
        private List<String> basenames = new ArrayList<>();
        /**
         * Locale mặc định của thư viện message.
         * Dùng format BCP-47 (vi-VN) hoặc kiểu vi_VN.
         */
        private String defaultLocale = "vi_VN";

        public List<String> getBasenames() {
            return basenames;
        }

        public void setBasenames(List<String> basenames) {
            this.basenames = basenames;
        }

        public String getDefaultLocale() {
            return defaultLocale;
        }

        public void setDefaultLocale(String defaultLocale) {
            this.defaultLocale = defaultLocale;
        }

        public Locale toDefaultLocale() {
            return Locale.forLanguageTag(defaultLocale.replace('_', '-'));
        }
    }
}
