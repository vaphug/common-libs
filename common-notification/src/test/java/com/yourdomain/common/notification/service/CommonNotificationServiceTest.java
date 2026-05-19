package com.yourdomain.common.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourdomain.common.notification.config.CommonNotificationProperties;
import com.yourdomain.common.notification.history.model.NotificationHistoryRecord;
import com.yourdomain.common.notification.history.model.NotificationHistoryStatus;
import com.yourdomain.common.notification.history.repository.NotificationHistoryRepository;
import com.yourdomain.common.notification.history.service.CommonNotificationHistoryService;
import com.yourdomain.common.notification.model.NotificationSendRequest;
import com.yourdomain.common.notification.model.NotificationSendResult;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.ses.SesClient;

class CommonNotificationServiceTest {

    @Test
    void sendSmsPostsConfiguredHttpRequestAndWritesHistory() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "application/json")
                    .setBody("{\"messageId\":\"sms-1\"}"));
            server.start();

            CommonNotificationProperties properties = new CommonNotificationProperties();
            properties.getSms().setBaseUrl(server.url("/").toString());
            properties.getSms().setAccountSid("AC123");
            properties.getSms().setAuthToken("secret");
            properties.getSms().setFrom("+15550000000");
            InMemoryHistoryRepository repository = new InMemoryHistoryRepository();
            CommonNotificationService service = new CommonNotificationService(
                    WebClient.builder(),
                    mock(SesClient.class),
                    properties,
                    new CommonNotificationHistoryService(repository, new ObjectMapper()),
                    new ObjectMapper());

            NotificationSendRequest request = new NotificationSendRequest();
            request.setRecipient("+84900000000");
            request.setText("Hello");

            NotificationSendResult result = service.sendSMS(request);

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getProviderMessageId()).isEqualTo("sms-1");
            assertThat(repository.status).isEqualTo(NotificationHistoryStatus.SUCCESS);
            okhttp3.mockwebserver.RecordedRequest recorded = server.takeRequest();
            assertThat(recorded.getPath()).isEqualTo("/2010-04-01/Accounts/AC123/Messages.json");
            assertThat(recorded.getHeader("Authorization")).startsWith("Basic ");
            assertThat(recorded.getBody().readUtf8())
                    .contains("To=%2B84900000000")
                    .contains("From=%2B15550000000")
                    .contains("Body=Hello");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AssertionError(ex);
        }
    }

    private static class InMemoryHistoryRepository implements NotificationHistoryRepository {

        private String id;
        private NotificationHistoryStatus status;

        @Override
        public void insert(NotificationHistoryRecord record) {
            this.id = record.getId();
            this.status = record.getStatus();
        }

        @Override
        public void updateStatus(
                String id,
                NotificationHistoryStatus status,
                String responsePayload,
                String providerMessageId,
                String errorMessage
        ) {
            assertThat(id).isEqualTo(this.id);
            this.status = status;
        }
    }
}
