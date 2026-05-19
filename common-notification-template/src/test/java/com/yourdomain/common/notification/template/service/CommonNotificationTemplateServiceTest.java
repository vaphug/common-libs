package com.yourdomain.common.notification.template.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yourdomain.common.notification.template.config.CommonNotificationTemplateProperties;
import com.yourdomain.common.notification.template.model.CreateTemplateRequest;
import com.yourdomain.common.notification.template.model.NotificationTemplate;
import com.yourdomain.common.notification.template.model.UpdateTemplateRequest;
import com.yourdomain.common.s3file.config.CommonS3FileProperties;
import com.yourdomain.common.s3file.service.CommonS3FileService;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CommonNotificationTemplateServiceTest {

    @Test
    void createUpdateGetAndDeleteTemplateUsingCsvEscaping() {
        FakeS3FileService s3 = new FakeS3FileService();
        CommonNotificationTemplateProperties properties = new CommonNotificationTemplateProperties();
        properties.setBucket("bucket-a");
        properties.setKey("templates.csv");
        CommonNotificationTemplateService service = new CommonNotificationTemplateService(s3, properties);

        CreateTemplateRequest create = new CreateTemplateRequest();
        create.setTemplateId("welcome");
        create.setType("MAIL");
        create.setSubject("Hello, \"User\"");
        create.setTitle("Welcome");
        create.setText("Line 1\nLine 2");
        create.setHtml("<b>Hello</b>");
        create.setCreatedBy("tester");

        NotificationTemplate created = service.createTemplate(create);
        assertThat(created.getTemplateId()).isEqualTo("welcome");
        assertThat(s3.content).contains("\"Hello, \"\"User\"\"\"");

        Optional<NotificationTemplate> found = service.getTemplate("welcome");
        assertThat(found).isPresent();
        assertThat(found.get().getSubject()).isEqualTo("Hello, \"User\"");
        assertThat(found.get().getText()).isEqualTo("Line 1\nLine 2");

        UpdateTemplateRequest update = new UpdateTemplateRequest();
        update.setTitle("Updated");
        update.setUpdatedBy("editor");
        NotificationTemplate updated = service.updateTemplate("welcome", update);
        assertThat(updated.getTemplateId()).isEqualTo("welcome");
        assertThat(updated.getTitle()).isEqualTo("Updated");
        assertThat(updated.getSubject()).isEqualTo("Hello, \"User\"");

        service.deleteTemplate("welcome");
        assertThat(service.getTemplate("welcome")).isEmpty();
    }

    private static class FakeS3FileService extends CommonS3FileService {

        private String content;

        FakeS3FileService() {
            super(null, null, new CommonS3FileProperties());
        }

        @Override
        public boolean objectExists(String bucket, String key) {
            return content != null;
        }

        @Override
        public byte[] getObject(String bucket, String key) {
            return content.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void uploadByBytes(String bucket, String key, byte[] bytes, String contentType) {
            this.content = new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
