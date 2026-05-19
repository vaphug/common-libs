# common-database Release Version

## Module

- Name: `common-database`

## Current Release Version

- Version: `0.0.2-SNAPSHOT`
- Last Updated: `2026-05-20`

## Versioning Rule

- Follow [COMMON_RELEASE_VERSION_GUIDE.md](/Users/vaphug/Project/common-libs/COMMON_RELEASE_VERSION_GUIDE.md)

## Module Summary

- Cung cấp nền tảng database dùng chung gồm auto-configuration, rotating datasource, CRUD abstraction, SQL provider, retention, partition maintenance, và metadata support.

## Source Map

- `config/CommonDatabaseAutoConfiguration.java`: entry point auto-configuration của module.
- `config/CommonDatabaseBeansConfiguration.java`: khai báo bean hạ tầng database dùng chung.
- `config/DatabaseProperties.java`: cấu hình runtime cho datasource và các behavior liên quan.
- `config/RotatingDataSource.java`: datasource hỗ trợ rotation credential hoặc endpoint.
- `config/DatabaseRotationScheduler.java`: scheduler phục vụ rotation database secret/config.
- `config/DatabaseSecret.java`, `config/DatabaseSecretMapper.java`: mô hình và mapper secret database.
- `service/CommonDatabaseService.java`: service facade thao tác database dùng chung.
- `service/DataRetentionService.java`: xử lý retention dữ liệu.
- `service/PartitionMaintenanceService.java`: bảo trì partition dữ liệu.
- `repository/CommonCrudRepository.java`, `repository/MyBatisCommonCrudRepository.java`: abstraction repository CRUD dùng chung.
- `sql/CommonSqlProvider.java`: build SQL động dùng chung.
- `mapper/CommonEntityMapper.java`: mapper entity phục vụ thao tác DB.
- `domain/*`: entity base, search criteria, write command, lock mode.
- `context/TableMetadata.java`: metadata cho table handling.
- `util/SqlIdentifierValidator.java`: validate identifier để giảm rủi ro SQL injection từ dynamic SQL.

## Resources and Configuration

- `src/main/resources/application-common-database.yml`
- `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- `src/main/resources/db/migration/V1__create_orders.sql`
- `src/main/resources/db/migration/V2__upgrade_orders_for_common_database.sql`
- `src/main/resources/db/migration/V3__create_orders_common_demo.sql`

## Release History

## 0.0.2-SNAPSHOT - 2026-05-20

- Type: `refactor`
- Summary: Đồng bộ style cho properties/domain bề mặt theo guide chung.
- Changes:
- Thêm dependency Lombok cho module.
- Chuyển `DatabaseProperties` và `BaseEntity` sang Lombok để giảm boilerplate.
- Bổ sung field comment và Javadoc cho public helper method `jdbcUrl()`.

## 0.0.1-SNAPSHOT - 2026-05-20

- Type: `init`
- Summary: Khởi tạo release note baseline cho module `common-database`.
- Changes:
- Ghi nhận thành phần source chính, migration script, và phạm vi nghiệp vụ hiện tại.
