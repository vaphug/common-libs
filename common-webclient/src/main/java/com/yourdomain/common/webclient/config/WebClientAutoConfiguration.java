package com.yourdomain.common.webclient.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Auto-configuration cho module {@code common-webclient}.
 *
 * <p>Class này khởi tạo {@link WebClient.Builder} mặc định và áp dụng giới hạn buffer
 * đọc response theo cấu hình runtime của module.
 */
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientAutoConfiguration {

    /**
     * Tạo {@link WebClient.Builder} mặc định cho toàn module.
     *
     * @param properties cấu hình runtime của web client module
     * @return builder đã được cấu hình max in-memory buffer size cho response body
     */
    @Bean
    @ConditionalOnMissingBean(WebClient.Builder.class)
    public WebClient.Builder webClientBuilder(WebClientProperties properties) {
        return WebClient.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(toIntByteSize(properties.getMaxBufferSize())));
    }

    /**
     * Chuyển {@code DataSize} sang số byte dạng {@code int} để cấu hình codec buffer.
     *
     * @param dataSize kích thước buffer mong muốn từ cấu hình
     * @return số byte tương ứng ở dạng {@code int}
     * @throws IllegalArgumentException khi kích thước vượt quá giới hạn {@code int}
     */
    private int toIntByteSize(org.springframework.util.unit.DataSize dataSize) {
        long bytes = dataSize.toBytes();
        if (bytes > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("webclient.max-buffer-size must be <= " + Integer.MAX_VALUE + " bytes");
        }
        return (int) bytes;
    }
}
