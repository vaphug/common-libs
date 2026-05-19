# common-database

## Tom tat
CRUD dùng MyBatis + JDBC, hỗ trợ soft delete, search, retention và xoay DB secret.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:11:public class CommonSqlProvider {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:13:    public String insert(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:28:    public String update(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:57:    public String findById(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:79:    public String softDelete(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:91:    public String hardDelete(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:97:    public String restore(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:109:    public String search(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/sql/CommonSqlProvider.java:130:    public String cleanupDeletedBefore(Map<String, Object> params) {
common-database/src/main/java/com/yourdomain/common/database/domain/LockMode.java:6:public enum LockMode {
common-database/src/main/java/com/yourdomain/common/database/domain/WriteCommand.java:9:public record WriteCommand(
common-database/src/main/java/com/yourdomain/common/database/domain/SearchCriteria.java:9:public record SearchCriteria(
common-database/src/main/java/com/yourdomain/common/database/service/PartitionMaintenanceService.java:16:public class PartitionMaintenanceService {
common-database/src/main/java/com/yourdomain/common/database/service/PartitionMaintenanceService.java:24:    public PartitionMaintenanceService(DataSource dataSource) {
common-database/src/main/java/com/yourdomain/common/database/service/PartitionMaintenanceService.java:34:    public void ensureRangePartition(TableMetadata metadata, long partitionIndex) {
common-database/src/main/java/com/yourdomain/common/database/context/TableMetadata.java:15:public record TableMetadata(
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseSecretMapper.java:6:public class DatabaseSecretMapper {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseSecretMapper.java:8:    public DatabaseSecret map(SecretSnapshot snapshot) {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseSecretMapper.java:12:    public DatabaseSecret map(Map<String, Object> payload) {
common-database/src/main/java/com/yourdomain/common/database/repository/CommonCrudRepository.java:13:public interface CommonCrudRepository {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:15:public class CommonDatabaseService {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:19:    public CommonDatabaseService(CommonCrudRepository repository) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:26:    public int insert(WriteCommand command) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:33:    public int updateById(Object id, WriteCommand command) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:40:    public Optional<Map<String, Object>> findById(Object id, LockMode lockMode, boolean includeDeleted) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:47:    public Optional<Map<String, Object>> findByIdForShare(Object id, boolean includeDeleted) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:54:    public Optional<Map<String, Object>> findByIdForUpdate(Object id, boolean includeDeleted) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:61:    public int softDelete(Object id) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:65:    public int softDelete(Object id, String actor) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:72:    public int hardDelete(Object id) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:79:    public int restore(Object id) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:83:    public int restore(Object id, String actor) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:90:    public List<Map<String, Object>> search(SearchCriteria criteria) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:97:    public List<Map<String, Object>> findAll(int offset, int limit, boolean includeDeleted) {
common-database/src/main/java/com/yourdomain/common/database/service/CommonDatabaseService.java:104:    public int cleanupBefore(LocalDateTime cutoff) {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseSecret.java:3:public record DatabaseSecret(
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseSecret.java:11:    public String jdbcUrl() {
common-database/src/main/java/com/yourdomain/common/database/service/DataRetentionService.java:11:public class DataRetentionService {
common-database/src/main/java/com/yourdomain/common/database/service/DataRetentionService.java:18:    public DataRetentionService(CommonDatabaseService databaseService, DatabaseProperties properties) {
common-database/src/main/java/com/yourdomain/common/database/service/DataRetentionService.java:26:    public int purgeExpiredRecycleBinData() {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:12:public class RotatingDataSource implements DataSource {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:18:    public RotatingDataSource(SecretRefreshService secretRefreshService, DatabaseSecretMapper secretMapper) {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:24:    public synchronized boolean refreshIfRotated() {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:32:    public synchronized boolean refreshFromLatestSecret() {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:49:    public Connection getConnection() throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:60:    public Connection getConnection(String username, String password) throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:71:    public <T> T unwrap(Class<T> iface) throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:76:    public boolean isWrapperFor(Class<?> iface) throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:81:    public java.io.PrintWriter getLogWriter() throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:86:    public void setLogWriter(java.io.PrintWriter out) throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:91:    public void setLoginTimeout(int seconds) throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:96:    public int getLoginTimeout() throws SQLException {
common-database/src/main/java/com/yourdomain/common/database/config/RotatingDataSource.java:101:    public java.util.logging.Logger getParentLogger() {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:22:public class MyBatisCommonCrudRepository implements CommonCrudRepository {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:33:    public MyBatisCommonCrudRepository(CommonEntityMapper mapper, TableMetadata metadata) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:37:    public MyBatisCommonCrudRepository(CommonEntityMapper mapper, TableMetadata metadata, Clock clock) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:44:    public int insert(WriteCommand command) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:60:    public int updateById(Object id, WriteCommand command) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:73:    public Optional<Map<String, Object>> findById(Object id, LockMode lockMode, boolean includeDeleted) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:91:    public int softDeleteById(Object id) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:96:    public int softDeleteById(Object id, String actor) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:106:    public int hardDeleteById(Object id) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:114:    public int restoreById(Object id) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:119:    public int restoreById(Object id, String actor) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:129:    public List<Map<String, Object>> search(SearchCriteria criteria) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:143:    public List<Map<String, Object>> findAll(int offset, int limit, boolean includeDeleted) {
common-database/src/main/java/com/yourdomain/common/database/repository/MyBatisCommonCrudRepository.java:156:    public int cleanupRecycleBinBefore(Object cutoffDateTime) {
common-database/src/main/java/com/yourdomain/common/database/mapper/CommonEntityMapper.java:14:public interface CommonEntityMapper {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:19:public class CommonDatabaseBeansConfiguration {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:25:    public TableMetadata defaultTableMetadata() {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:39:    public CommonCrudRepository commonCrudRepository(CommonEntityMapper mapper, TableMetadata metadata, Clock clock) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:44:    public Clock systemUtcClock() {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:49:    public CommonDatabaseService commonDatabaseService(CommonCrudRepository repository) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:54:    public PartitionMaintenanceService partitionMaintenanceService(DataSource dataSource) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseBeansConfiguration.java:59:    public DataRetentionService dataRetentionService(CommonDatabaseService service, DatabaseProperties properties) {
common-database/src/main/java/com/yourdomain/common/database/util/SqlIdentifierValidator.java:21:    public static String safeIdentifier(String raw) {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseRotationScheduler.java:3:public class DatabaseRotationScheduler {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseRotationScheduler.java:7:    public DatabaseRotationScheduler(RotatingDataSource rotatingDataSource) {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseRotationScheduler.java:11:    public void refresh() {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:23:public class CommonDatabaseAutoConfiguration {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:29:    public DatabaseSecretMapper databaseSecretMapper() {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:36:    public DataSource rotatingDataSource(SecretRefreshService secretRefreshService,
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:47:    public DataSource commonDataSource(DatabaseProperties properties) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:54:    public DatabaseRotationScheduler databaseRotationScheduler(RotatingDataSource rotatingDataSource) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:61:    public Runnable commonDatabaseRotationJob(DatabaseRotationScheduler scheduler, DatabaseProperties properties) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:65:            public void run() {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:73:    public DatabaseProperties commonDatabasePropertiesBean(DatabaseProperties properties) {
common-database/src/main/java/com/yourdomain/common/database/config/CommonDatabaseAutoConfiguration.java:79:    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseProperties.java:14:public class DatabaseProperties {
common-database/src/main/java/com/yourdomain/common/database/config/DatabaseProperties.java:40:    public String jdbcUrl() {
```

## 3rd-party API / thu vien lien quan
- MyBatis Spring Boot Starter: https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/
- MyBatis Dynamic SQL: https://mybatis.org/mybatis-dynamic-sql/docs/introduction.html
- Spring JDBC: https://docs.spring.io/spring-framework/reference/data-access/jdbc.html

## Module lien quan
- [common-secret-manager](../common-secret-manager)
