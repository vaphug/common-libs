# common-notification-template Release Version

## Module

- Name: `common-notification-template`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp quản lý template notification gồm model tạo/cập nhật template, properties và service thao tác template dùng chung.

## Source Map

- `config/CommonNotificationTemplateAutoConfiguration.java`: wiring bean và auto-configuration.
- `config/CommonNotificationTemplateProperties.java`: cấu hình runtime của module template.
- `model/NotificationTemplate.java`: model template notification.
- `model/CreateTemplateRequest.java`: request tạo template mới.
- `model/UpdateTemplateRequest.java`: request cập nhật template hiện có.
- `service/CommonNotificationTemplateService.java`: service thao tác template notification.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Đồng bộ style cho properties và template model theo guide chung.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `CommonNotificationTemplateProperties`, `CreateTemplateRequest`, `UpdateTemplateRequest`, và `NotificationTemplate` sang `@Getter/@Setter`.
- Giữ nguyên logic CRUD template trên CSV/S3, chỉ dọn boilerplate và comment/Javadoc.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-notification-template`.
- Changes:
- Ghi nhận source map và config entrypoint hiện có.
