# common-webclient Release Version

## Module

- Name: `common-webclient`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp abstraction web client dùng chung gồm request model, builder, service wrapper, user context helper, header naming, properties và auto-configuration.

## Source Map

- `client/CommonWebClient.java`: facade hoặc entrypoint cho web client abstraction.
- `client/WebClientBuilder.java`: builder hoặc helper khởi tạo web client.
- `config/WebClientAutoConfiguration.java`: wiring bean và auto-configuration của module.
- `config/WebClientProperties.java`: properties runtime cho web client behavior.
- `config/HeaderNames.java`: tên header chuẩn dùng chung.
- `context/UserContextHelper.java`: helper gắn hoặc trích user context cho outbound request.
- `model/WebClientRequest.java`: model request chuẩn hóa của module.
- `service/WebClientService.java`: service wrapper thao tác HTTP dùng chung.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- `src/main/resources/webclientproperties.properties`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Đồng bộ model/config public API theo guide và sửa hạ tầng Lombok cho module.
- Changes:
- Thêm dependency Lombok cho module để khớp với `WebClientProperties`.
- Chuyển `WebClientRequest` sang `@Getter/@Setter`.
- Bổ sung Javadoc cho `WebClientRequest` và `WebClientAutoConfiguration`.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-webclient`.
- Changes:
- Ghi nhận source map và resource chính của module web client.
