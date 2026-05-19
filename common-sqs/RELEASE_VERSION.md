# common-sqs Release Version

## Module

- Name: `common-sqs`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp service gửi/nhận/xử lý SQS message với auto-configuration, options xử lý, idempotency store, và callback handler cho business layer.

## Source Map

- `config/CommonSqsAutoConfiguration.java`: wiring bean và auto-configuration cho SQS module.
- `config/CommonSqsProperties.java`: cấu hình runtime cho SQS client và behavior module.
- `model/ProcessMessageOptions.java`: options điều khiển polling và xử lý message.
- `service/CommonSqsService.java`: facade gửi nhận và xử lý message SQS.
- `service/SqsMessageHandler.java`: callback contract cho business handler.
- `idempotency/MessageIdempotencyStore.java`: contract idempotency cho xử lý message.
- `idempotency/InMemoryMessageIdempotencyStore.java`: implementation in-memory cho idempotency store.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- `src/main/resources/i18n/sqs-messages.properties`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa model/config style và giảm boilerplate theo guide.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `CommonSqsProperties` và `ProcessMessageOptions` sang `@Getter/@Setter`.
- Giữ nguyên contract xử lý message, chỉ dọn style và Javadoc/comment bề mặt.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-sqs`.
- Changes:
- Ghi nhận service chính, model xử lý message, và cơ chế idempotency hiện có.
