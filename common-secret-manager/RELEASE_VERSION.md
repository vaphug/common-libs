# common-secret-manager Release Version

## Module

- Name: `common-secret-manager`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp secret provider abstraction, AWS Secrets Manager implementation, snapshot model, refresh service, và auto-configuration cho secret management dùng chung.

## Source Map

- `config/CommonSecretManagerAutoConfiguration.java`: wiring bean và auto-configuration secret manager.
- `config/SecretManagerProperties.java`: cấu hình runtime cho secret provider và refresh behavior.
- `model/SecretSnapshot.java`: snapshot dữ liệu secret đang hoạt động.
- `model/ValkeySecret.java`: model secret chuyên biệt cho Valkey.
- `service/SecretProvider.java`: contract cung cấp secret.
- `service/AwsSecretsManagerSecretProvider.java`: implementation đọc secret từ AWS Secrets Manager.
- `service/SecretRefreshService.java`: service refresh secret theo chu kỳ hoặc trigger.

## Resources and Configuration

- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Đồng bộ style config public API theo guide của repo.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `SecretManagerProperties` sang `@Getter/@Setter`.
- Bổ sung Javadoc và field comment cho cấu hình runtime.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-secret-manager`.
- Changes:
- Ghi nhận source map chính và contract refresh secret hiện tại.
