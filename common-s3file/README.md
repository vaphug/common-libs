# common-s3file

## Tom tat
Đóng gói thao tác upload/download/delete/head object trên AWS S3.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:30:public class CommonS3FileService {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:36:    public CommonS3FileService(S3Client s3Client, S3Presigner s3Presigner, CommonS3FileProperties properties) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:50:    public void putObject(String bucket, String key, String content, String contentType) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:62:    public String getObjectAsString(String bucket, String key) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:79:    public byte[] getObject(String bucket, String key) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:97:    public void uploadByBytes(String bucket, String key, byte[] bytes, String contentType) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:114:    public void uploadFile(String bucket, String key, Path file) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:131:    public void downloadFile(String bucket, String key, Path destination) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:150:    public URL generateUrl(String bucket, String key) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:164:    public URL presignedUrl(String bucket, String key, Duration ttl) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:183:    public void deleteObject(String bucket, String key) {
common-s3file/src/main/java/com/yourdomain/common/s3file/service/CommonS3FileService.java:197:    public boolean objectExists(String bucket, String key) {
common-s3file/src/main/java/com/yourdomain/common/s3file/config/CommonS3FileAutoConfiguration.java:26:public class CommonS3FileAutoConfiguration {
common-s3file/src/main/java/com/yourdomain/common/s3file/config/CommonS3FileAutoConfiguration.java:36:    public S3Client s3Client(CommonS3FileProperties properties) {
common-s3file/src/main/java/com/yourdomain/common/s3file/config/CommonS3FileAutoConfiguration.java:52:    public S3Presigner s3Presigner(CommonS3FileProperties properties) {
common-s3file/src/main/java/com/yourdomain/common/s3file/config/CommonS3FileAutoConfiguration.java:70:    public CommonS3FileService commonS3FileService(
common-s3file/src/main/java/com/yourdomain/common/s3file/config/CommonS3FileProperties.java:14:public class CommonS3FileProperties {
```

## 3rd-party API / thu vien lien quan
- AWS S3 API: https://docs.aws.amazon.com/AmazonS3/latest/API/Welcome.html

## Module lien quan
- [common-notification-template](../common-notification-template)
