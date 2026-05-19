# common-message-core Release Version

## Module

- Name: `common-message-core`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp core message service và auto-configuration tối giản cho các module/message consumer khác dùng lại.

## Source Map

- `MessageService.java`: service lõi xử lý message dùng chung.
- `config/MessageCoreAutoConfig.java`: auto-configuration của module message core.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa style nested properties theo guide và giảm boilerplate.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `MessageCoreProperties` sang `@Getter/@Setter`.
- Bổ sung Javadoc cho helper chuyển đổi locale mặc định.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-message-core`.
- Changes:
- Ghi nhận cấu trúc source hiện tại và resource auto-configuration.
