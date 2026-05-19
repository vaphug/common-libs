# common-s3file Release Version

## Module

- Name: `common-s3file`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp service thao tác file trên S3 cùng auto-configuration và properties cho upload/download/file access.

## Source Map

- `config/CommonS3FileAutoConfiguration.java`: wiring bean và auto-configuration S3 file module.
- `config/CommonS3FileProperties.java`: cấu hình runtime cho S3 file access.
- `service/CommonS3FileService.java`: service thao tác upload, download, head, delete hoặc URL handling với S3.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa config style theo guide và giảm boilerplate accessor.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `CommonS3FileProperties` sang `@Getter/@Setter`.
- Giữ nguyên behavior runtime, chỉ dọn style và comment/Javadoc.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-s3file`.
- Changes:
- Ghi nhận phạm vi source chính và resource auto-configuration.
