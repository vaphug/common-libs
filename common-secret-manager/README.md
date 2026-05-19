# common-secret-manager

## Tom tat
Lấy/refresh secret từ AWS Secrets Manager và ánh xạ sang snapshot nội bộ.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/SecretRefreshService.java:7:public class SecretRefreshService {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/SecretRefreshService.java:12:    public SecretRefreshService(SecretProvider provider) {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/SecretRefreshService.java:16:    public SecretSnapshot current() {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/SecretRefreshService.java:24:    public SecretSnapshot forceRefresh() {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/SecretRefreshService.java:30:    public boolean refreshIfVersionChanged() {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/AwsSecretsManagerSecretProvider.java:15:public class AwsSecretsManagerSecretProvider implements SecretProvider {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/AwsSecretsManagerSecretProvider.java:20:    public AwsSecretsManagerSecretProvider(SecretManagerProperties properties, ObjectMapper objectMapper) {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/AwsSecretsManagerSecretProvider.java:26:    public SecretSnapshot fetchCurrent() {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/service/SecretProvider.java:5:public interface SecretProvider {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/model/SecretSnapshot.java:6:public record SecretSnapshot(
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/model/ValkeySecret.java:3:public record ValkeySecret(
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/config/SecretManagerProperties.java:14:public class SecretManagerProperties {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/config/CommonSecretManagerAutoConfiguration.java:19:public class CommonSecretManagerAutoConfiguration {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/config/CommonSecretManagerAutoConfiguration.java:23:    public ObjectMapper commonSecretManagerObjectMapper() {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/config/CommonSecretManagerAutoConfiguration.java:29:    public SecretProvider secretProvider(SecretManagerProperties properties, ObjectMapper objectMapper) {
common-secret-manager/src/main/java/com/yourdomain/common/secretmanager/config/CommonSecretManagerAutoConfiguration.java:35:    public SecretRefreshService secretRefreshService(SecretProvider secretProvider) {
```

## 3rd-party API / thu vien lien quan
- AWS Secrets Manager API: https://docs.aws.amazon.com/secretsmanager/latest/apireference/Welcome.html

## Module lien quan
- [common-cache](../common-cache)
- [common-database](../common-database)
