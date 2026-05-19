# common-notification-history Release Version

## Module

- Name: `common-notification-history`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp lưu trữ lịch sử gửi notification gồm auto-configuration, record model, repository abstraction, JDBC repository, và service cập nhật trạng thái gửi.

## Source Map

- `config/CommonNotificationHistoryAutoConfiguration.java`: wiring bean và auto-configuration.
- `config/CommonNotificationHistoryProperties.java`: cấu hình runtime cho history module.
- `model/NotificationHistoryRecord.java`: record dữ liệu lịch sử notification.
- `model/NotificationHistoryStatus.java`: trạng thái lifecycle của notification history.
- `repository/NotificationHistoryRepository.java`: contract repository history.
- `repository/JdbcNotificationHistoryRepository.java`: JDBC implementation cho history persistence.
- `service/CommonNotificationHistoryService.java`: service ghi requested/success/failed cho notification.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Đồng bộ DTO/config style theo guide và thêm Lombok support.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `CommonNotificationHistoryProperties` và `NotificationHistoryRecord` sang `@Getter/@Setter`.
- Giữ nguyên contract lưu history và chỉ chuẩn hóa comment/Javadoc.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-notification-history`.
- Changes:
- Ghi nhận thành phần source chính và contract lưu history notification.
