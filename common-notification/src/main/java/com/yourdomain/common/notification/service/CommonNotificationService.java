package com.yourdomain.common.notification.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.yourdomain.common.notification.config.CommonNotificationProperties;
import com.yourdomain.common.notification.config.CommonNotificationProperties.FcmProperties;
import com.yourdomain.common.notification.config.CommonNotificationProperties.HttpChannelProperties;
import com.yourdomain.common.notification.config.CommonNotificationProperties.LineProperties;
import com.yourdomain.common.notification.config.CommonNotificationProperties.TwilioSmsProperties;
import com.yourdomain.common.notification.history.service.CommonNotificationHistoryService;
import com.yourdomain.common.notification.model.NotificationChannel;
import com.yourdomain.common.notification.model.NotificationSendRequest;
import com.yourdomain.common.notification.model.NotificationSendResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

/**
 * Service facade gửi notification qua nhiều channel khác nhau.
 *
 * <p>Module hiện hỗ trợ:
 * - WEBCAS mail qua HTTP provider configurable
 * - push notification qua Firebase Cloud Messaging HTTP v1
 * - LINE push message qua LINE Messaging API
 * - SMS qua Twilio Messages API
 * - email qua AWS SES
 *
 * <p>Mỗi lần gửi sẽ ghi lịch sử request trước khi gọi provider và cập nhật trạng thái
 * success hoặc failed sau khi nhận response hoặc exception.
 */
public class CommonNotificationService {

    private final WebClient.Builder webClientBuilder;
    private final SesClient sesClient;
    private final CommonNotificationProperties properties;
    private final CommonNotificationHistoryService historyService;
    private final ObjectMapper objectMapper;

    public CommonNotificationService(
            WebClient.Builder webClientBuilder,
            SesClient sesClient,
            CommonNotificationProperties properties,
            CommonNotificationHistoryService historyService,
            ObjectMapper objectMapper
    ) {
        this.webClientBuilder = webClientBuilder;
        this.sesClient = sesClient;
        this.properties = properties;
        this.historyService = historyService;
        this.objectMapper = objectMapper;
    }

    /**
     * Gửi email qua provider WEBCAS theo endpoint HTTP được cấu hình runtime.
     *
     * @param request request chứa người nhận, template, header, và payload gửi mail
     * @return kết quả gửi notification sau khi gọi provider
     * @throws IllegalArgumentException khi request không có recipient hoặc cấu hình channel không hợp lệ
     */
    public NotificationSendResult sendWebCasMail(NotificationSendRequest request) {
        return sendHttp(NotificationChannel.WEBCAS_MAIL, properties.getWebCasMail(), request);
    }

    /**
     * Gửi push notification qua Firebase Cloud Messaging HTTP v1.
     *
     * @param request request chứa device token trong trường recipient và phần nội dung notification
     * @return kết quả gửi push sau khi gọi FCM API
     * @throws IllegalArgumentException khi request hoặc cấu hình FCM thiếu dữ liệu bắt buộc
     */
    public NotificationSendResult sendPush(NotificationSendRequest request) {
        return sendFcmPush(request);
    }

    /**
     * Gửi push message qua LINE Messaging API.
     *
     * @param request request chứa user ID hoặc group ID của LINE trong trường recipient
     * @return kết quả gửi message sau khi gọi LINE API
     * @throws IllegalArgumentException khi request hoặc cấu hình LINE thiếu dữ liệu bắt buộc
     */
    public NotificationSendResult sendLINE(NotificationSendRequest request) {
        return sendLinePush(request);
    }

    /**
     * Gửi SMS qua Twilio Messages API.
     *
     * @param request request chứa số điện thoại đích và nội dung SMS
     * @return kết quả gửi SMS sau khi gọi Twilio API
     * @throws IllegalArgumentException khi request hoặc cấu hình Twilio thiếu dữ liệu bắt buộc
     */
    public NotificationSendResult sendSMS(NotificationSendRequest request) {
        return sendTwilioSms(request);
    }

    /**
     * Gửi email qua AWS SES.
     *
     * @param request request chứa người nhận, sender, subject, text body, hoặc html body
     * @return kết quả gửi mail sau khi gọi SES API
     * @throws IllegalArgumentException khi request không có recipient hoặc sender hợp lệ
     */
    public NotificationSendResult sendAWSMail(NotificationSendRequest request) {
        requireText(request.getRecipient(), "recipient");
        String source = firstText(request.getSender(), properties.getAwsMail().getFromAddress());
        requireText(source, "sender");
        String historyId = historyService.recordRequested(
                NotificationChannel.AWS_MAIL.name(),
                request.getRecipient(),
                request.getTemplateId(),
                request);

        NotificationSendResult result = baseResult(NotificationChannel.AWS_MAIL, request, historyId);
        try {
            // Build nội dung mail theo chuẩn SES, chỉ thêm text/html khi caller có truyền dữ liệu.
            Body.Builder bodyBuilder = Body.builder();
            if (request.getText() != null && !request.getText().isBlank()) {
                bodyBuilder.text(Content.builder().data(request.getText()).build());
            }
            if (request.getHtml() != null && !request.getHtml().isBlank()) {
                bodyBuilder.html(Content.builder().data(request.getHtml()).build());
            }
            if ((request.getText() == null || request.getText().isBlank())
                    && (request.getHtml() == null || request.getHtml().isBlank())) {
                bodyBuilder.text(Content.builder().data("").build());
            }

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .source(source)
                    .destination(Destination.builder().toAddresses(request.getRecipient()).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(firstText(request.getSubject(), request.getTitle(), "")).build())
                            .body(bodyBuilder.build())
                            .build())
                    .build();
            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            result.setSuccess(true);
            result.setProviderMessageId(response.messageId());
            result.setStatusCode(200);
            Map<String, Object> responsePayload = new LinkedHashMap<>();
            responsePayload.put("messageId", response.messageId());
            result.setResponseBody(toJson(responsePayload));
            historyService.markSuccess(historyId, result, response.messageId());
            return result;
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setErrorMessage(ex.getMessage());
            historyService.markFailed(historyId, result, ex.getMessage());
            return result;
        }
    }

    /**
     * Gửi notification qua một provider HTTP generic.
     *
     * <p>Method này dành cho các provider chưa có contract cố định trong source, ví dụ WEBCAS.
     *
     * @param channel channel đang gửi
     * @param channelProperties cấu hình endpoint, auth, header, và timeout của provider
     * @param request request notification do caller truyền vào
     * @return kết quả gửi notification sau khi gọi provider
     */
    private NotificationSendResult sendHttp(
            NotificationChannel channel,
            HttpChannelProperties channelProperties,
            NotificationSendRequest request
    ) {
        requireText(request.getRecipient(), "recipient");
        requireText(channelProperties.getBaseUrl(), channel.name() + " baseUrl");
        requireText(channelProperties.getPath(), channel.name() + " path");

        String historyId = historyService.recordRequested(
                channel.name(),
                request.getRecipient(),
                request.getTemplateId(),
                request);
        NotificationSendResult result = baseResult(channel, request, historyId);

        try {
            // Clone builder để mỗi channel có thể gắn base URL riêng mà không làm bẩn builder gốc.
            WebClient webClient = webClientBuilder
                    .clone()
                    .baseUrl(channelProperties.getBaseUrl())
                    .build();
            HttpResponse response = webClient.post()
                    .uri(channelProperties.getPath())
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> applyHeaders(headers, channelProperties, request))
                    .bodyValue(buildHttpBody(channel, request))
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new HttpResponse(clientResponse.statusCode().value(), body)))
                    .block(channelProperties.getTimeout());

            if (response == null) {
                throw new IllegalStateException("No response from " + channel.name());
            }
            result.setStatusCode(response.statusCode());
            result.setResponseBody(response.body());
            result.setProviderMessageId(extractProviderMessageId(response.body()));
            result.setSuccess(response.statusCode() >= 200 && response.statusCode() < 300);
            if (result.isSuccess()) {
                historyService.markSuccess(historyId, result, result.getProviderMessageId());
            } else {
                result.setErrorMessage("Provider returned HTTP " + response.statusCode());
                historyService.markFailed(historyId, result, result.getErrorMessage());
            }
            return result;
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setErrorMessage(ex.getMessage());
            historyService.markFailed(historyId, result, ex.getMessage());
            return result;
        }
    }

    /**
     * Gửi push notification qua Firebase Cloud Messaging HTTP v1 API.
     *
     * @param request request chứa device token và nội dung notification
     * @return kết quả gửi push sau khi gọi FCM API
     */
    private NotificationSendResult sendFcmPush(NotificationSendRequest request) {
        requireText(request.getRecipient(), "recipient");
        FcmProperties push = properties.getPush();
        requireText(push.getBaseUrl(), "FCM baseUrl");
        requireText(push.getProjectId(), "FCM projectId");

        String historyId = historyService.recordRequested(
                NotificationChannel.PUSH.name(),
                request.getRecipient(),
                request.getTemplateId(),
                request);
        NotificationSendResult result = baseResult(NotificationChannel.PUSH, request, historyId);

        try {
            // Lấy OAuth access token scoped cho Firebase Messaging trước khi gọi FCM HTTP v1 API.
            String accessToken = resolveGoogleAccessToken(push);
            HttpResponse response = webClientBuilder.clone()
                    .baseUrl(push.getBaseUrl())
                    .build()
                    .post()
                    .uri("/v1/projects/{projectId}/messages:send", push.getProjectId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .bodyValue(buildFcmBody(request))
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new HttpResponse(clientResponse.statusCode().value(), body)))
                    .block(push.getTimeout());
            return finalizeHttpResult(result, historyId, response);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setErrorMessage(ex.getMessage());
            historyService.markFailed(historyId, result, ex.getMessage());
            return result;
        }
    }

    /**
     * Gửi push message qua LINE Messaging API.
     *
     * @param request request chứa LINE recipient và nội dung message
     * @return kết quả gửi message sau khi gọi LINE API
     */
    private NotificationSendResult sendLinePush(NotificationSendRequest request) {
        requireText(request.getRecipient(), "recipient");
        LineProperties line = properties.getLine();
        requireText(line.getBaseUrl(), "LINE baseUrl");
        requireText(line.getChannelAccessToken(), "LINE channelAccessToken");

        String historyId = historyService.recordRequested(
                NotificationChannel.LINE.name(),
                request.getRecipient(),
                request.getTemplateId(),
                request);
        NotificationSendResult result = baseResult(NotificationChannel.LINE, request, historyId);

        try {
            // B1) Gọi LINE push API với bearer token của channel đã cấu hình.
            HttpResponse response = webClientBuilder.clone()
                    .baseUrl(line.getBaseUrl())
                    .build()
                    .post()
                    .uri("/v2/bot/message/push")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> headers.setBearerAuth(line.getChannelAccessToken()))
                    .bodyValue(buildLineBody(request))
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new HttpResponse(clientResponse.statusCode().value(), body)))
                    .block(line.getTimeout());
            return finalizeHttpResult(result, historyId, response);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setErrorMessage(ex.getMessage());
            historyService.markFailed(historyId, result, ex.getMessage());
            return result;
        }
    }

    /**
     * Gửi SMS qua Twilio Messages API.
     *
     * @param request request chứa số điện thoại nhận, sender tùy chọn, và nội dung body
     * @return kết quả gửi SMS sau khi gọi Twilio API
     */
    private NotificationSendResult sendTwilioSms(NotificationSendRequest request) {
        requireText(request.getRecipient(), "recipient");
        TwilioSmsProperties sms = properties.getSms();
        requireText(sms.getBaseUrl(), "Twilio baseUrl");
        requireText(sms.getAccountSid(), "Twilio accountSid");
        requireText(sms.getAuthToken(), "Twilio authToken");
        String sender = firstText(request.getSender(), sms.getFrom());
        if (isBlank(sender) && isBlank(sms.getMessagingServiceSid())) {
            throw new IllegalArgumentException("Twilio from or messagingServiceSid must not be blank");
        }

        String historyId = historyService.recordRequested(
                NotificationChannel.SMS.name(),
                request.getRecipient(),
                request.getTemplateId(),
                request);
        NotificationSendResult result = baseResult(NotificationChannel.SMS, request, historyId);

        try {
            // B1) Twilio yêu cầu body dạng application/x-www-form-urlencoded thay vì JSON.
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("To", request.getRecipient());
            form.add("Body", firstText(request.getText(), request.getTitle(), ""));
            if (!isBlank(sms.getMessagingServiceSid())) {
                form.add("MessagingServiceSid", sms.getMessagingServiceSid());
            } else {
                form.add("From", sender);
            }

            HttpResponse response = webClientBuilder.clone()
                    .baseUrl(sms.getBaseUrl())
                    .build()
                    .post()
                    .uri("/2010-04-01/Accounts/{accountSid}/Messages.json", sms.getAccountSid())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .headers(headers -> headers.setBasicAuth(sms.getAccountSid(), sms.getAuthToken()))
                    .bodyValue(form)
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new HttpResponse(clientResponse.statusCode().value(), body)))
                    .block(sms.getTimeout());
            return finalizeHttpResult(result, historyId, response);
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setErrorMessage(ex.getMessage());
            historyService.markFailed(historyId, result, ex.getMessage());
            return result;
        }
    }

    /**
     * Build JSON body chuẩn cho provider HTTP generic.
     *
     * @param channel channel đang gửi
     * @param request request notification gốc từ caller
     * @return payload JSON đã merge phần meta chung và payload nghiệp vụ
     */
    private Map<String, Object> buildHttpBody(NotificationChannel channel, NotificationSendRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("channel", channel.name());
        body.put("recipient", request.getRecipient());
        body.put("sender", request.getSender());
        body.put("templateId", request.getTemplateId());
        body.put("subject", request.getSubject());
        body.put("title", request.getTitle());
        body.put("text", request.getText());
        body.put("html", request.getHtml());
        if (request.getPayload() != null && !request.getPayload().isEmpty()) {
            body.putAll(request.getPayload());
        }
        return body;
    }

    /**
     * Build request body cho Firebase Cloud Messaging HTTP v1.
     *
     * @param request request chứa token người nhận, title, text, và data payload
     * @return body JSON theo schema {@code {"message": ...}} của FCM
     */
    private Map<String, Object> buildFcmBody(NotificationSendRequest request) {
        Map<String, Object> message = new LinkedHashMap<>();
        message.put("token", request.getRecipient());
        Map<String, Object> notification = new LinkedHashMap<>();
        notification.put("title", firstText(request.getTitle(), request.getSubject(), ""));
        notification.put("body", firstText(request.getText(), ""));
        message.put("notification", notification);
        if (request.getPayload() != null && !request.getPayload().isEmpty()) {
            Map<String, String> data = new LinkedHashMap<>();
            request.getPayload().forEach((key, value) -> data.put(key, value == null ? "" : String.valueOf(value)));
            message.put("data", data);
        }
        return Map.of("message", message);
    }

    /**
     * Build request body cho LINE push message API.
     *
     * @param request request chứa recipient và nội dung text hoặc messages custom trong payload
     * @return body JSON theo schema LINE Messaging API
     */
    private Map<String, Object> buildLineBody(NotificationSendRequest request) {
        if (request.getPayload() != null && request.getPayload().get("messages") instanceof List<?>) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("to", request.getRecipient());
            body.put("messages", request.getPayload().get("messages"));
            return body;
        }

        Map<String, Object> textMessage = new LinkedHashMap<>();
        textMessage.put("type", "text");
        textMessage.put("text", firstText(request.getText(), request.getTitle(), request.getSubject(), ""));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("to", request.getRecipient());
        body.put("messages", List.of(textMessage));
        return body;
    }

    /**
     * Chuẩn hóa response HTTP từ provider thành {@link NotificationSendResult} và cập nhật history.
     *
     * @param result result object đang được build dần trong luồng gửi
     * @param historyId ID history record đã ghi trước khi gọi provider
     * @param response response HTTP chuẩn hóa gồm status code và raw body
     * @return result cuối cùng sau khi đã gắn status, response body, provider message ID, và history status
     */
    private NotificationSendResult finalizeHttpResult(
            NotificationSendResult result,
            String historyId,
            HttpResponse response
    ) {
        // B1) Chuẩn hóa response provider về status, response body và provider message ID chung.
        if (response == null) {
            throw new IllegalStateException("No response from " + result.getChannel().name());
        }
        result.setStatusCode(response.statusCode());
        result.setResponseBody(response.body());
        result.setProviderMessageId(extractProviderMessageId(response.body()));
        result.setSuccess(response.statusCode() >= 200 && response.statusCode() < 300);
        if (result.isSuccess()) {
            historyService.markSuccess(historyId, result, result.getProviderMessageId());
        } else {
            result.setErrorMessage("Provider returned HTTP " + response.statusCode());
            historyService.markFailed(historyId, result, result.getErrorMessage());
        }
        return result;
    }

    /**
     * Áp dụng header xác thực và header custom cho provider HTTP generic.
     *
     * @param headers HTTP headers đang được build
     * @param channelProperties cấu hình auth/header mặc định của channel
     * @param request request chứa header override do caller truyền vào
     */
    private void applyHeaders(
            HttpHeaders headers,
            HttpChannelProperties channelProperties,
            NotificationSendRequest request
    ) {
        // B1) Áp header tĩnh và auth mặc định của channel trước.
        if (channelProperties.getHeaders() != null) {
            channelProperties.getHeaders().forEach(headers::set);
        }
        if (channelProperties.getBearerToken() != null && !channelProperties.getBearerToken().isBlank()) {
            headers.setBearerAuth(channelProperties.getBearerToken());
        }
        if (channelProperties.getApiKeyHeader() != null && !channelProperties.getApiKeyHeader().isBlank()
                && channelProperties.getApiKey() != null && !channelProperties.getApiKey().isBlank()) {
            headers.set(channelProperties.getApiKeyHeader(), channelProperties.getApiKey());
        }
        // B2) Header override từ request được apply sau cùng để caller có thể ghi đè khi cần.
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(headers::set);
        }
    }

    /**
     * Tạo result object cơ sở trước khi bắt đầu gọi provider.
     *
     * @param channel channel đang gửi
     * @param request request gốc từ caller
     * @param historyId ID history record đã được tạo
     * @return result object chứa sẵn channel, recipient, và history ID
     */
    private NotificationSendResult baseResult(
            NotificationChannel channel,
            NotificationSendRequest request,
            String historyId
    ) {
        NotificationSendResult result = new NotificationSendResult();
        result.setChannel(channel);
        result.setRecipient(request.getRecipient());
        result.setHistoryId(historyId);
        return result;
    }

    /**
     * Trích provider message ID từ JSON response phổ biến của nhiều provider.
     *
     * @param responseBody raw response body dạng JSON
     * @return provider message ID nếu tìm thấy, ngược lại trả về {@code null}
     */
    private String extractProviderMessageId(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            for (String field : new String[] {"messageId", "message_id", "sid", "name", "id", "requestId", "request_id"}) {
                JsonNode value = root.get(field);
                if (value != null && !value.isNull()) {
                    return value.asText();
                }
            }
        } catch (Exception ignored) {
            return null;
        }
        return null;
    }

    /**
     * Serialize object bất kỳ sang JSON để lưu vào result hoặc history.
     *
     * @param value object cần serialize
     * @return chuỗi JSON nếu serialize thành công; fallback sang {@code toString()} khi lỗi
     */
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return String.valueOf(value);
        }
    }

    /**
     * Lấy giá trị chuỗi đầu tiên không rỗng trong danh sách ứng viên.
     *
     * @param values danh sách giá trị cần duyệt theo thứ tự ưu tiên
     * @return chuỗi đầu tiên khác {@code null} và không blank; nếu không có sẽ trả về {@code null}
     */
    private String firstText(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    /**
     * Lấy OAuth access token cho Firebase Messaging scope.
     *
     * <p>Nếu cấu hình có {@code serviceAccountPath} thì dùng file JSON tại path đó.
     * Nếu không, method sẽ fallback sang Google Application Default Credentials.
     *
     * @param push cấu hình FCM hiện tại
     * @return bearer token dùng cho FCM HTTP v1 API
     * @throws IOException nếu không đọc được credentials hoặc refresh token thất bại
     */
    private String resolveGoogleAccessToken(FcmProperties push) throws IOException {
        GoogleCredentials credentials;
        // B1) Ưu tiên service account file nếu caller đã chỉ định path rõ ràng.
        if (push.getServiceAccountPath() != null && !push.getServiceAccountPath().isBlank()) {
            try (FileInputStream inputStream = new FileInputStream(push.getServiceAccountPath())) {
                credentials = GoogleCredentials.fromStream(inputStream);
            }
        } else {
            // B2) Fallback sang Google Application Default Credentials của runtime hiện tại.
            credentials = GoogleCredentials.getApplicationDefault();
        }
        credentials = credentials.createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        if (token == null || token.getTokenValue() == null || token.getTokenValue().isBlank()) {
            // B3) Một số credential source cần refresh chủ động mới trả về access token usable.
            credentials.refresh();
            token = credentials.getAccessToken();
        }
        return token.getTokenValue();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }

    private record HttpResponse(int statusCode, String body) {
    }
}
