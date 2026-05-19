# common-cache

## Tom tat
Quản lý cache Valkey/Redis với cơ chế xoay secret và refresh template runtime.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeySecretMapper.java:7:public class ValkeySecretMapper {
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeySecretMapper.java:9:    public ValkeySecret map(SecretSnapshot snapshot) {
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeySecretMapper.java:13:    public ValkeySecret map(Map<String, Object> payload) {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheProperties.java:14:public class CommonCacheProperties {
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeyTemplateManager.java:15:public class ValkeyTemplateManager {
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeyTemplateManager.java:20:    public ValkeyTemplateManager(ValkeySecretMapper valkeySecretMapper) {
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeyTemplateManager.java:24:    public RedisTemplate<String, String> currentTemplate() {
common-cache/src/main/java/com/yourdomain/common/cache/service/ValkeyTemplateManager.java:32:    public synchronized boolean applyIfChanged(SecretSnapshot snapshot) {
common-cache/src/main/java/com/yourdomain/common/cache/service/CacheRotationScheduler.java:3:public class CacheRotationScheduler {
common-cache/src/main/java/com/yourdomain/common/cache/service/CacheRotationScheduler.java:7:    public CacheRotationScheduler(RotatingValkeyCacheService cacheService) {
common-cache/src/main/java/com/yourdomain/common/cache/service/CacheRotationScheduler.java:11:    public void refresh() {
common-cache/src/main/java/com/yourdomain/common/cache/service/RotatingValkeyCacheService.java:7:public class RotatingValkeyCacheService {
common-cache/src/main/java/com/yourdomain/common/cache/service/RotatingValkeyCacheService.java:13:    public RotatingValkeyCacheService(
common-cache/src/main/java/com/yourdomain/common/cache/service/RotatingValkeyCacheService.java:23:    public String get(String key) {
common-cache/src/main/java/com/yourdomain/common/cache/service/RotatingValkeyCacheService.java:28:    public void set(String key, String value, Duration ttl) {
common-cache/src/main/java/com/yourdomain/common/cache/service/RotatingValkeyCacheService.java:41:    public void refreshFromLatestSecret() {
common-cache/src/main/java/com/yourdomain/common/cache/service/RotatingValkeyCacheService.java:46:    public void refreshIfRotated() {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:22:public class CommonCacheAutoConfiguration {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:26:    public ValkeySecretMapper valkeySecretMapper() {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:32:    public ValkeyTemplateManager valkeyTemplateManager(ValkeySecretMapper mapper) {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:38:    public RotatingValkeyCacheService rotatingValkeyCacheService(
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:55:    public CacheRotationScheduler cacheRotationScheduler(RotatingValkeyCacheService cacheService) {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:61:    public Runnable commonCacheRotationJob(CacheRotationScheduler scheduler, CommonCacheProperties properties) {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:67:            public void run() {
common-cache/src/main/java/com/yourdomain/common/cache/config/CommonCacheAutoConfiguration.java:75:    public CommonCacheProperties commonCachePropertiesBean(CommonCacheProperties properties) {
```

## 3rd-party API / thu vien lien quan
- Spring Data Redis: https://docs.spring.io/spring-data/redis/reference/
- Lettuce Redis client: https://lettuce.io/core/release/reference/

## Module lien quan
- [common-secret-manager](../common-secret-manager)
