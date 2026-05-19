package com.yourdomain.common.webclient.config;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * Cấu hình runtime cho module {@code common-webclient}.
 */
@Data
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {

    /** Danh sách header được phép kế thừa từ inbound request sang outbound request. */
    private Map<String, List<String>> inheritHeaders = new HashMap<>();
    /** Giới hạn bộ nhớ đệm tối đa khi đọc response body vào memory. */
    private DataSize maxBufferSize = DataSize.ofKilobytes(256);
}
