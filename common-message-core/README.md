# common-message-core

## Tom tat
Cung cấp service lấy message đa ngôn ngữ (i18n) từ MessageSource.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-message-core/src/main/java/com/yourdomain/common/core/MessageService.java:16:public class MessageService {
common-message-core/src/main/java/com/yourdomain/common/core/MessageService.java:20:    public MessageService(MessageSource messageSource) {
common-message-core/src/main/java/com/yourdomain/common/core/MessageService.java:30:    public String getMessage(String messageId) {
common-message-core/src/main/java/com/yourdomain/common/core/MessageService.java:41:    public String getMessage(String messageId, Locale locale) {
common-message-core/src/main/java/com/yourdomain/common/core/MessageService.java:53:    public String getMessage(String messageId, Object[] args, Locale locale) {
common-message-core/src/main/java/com/yourdomain/common/core/config/MessageCoreAutoConfig.java:49:public class MessageCoreAutoConfig {
common-message-core/src/main/java/com/yourdomain/common/core/config/MessageCoreAutoConfig.java:67:    public MessageSource messageSource(MessageCoreProperties properties) {
common-message-core/src/main/java/com/yourdomain/common/core/config/MessageCoreAutoConfig.java:88:    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
common-message-core/src/main/java/com/yourdomain/common/core/config/MessageCoreAutoConfig.java:157:        public Locale toDefaultLocale() {
```

## 3rd-party API / thu vien lien quan
- Spring MessageSource: https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-messages

## Module lien quan
- [common-messages](../common-messages)
