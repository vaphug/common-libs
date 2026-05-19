# common-validation Release Version

## Module

- Name: `common-validation`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp annotation validation dùng chung, validator implementation, domain validation repository, NG word service, và metadata hỗ trợ validation nghiệp vụ.

## Source Map

- `annotation/*`: tập annotation validation như `NotBlank`, `Regex`, `Range`, `Min`, `Max`, `ValidPhoneNumber`, `HalfWidth`, `FullWidth`, `ItemValidate`.
- `constraint/*`: validator implementation cho các annotation validation.
- `config/DomainValidationDefinition.java`, `config/DomainValidationRepository.java`: dữ liệu và repository cấu hình validation theo domain.
- `ngword/*`: normalize, check service, repository và result cho NG word validation.
- `validator/*`: helper validator, matcher, enum và metadata validation.

## Resources and Configuration

- `src/main/resources/validation/DomainDefinition.json`
- `src/main/resources/validation/DomainValidationData.json`
- `NG_WORD_COMMON_SPEC.md`
- `VALIDATION_MATRIX.md`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Chuẩn hóa model cấu hình validation theo guide và giảm boilerplate accessor.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `DomainValidationDefinition` sang `@Getter` cho immutable config object.
- Bổ sung Javadoc mô tả vai trò của rule definition trong runtime validation.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-validation`.
- Changes:
- Ghi nhận toàn bộ nhóm annotation, validator, domain validation, và NG word support hiện có.
