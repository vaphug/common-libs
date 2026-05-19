package com.yourdomain.common.webclient.context;

import com.yourdomain.common.webclient.config.HeaderNames;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpHeaders;

public class UserContextHelper {

    private static final List<String> WEB_INHERITED_HEADERS = List.of(
            HeaderNames.REQUEST_ID,
            HeaderNames.CORRELATION_ID,
            HeaderNames.USER_CONTEXT,
            HeaderNames.CLIENT_IP);

    private static final List<String> BATCH_INHERITED_HEADERS = List.of(
            HeaderNames.REQUEST_ID,
            HeaderNames.CORRELATION_ID);

    public Map<String, String> resolveInheritedHeaders(Map<String, String> incomingHeaders, boolean batchMode) {
        if (incomingHeaders == null || incomingHeaders.isEmpty()) {
            return Map.of();
        }

        List<String> inherited = batchMode
                ? BATCH_INHERITED_HEADERS
                : WEB_INHERITED_HEADERS;

        Map<String, String> normalizedIncoming = normalizeKeys(incomingHeaders);
        Map<String, String> resolved = new LinkedHashMap<>();

        for (String headerName : inherited) {
            String value = normalizedIncoming.get(toLookupKey(headerName));
            if (value != null && !value.isBlank()) {
                resolved.put(headerName, value);
            }
        }
        return resolved;
    }

    public Map<String, String> resolveInheritedHeaders(HttpHeaders incomingHeaders, boolean batchMode) {
        if (incomingHeaders == null || incomingHeaders.isEmpty()) {
            return Map.of();
        }
        Map<String, String> map = new LinkedHashMap<>();
        incomingHeaders.forEach((key, values) -> {
            if (values != null && !values.isEmpty()) {
                map.put(key, values.getFirst());
            }
        });
        return resolveInheritedHeaders(map, batchMode);
    }

    private Map<String, String> normalizeKeys(Map<String, String> source) {
        Map<String, String> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : source.entrySet()) {
            normalized.put(toLookupKey(entry.getKey()), entry.getValue());
        }
        return normalized;
    }

    private String toLookupKey(String headerName) {
        return headerName.toLowerCase(Locale.ROOT);
    }
}
