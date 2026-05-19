# common-cache Release Version

## Module

- Name: `common-cache`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp cache service dùng chung với cơ chế xoay secret/connection cho Valkey.
- Tập trung vào auto-configuration, mapping secret, quản lý template, và scheduler phục vụ cache rotation.

## Source Map

- `config/CommonCacheAutoConfiguration.java`: khởi tạo bean và wiring module cache.
- `config/CommonCacheProperties.java`: cấu hình runtime cho module cache.
- `service/RotatingValkeyCacheService.java`: facade/service thao tác cache với backend Valkey.
- `service/ValkeyTemplateManager.java`: quản lý template/client kết nối Valkey.
- `service/ValkeySecretMapper.java`: map secret sang cấu hình Valkey runtime.
- `service/CacheRotationScheduler.java`: scheduler refresh hoặc rotate cache connection.

## Resources and Configuration

- `src/main/resources/application-common-cache.yml`
- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa style config class theo guide và thêm Lombok support cho module.
- Changes:
- Thêm dependency Lombok để giảm boilerplate getter/setter.
- Chuyển `CommonCacheProperties` sang `@Getter/@Setter`.
- Bổ sung Javadoc và field comment cho properties công khai.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-cache`.
- Changes:
- Ghi nhận source map, resource chính, và phạm vi chức năng hiện tại của module.
