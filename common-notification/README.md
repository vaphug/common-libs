# common-notification

## Tom tat
Facade gửi notification đa kênh (WEBCAS mail, FCM push, LINE, Twilio SMS, AWS SES) và ghi lịch sử gửi.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:47:public class CommonNotificationService {
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:55:    public CommonNotificationService(
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:76:    public NotificationSendResult sendWebCasMail(NotificationSendRequest request) {
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:87:    public NotificationSendResult sendPush(NotificationSendRequest request) {
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:98:    public NotificationSendResult sendLINE(NotificationSendRequest request) {
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:109:    public NotificationSendResult sendSMS(NotificationSendRequest request) {
common-notification/src/main/java/com/yourdomain/common/notification/service/CommonNotificationService.java:120:    public NotificationSendResult sendAWSMail(NotificationSendRequest request) {
common-notification/src/main/java/com/yourdomain/common/notification/model/NotificationChannel.java:6:public enum NotificationChannel {
common-notification/src/main/java/com/yourdomain/common/notification/model/NotificationSendResult.java:11:public class NotificationSendResult {
common-notification/src/main/java/com/yourdomain/common/notification/model/NotificationSendRequest.java:16:public class NotificationSendRequest {
common-notification/src/main/java/com/yourdomain/common/notification/config/CommonNotificationAutoConfiguration.java:28:public class CommonNotificationAutoConfiguration {
common-notification/src/main/java/com/yourdomain/common/notification/config/CommonNotificationAutoConfiguration.java:37:    public WebClient.Builder notificationWebClientBuilder() {
common-notification/src/main/java/com/yourdomain/common/notification/config/CommonNotificationAutoConfiguration.java:50:    public SesClient sesClient(CommonNotificationProperties properties) {
common-notification/src/main/java/com/yourdomain/common/notification/config/CommonNotificationAutoConfiguration.java:66:    public ObjectMapper notificationObjectMapper() {
common-notification/src/main/java/com/yourdomain/common/notification/config/CommonNotificationAutoConfiguration.java:83:    public CommonNotificationService commonNotificationService(
common-notification/src/main/java/com/yourdomain/common/notification/config/CommonNotificationProperties.java:18:public class CommonNotificationProperties {
```

## 3rd-party API / thu vien lien quan
- Firebase Cloud Messaging HTTP v1 (POST /v1/projects/{projectId}/messages:send): dùng cho sendPush
  Doc: https://firebase.google.com/docs/cloud-messaging/send-message
- LINE Messaging API Push (POST /v2/bot/message/push): dùng cho sendLINE
  Doc: https://developers.line.biz/en/reference/messaging-api/#send-push-message
- Twilio Messages API (POST /2010-04-01/Accounts/{AccountSid}/Messages.json): dùng cho sendSMS
  Doc: https://www.twilio.com/docs/messaging/api/message-resource
- AWS SES SendEmail: dùng cho sendAWSMail
  Doc: https://docs.aws.amazon.com/ses/latest/APIReference/API_SendEmail.html
- HTTP provider tùy biến (ví dụ WEBCAS): dùng cho sendWebCasMail qua sendHttp

## Module lien quan
- [common-notification-history](../common-notification-history)
