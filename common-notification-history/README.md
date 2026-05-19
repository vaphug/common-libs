# common-notification-history

## Tom tat
Lưu và cập nhật lịch sử gửi notification (REQUESTED/SUCCESS/FAILED).

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-notification-history/src/main/java/com/yourdomain/common/notification/history/service/CommonNotificationHistoryService.java:19:public class CommonNotificationHistoryService {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/service/CommonNotificationHistoryService.java:24:    public CommonNotificationHistoryService(NotificationHistoryRepository repository, ObjectMapper objectMapper) {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/service/CommonNotificationHistoryService.java:38:    public String recordRequested(String channel, String recipient, String templateId, Object requestPayload) {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/service/CommonNotificationHistoryService.java:58:    public void markSuccess(String id, Object responsePayload, String providerMessageId) {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/service/CommonNotificationHistoryService.java:69:    public void markFailed(String id, Object responsePayload, String errorMessage) {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/model/NotificationHistoryStatus.java:6:public enum NotificationHistoryStatus {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/model/NotificationHistoryRecord.java:12:public class NotificationHistoryRecord {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/config/CommonNotificationHistoryProperties.java:13:public class CommonNotificationHistoryProperties {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/repository/JdbcNotificationHistoryRepository.java:14:public class JdbcNotificationHistoryRepository implements NotificationHistoryRepository {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/repository/JdbcNotificationHistoryRepository.java:19:    public JdbcNotificationHistoryRepository(
common-notification-history/src/main/java/com/yourdomain/common/notification/history/repository/JdbcNotificationHistoryRepository.java:33:    public void insert(NotificationHistoryRecord record) {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/repository/JdbcNotificationHistoryRepository.java:62:    public void updateStatus(
common-notification-history/src/main/java/com/yourdomain/common/notification/history/config/CommonNotificationHistoryAutoConfiguration.java:25:public class CommonNotificationHistoryAutoConfiguration {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/config/CommonNotificationHistoryAutoConfiguration.java:36:    public JdbcTemplate notificationHistoryJdbcTemplate(DataSource dataSource) {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/config/CommonNotificationHistoryAutoConfiguration.java:50:    public NotificationHistoryRepository notificationHistoryRepository(
common-notification-history/src/main/java/com/yourdomain/common/notification/history/config/CommonNotificationHistoryAutoConfiguration.java:64:    public ObjectMapper notificationHistoryObjectMapper() {
common-notification-history/src/main/java/com/yourdomain/common/notification/history/config/CommonNotificationHistoryAutoConfiguration.java:78:    public CommonNotificationHistoryService commonNotificationHistoryService(
common-notification-history/src/main/java/com/yourdomain/common/notification/history/repository/NotificationHistoryRepository.java:9:public interface NotificationHistoryRepository {
```

## 3rd-party API / thu vien lien quan
- Spring JDBC: https://docs.spring.io/spring-framework/reference/data-access/jdbc.html

## Module lien quan
- [common-notification](../common-notification)
