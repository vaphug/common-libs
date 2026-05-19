package com.yourdomain.common.webclient.client;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CommonWebClient {

    private final WebClient webClient;

    public CommonWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> get(String uri, Class<T> responseType) {
        return get(uri, null, null, responseType);
    }

    public <T> Mono<T> get(String uri, Map<String, String> queryParams, Consumer<HttpHeaders> headersCustomizer,
            Class<T> responseType) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(uri);
                    applyQueryParams(uriBuilder, queryParams);
                    return uriBuilder.build();
                })
                .headers(headers -> applyHeaders(headers, headersCustomizer))
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<List<T>> getList(String uri, Class<T> itemType) {
        return getList(uri, null, null, itemType);
    }

    public <T> Mono<List<T>> getList(String uri, Map<String, String> queryParams, Consumer<HttpHeaders> headersCustomizer,
            Class<T> itemType) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(uri);
                    applyQueryParams(uriBuilder, queryParams);
                    return uriBuilder.build();
                })
                .headers(headers -> applyHeaders(headers, headersCustomizer))
                .retrieve()
                .bodyToFlux(itemType)
                .collectList();
    }

    public <T, B> Mono<T> post(String uri, B body, Class<T> responseType) {
        return post(uri, body, null, responseType);
    }

    public <T, B> Mono<T> post(String uri, B body, Consumer<HttpHeaders> headersCustomizer, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .headers(headers -> applyHeaders(headers, headersCustomizer))
                .body(body == null ? BodyInserters.empty() : BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T, B> Mono<T> put(String uri, B body, Class<T> responseType) {
        return put(uri, body, null, responseType);
    }

    public <T, B> Mono<T> put(String uri, B body, Consumer<HttpHeaders> headersCustomizer, Class<T> responseType) {
        return webClient.put()
                .uri(uri)
                .headers(headers -> applyHeaders(headers, headersCustomizer))
                .body(body == null ? BodyInserters.empty() : BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> delete(String uri, Class<T> responseType) {
        return delete(uri, null, responseType);
    }

    public <T> Mono<T> delete(String uri, Consumer<HttpHeaders> headersCustomizer, Class<T> responseType) {
        return webClient.delete()
                .uri(uri)
                .headers(headers -> applyHeaders(headers, headersCustomizer))
                .retrieve()
                .bodyToMono(responseType);
    }

    public Mono<Void> delete(String uri) {
        return webClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private void applyQueryParams(org.springframework.web.util.UriBuilder uriBuilder, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }
    }

    private void applyHeaders(HttpHeaders headers, Consumer<HttpHeaders> headersCustomizer) {
        if (headersCustomizer != null) {
            headersCustomizer.accept(headers);
        }
    }
}
