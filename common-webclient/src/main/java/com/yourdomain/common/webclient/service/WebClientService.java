package com.yourdomain.common.webclient.service;

import com.yourdomain.common.webclient.client.CommonWebClient;
import com.yourdomain.common.webclient.context.UserContextHelper;
import com.yourdomain.common.webclient.model.WebClientRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

public class WebClientService {

    private final CommonWebClient commonWebClient;
    private final UserContextHelper userContextHelper;

    public WebClientService(CommonWebClient commonWebClient, UserContextHelper userContextHelper) {
        this.commonWebClient = commonWebClient;
        this.userContextHelper = userContextHelper;
    }

    public <T> Mono<T> get(WebClientRequest request, Class<T> responseType) {
        return commonWebClient.get(request.getUri(), request.getQueryParams(),
                headers -> headers.addAll(toHttpHeaders(request.getHeaders())), responseType);
    }

    public <T> Mono<List<T>> getList(WebClientRequest request, Class<T> itemType) {
        return commonWebClient.getList(request.getUri(), request.getQueryParams(),
                headers -> headers.addAll(toHttpHeaders(request.getHeaders())), itemType);
    }

    public <T> Mono<T> post(WebClientRequest request, Class<T> responseType) {
        return commonWebClient.post(request.getUri(), request.getBody(),
                headers -> headers.addAll(toHttpHeaders(request.getHeaders())), responseType);
    }

    public <T> Mono<T> put(WebClientRequest request, Class<T> responseType) {
        return commonWebClient.put(request.getUri(), request.getBody(),
                headers -> headers.addAll(toHttpHeaders(request.getHeaders())), responseType);
    }

    public <T> Mono<T> delete(WebClientRequest request, Class<T> responseType) {
        return commonWebClient.delete(request.getUri(),
                headers -> headers.addAll(toHttpHeaders(request.getHeaders())), responseType);
    }

    public Map<String, String> inheritedHeaders(Map<String, String> incomingHeaders, boolean batchMode) {
        return userContextHelper.resolveInheritedHeaders(incomingHeaders, batchMode);
    }

    private HttpHeaders toHttpHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers == null || headers.isEmpty()) {
            return httpHeaders;
        }
        Map<String, String> safeHeaders = new LinkedHashMap<>(headers);
        safeHeaders.forEach(httpHeaders::add);
        return httpHeaders;
    }
}
