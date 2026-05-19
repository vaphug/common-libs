package com.yourdomain.common.notification.template.service;

import com.yourdomain.common.notification.template.config.CommonNotificationTemplateProperties;
import com.yourdomain.common.notification.template.model.CreateTemplateRequest;
import com.yourdomain.common.notification.template.model.NotificationTemplate;
import com.yourdomain.common.notification.template.model.UpdateTemplateRequest;
import com.yourdomain.common.s3file.service.CommonS3FileService;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * Service CRUD template notification lưu dưới dạng CSV trên S3.
 *
 * <p>Module này dùng chiến lược read-modify-write:
 * - đọc toàn bộ CSV hiện tại từ S3
 * - cập nhật danh sách template trong memory
 * - ghi đè toàn bộ CSV trở lại S3
 *
 * <p>Cách làm này đơn giản, dễ audit, và phù hợp khi số lượng template nhỏ đến vừa.
 */
public class CommonNotificationTemplateService {

    private static final String[] HEADERS = {
            "templateId", "type", "subject", "title", "text", "html",
            "createdAt", "createdBy", "updatedAt", "updatedBy"
    };

    private final CommonS3FileService s3FileService;
    private final CommonNotificationTemplateProperties properties;

    public CommonNotificationTemplateService(
            CommonS3FileService s3FileService,
            CommonNotificationTemplateProperties properties
    ) {
        this.s3FileService = s3FileService;
        this.properties = properties;
    }

    /**
     * Tạo mới một template và lưu lại vào file CSV trên S3.
     *
     * @param request request tạo template, chứa template ID, nội dung subject/title/text/html, và người tạo
     * @return template vừa được tạo sau khi đã gắn createdAt và updatedAt
     */
    public NotificationTemplate createTemplate(CreateTemplateRequest request) {
        requireText(request.getTemplateId(), "templateId");
        List<NotificationTemplate> templates = readTemplates();
        boolean exists = templates.stream().anyMatch(template -> request.getTemplateId().equals(template.getTemplateId()));
        if (exists) {
            throw new IllegalArgumentException("Template already exists: " + request.getTemplateId());
        }

        String now = Instant.now().toString();
        NotificationTemplate template = new NotificationTemplate();
        template.setTemplateId(request.getTemplateId());
        template.setType(request.getType());
        template.setSubject(request.getSubject());
        template.setTitle(request.getTitle());
        template.setText(request.getText());
        template.setHtml(request.getHtml());
        template.setCreatedAt(now);
        template.setCreatedBy(request.getCreatedBy());
        template.setUpdatedAt(now);
        template.setUpdatedBy(request.getCreatedBy());
        templates.add(template);
        writeTemplates(templates);
        return template;
    }

    /**
     * Cập nhật template hiện có theo template ID.
     *
     * @param templateId định danh duy nhất của template cần cập nhật
     * @param request request cập nhật nội dung template và thông tin người cập nhật
     * @return template sau khi đã merge dữ liệu mới và cập nhật thời gian sửa đổi
     */
    public NotificationTemplate updateTemplate(String templateId, UpdateTemplateRequest request) {
        requireText(templateId, "templateId");
        List<NotificationTemplate> templates = readTemplates();
        NotificationTemplate template = templates.stream()
                .filter(item -> templateId.equals(item.getTemplateId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        if (request.getType() != null) {
            template.setType(request.getType());
        }
        if (request.getSubject() != null) {
            template.setSubject(request.getSubject());
        }
        if (request.getTitle() != null) {
            template.setTitle(request.getTitle());
        }
        if (request.getText() != null) {
            template.setText(request.getText());
        }
        if (request.getHtml() != null) {
            template.setHtml(request.getHtml());
        }
        template.setUpdatedAt(Instant.now().toString());
        template.setUpdatedBy(request.getUpdatedBy());
        writeTemplates(templates);
        return template;
    }

    /**
     * Tìm một template theo template ID.
     *
     * @param templateId định danh template cần tra cứu
     * @return {@link Optional} chứa template nếu tồn tại, ngược lại trả về rỗng
     */
    public Optional<NotificationTemplate> getTemplate(String templateId) {
        requireText(templateId, "templateId");
        return readTemplates().stream()
                .filter(template -> templateId.equals(template.getTemplateId()))
                .findFirst();
    }

    /**
     * Xóa một template khỏi file CSV trên S3.
     *
     * @param templateId định danh template cần xóa
     */
    public void deleteTemplate(String templateId) {
        requireText(templateId, "templateId");
        List<NotificationTemplate> templates = readTemplates();
        boolean removed = templates.removeIf(template -> templateId.equals(template.getTemplateId()));
        if (!removed) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }
        writeTemplates(templates);
    }

    /**
     * Đọc toàn bộ danh sách template hiện có từ file CSV trên S3.
     *
     * @return danh sách template đã parse từ CSV; trả về danh sách rỗng nếu object chưa tồn tại hoặc file rỗng
     */
    private List<NotificationTemplate> readTemplates() {
        if (!s3FileService.objectExists(properties.getBucket(), properties.getKey())) {
            return new ArrayList<>();
        }
        String content = new String(
                s3FileService.getObject(properties.getBucket(), properties.getKey()),
                properties.getCsvCharset());
        if (content.isBlank()) {
            return new ArrayList<>();
        }

        try (StringReader reader = new StringReader(content);
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)) {
            List<NotificationTemplate> templates = new ArrayList<>();
            for (CSVRecord record : parser) {
                NotificationTemplate template = new NotificationTemplate();
                template.setTemplateId(record.get("templateId"));
                template.setType(record.get("type"));
                template.setSubject(record.get("subject"));
                template.setTitle(record.get("title"));
                template.setText(record.get("text"));
                template.setHtml(record.get("html"));
                template.setCreatedAt(record.get("createdAt"));
                template.setCreatedBy(record.get("createdBy"));
                template.setUpdatedAt(record.get("updatedAt"));
                template.setUpdatedBy(record.get("updatedBy"));
                templates.add(template);
            }
            return templates;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read notification template CSV", ex);
        }
    }

    /**
     * Ghi toàn bộ danh sách template hiện tại trở lại file CSV trên S3.
     *
     * @param templates danh sách template cần serialize và upload
     */
    private void writeTemplates(List<NotificationTemplate> templates) {
        try (StringWriter writer = new StringWriter();
                CSVPrinter printer = CSVFormat.DEFAULT.builder()
                        .setHeader(HEADERS)
                        .build()
                        .print(writer)) {
            // Serialize lại toàn bộ template theo schema header cố định để tránh drift format giữa các lần ghi.
            for (NotificationTemplate template : templates) {
                printer.printRecord(
                        template.getTemplateId(),
                        template.getType(),
                        template.getSubject(),
                        template.getTitle(),
                        template.getText(),
                        template.getHtml(),
                        template.getCreatedAt(),
                        template.getCreatedBy(),
                        template.getUpdatedAt(),
                        template.getUpdatedBy());
            }
            printer.flush();
            // Upload lại toàn bộ CSV như một object mới sau khi danh sách trong memory đã được cập nhật.
            s3FileService.uploadByBytes(
                    properties.getBucket(),
                    properties.getKey(),
                    writer.toString().getBytes(properties.getCsvCharset()),
                    "text/csv");
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to write notification template CSV", ex);
        }
    }

    private void requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
