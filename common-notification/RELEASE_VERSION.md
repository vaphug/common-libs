# common-notification Release Version

## Module

- Name: `common-notification`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp facade gửi notification đa kênh qua WEBCAS HTTP, Firebase Cloud Messaging, LINE, Twilio SMS, và AWS SES.
- Tích hợp với `common-notification-history` để ghi nhận trạng thái gửi trước và sau khi gọi provider.

## Source Map

- `config/CommonNotificationAutoConfiguration.java`: wiring bean và auto-configuration của module.
- `config/CommonNotificationProperties.java`: cấu hình runtime cho các channel notification.
- `model/NotificationChannel.java`: enum channel được hỗ trợ.
- `model/NotificationSendRequest.java`: request chuẩn hóa cho mọi channel.
- `model/NotificationSendResult.java`: result chuẩn hóa sau khi gọi provider.
- `service/CommonNotificationService.java`: facade gửi notification qua nhiều provider.
- `src/test/java/com/yourdomain/common/notification/service/CommonNotificationServiceTest.java`: test hành vi gửi SMS qua Twilio flow.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa Javadoc/comment theo guide và dọn boilerplate model/config bằng Lombok.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển getter/setter viết tay trong request, result, và properties sang Lombok.
- Bổ sung Javadoc cho public API và nested config type.
- Bổ sung comment workflow ở các đoạn gửi HTTP, apply header, và resolve Google access token.
- Xác nhận lại test module pass sau refactor.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-notification`.
- Changes:
- Ghi nhận các channel đang được hỗ trợ và source map hiện tại của module.
