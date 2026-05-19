package com.yourdomain.common.webclient.client;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class WebClientBuilder {

    private final WebClient.Builder delegate;

    public WebClientBuilder(WebClient.Builder delegate) {
        this.delegate = delegate;
    }

    public WebClientBuilder baseUrl(String baseUrl) {
        if (baseUrl != null && !baseUrl.isBlank()) {
            delegate.baseUrl(baseUrl);
        }
        return this;
    }

    public WebClientBuilder defaultHeader(String name, String value) {
        if (name != null && !name.isBlank() && value != null) {
            delegate.defaultHeader(name, value);
        }
        return this;
    }

    public WebClientBuilder defaultHeaders(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return this;
        }
        headers.forEach(this::defaultHeader);
        return this;
    }

    public WebClientBuilder headers(Consumer<HttpHeaders> headersConsumer) {
        if (headersConsumer != null) {
            delegate.defaultHeaders(headersConsumer);
        }
        return this;
    }

    public WebClientBuilder maxInMemorySize(int maxInMemorySizeBytes) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySizeBytes))
                .build();
        delegate.exchangeStrategies(strategies);
        return this;
    }

    public WebClientBuilder responseTimeout(Duration timeout) {
        if (timeout == null) {
            return this;
        }
        HttpClient httpClient = HttpClient.create().responseTimeout(timeout);
        delegate.clientConnector(new ReactorClientHttpConnector(httpClient));
        return this;
    }

    public WebClient build() {
        return delegate.build();
    }

    public CommonWebClient buildClient() {
        return new CommonWebClient(build());
    }
}
