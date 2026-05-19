package com.yourdomain.demo.api;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/webclient-test")
public class WebClientBufferTestController {

    private final WebClient.Builder webClientBuilder;
    private final int serverPort;

    public WebClientBufferTestController(WebClient.Builder webClientBuilder,
            @Value("${server.port:8080}") int serverPort) {
        this.webClientBuilder = webClientBuilder;
        this.serverPort = serverPort;
    }

    @GetMapping("/source")
    public Map<String, Object> source(@RequestParam(defaultValue = "300") int kb) {
        if (kb <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "kb must be > 0");
        }
        String payload = "x".repeat(kb * 1024);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("kb", kb);
        response.put("sizeBytes", payload.length());
        response.put("payload", payload);
        return response;
    }

    @GetMapping("/fetch")
    public Map<String, Object> fetch(@RequestParam(defaultValue = "300") int kb) {
        String url = "http://localhost:" + serverPort + "/api/webclient-test/source?kb=" + kb;
        try {
            String body = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("ok", true);
            response.put("kb", kb);
            response.put("receivedBytes", body == null ? 0 : body.length());
            return response;
        } catch (Exception ex) {
            Throwable root = rootCause(ex);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("ok", false);
            response.put("kb", kb);
            response.put("errorType", root.getClass().getName());
            response.put("message", root.getMessage());
            return response;
        }
    }

    private Throwable rootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }
}
