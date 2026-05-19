# common-notification-template

## Tom tat
CRUD template notification dạng CSV trên S3 theo mô hình read-modify-write.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-notification-template/src/main/java/com/yourdomain/common/notification/template/service/CommonNotificationTemplateService.java:30:public class CommonNotificationTemplateService {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/service/CommonNotificationTemplateService.java:40:    public CommonNotificationTemplateService(
common-notification-template/src/main/java/com/yourdomain/common/notification/template/service/CommonNotificationTemplateService.java:54:    public NotificationTemplate createTemplate(CreateTemplateRequest request) {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/service/CommonNotificationTemplateService.java:86:    public NotificationTemplate updateTemplate(String templateId, UpdateTemplateRequest request) {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/service/CommonNotificationTemplateService.java:121:    public Optional<NotificationTemplate> getTemplate(String templateId) {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/service/CommonNotificationTemplateService.java:133:    public void deleteTemplate(String templateId) {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/model/NotificationTemplate.java:11:public class NotificationTemplate {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/model/UpdateTemplateRequest.java:11:public class UpdateTemplateRequest {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/model/CreateTemplateRequest.java:11:public class CreateTemplateRequest {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/config/CommonNotificationTemplateProperties.java:15:public class CommonNotificationTemplateProperties {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/config/CommonNotificationTemplateAutoConfiguration.java:18:public class CommonNotificationTemplateAutoConfiguration {
common-notification-template/src/main/java/com/yourdomain/common/notification/template/config/CommonNotificationTemplateAutoConfiguration.java:30:    public CommonNotificationTemplateService commonNotificationTemplateService(
```

## 3rd-party API / thu vien lien quan
- AWS S3 API (gián tiếp qua common-s3file): https://docs.aws.amazon.com/AmazonS3/latest/API/Welcome.html
- Apache Commons CSV: https://commons.apache.org/proper/commons-csv/user-guide.html

## Module lien quan
- [common-s3file](../common-s3file)
