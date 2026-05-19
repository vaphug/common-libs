package com.yourdomain.common.webclient.model;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Request chuẩn hóa cho các lệnh gọi HTTP dùng chung trong module web client.
 */
@Getter
@Setter
public class WebClientRequest {

    /** URI hoặc path đích cần gọi. */
    private String uri;
    /** Query parameter cần gắn vào request theo cặp key-value. */
    private Map<String, String> queryParams = new LinkedHashMap<>();
    /** Header override do caller truyền vào cho request hiện tại. */
    private Map<String, String> headers = new LinkedHashMap<>();
    /** Body request tùy chọn cho các method có payload. */
    private Object body;

    public static WebClientRequest of(String uri) {
        WebClientRequest request = new WebClientRequest();
        request.setUri(uri);
        return request;
    }
}
