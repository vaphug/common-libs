package com.yourdomain.common.s3file.service;

import com.yourdomain.common.s3file.config.CommonS3FileProperties;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * Service facade cho các thao tác file dùng chung trên AWS S3.
 *
 * <p>Class này bọc lại các use case thường gặp như upload text, upload bytes,
 * upload file, download file, sinh URL trực tiếp, sinh presigned URL, và kiểm tra
 * object tồn tại để caller không phải làm việc trực tiếp với AWS SDK ở từng nơi.
 */
public class CommonS3FileService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final CommonS3FileProperties properties;

    public CommonS3FileService(S3Client s3Client, S3Presigner s3Presigner, CommonS3FileProperties properties) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.properties = properties;
    }

    /**
     * Upload chuỗi text lên S3 bằng UTF-8.
     *
     * @param bucket tên bucket đích; nếu rỗng sẽ dùng bucket mặc định trong config
     * @param key object key đích trong bucket
     * @param content nội dung text cần ghi; nếu {@code null} sẽ upload file rỗng
     * @param contentType MIME type gắn vào object, ví dụ {@code text/csv}
     */
    public void putObject(String bucket, String key, String content, String contentType) {
        byte[] bytes = content == null ? new byte[0] : content.getBytes(StandardCharsets.UTF_8);
        uploadByBytes(bucket, key, bytes, contentType);
    }

    /**
     * Tải object từ S3 và trả về dưới dạng chuỗi UTF-8.
     *
     * @param bucket tên bucket nguồn; nếu rỗng sẽ dùng bucket mặc định trong config
     * @param key object key cần đọc
     * @return nội dung object đã decode bằng UTF-8
     */
    public String getObjectAsString(String bucket, String key) {
        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(resolveBucket(bucket))
                        .key(requireText(key, "key"))
                        .build(),
                ResponseTransformer.toBytes());
        return responseBytes.asString(StandardCharsets.UTF_8);
    }

    /**
     * Tải object từ S3 và trả về raw bytes.
     *
     * @param bucket tên bucket nguồn; nếu rỗng sẽ dùng bucket mặc định trong config
     * @param key object key cần đọc
     * @return toàn bộ bytes của object
     */
    public byte[] getObject(String bucket, String key) {
        return s3Client.getObject(
                        GetObjectRequest.builder()
                                .bucket(resolveBucket(bucket))
                                .key(requireText(key, "key"))
                                .build(),
                        ResponseTransformer.toBytes())
                .asByteArray();
    }

    /**
     * Upload mảng bytes lên S3.
     *
     * @param bucket tên bucket đích; nếu rỗng sẽ dùng bucket mặc định trong config
     * @param key object key đích trong bucket
     * @param bytes dữ liệu cần upload; nếu {@code null} sẽ upload file rỗng
     * @param contentType MIME type gắn vào object, có thể {@code null}
     */
    public void uploadByBytes(String bucket, String key, byte[] bytes, String contentType) {
        PutObjectRequest.Builder builder = PutObjectRequest.builder()
                .bucket(resolveBucket(bucket))
                .key(requireText(key, "key"));
        if (contentType != null && !contentType.isBlank()) {
            builder.contentType(contentType);
        }
        s3Client.putObject(builder.build(), RequestBody.fromBytes(bytes == null ? new byte[0] : bytes));
    }

    /**
     * Upload file local lên S3 bằng streaming từ filesystem.
     *
     * @param bucket tên bucket đích; nếu rỗng sẽ dùng bucket mặc định trong config
     * @param key object key đích trong bucket
     * @param file đường dẫn file local cần upload
     */
    public void uploadFile(String bucket, String key, Path file) {
        Objects.requireNonNull(file, "file must not be null");
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(resolveBucket(bucket))
                        .key(requireText(key, "key"))
                        .build(),
                RequestBody.fromFile(file));
    }

    /**
     * Tải object từ S3 xuống file local.
     *
     * @param bucket tên bucket nguồn; nếu rỗng sẽ dùng bucket mặc định trong config
     * @param key object key cần tải
     * @param destination file đích trên local filesystem
     */
    public void downloadFile(String bucket, String key, Path destination) {
        Objects.requireNonNull(destination, "destination must not be null");
        s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(resolveBucket(bucket))
                        .key(requireText(key, "key"))
                        .build(),
                ResponseTransformer.toFile(destination));
    }

    /**
     * Sinh URL truy cập trực tiếp đến object.
     *
     * <p>URL trả về không tự thêm chữ ký. Object phải public hoặc caller phải có quyền phù hợp.
     *
     * @param bucket tên bucket chứa object
     * @param key object key cần tạo URL
     * @return URL trực tiếp trỏ đến object
     */
    public URL generateUrl(String bucket, String key) {
        return s3Client.utilities().getUrl(builder -> builder
                .bucket(resolveBucket(bucket))
                .key(requireText(key, "key")));
    }

    /**
     * Sinh presigned URL GET object với TTL tùy chọn.
     *
     * @param bucket tên bucket chứa object
     * @param key object key cần ký URL
     * @param ttl thời gian hiệu lực của URL; nếu {@code null} sẽ dùng TTL mặc định trong config
     * @return presigned URL có thể dùng để tải object trong thời gian hiệu lực
     */
    public URL presignedUrl(String bucket, String key, Duration ttl) {
        Duration effectiveTtl = ttl == null ? properties.getPresignedUrlTtl() : ttl;
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(resolveBucket(bucket))
                .key(requireText(key, "key"))
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(effectiveTtl)
                .getObjectRequest(getObjectRequest)
                .build();
        return s3Presigner.presignGetObject(presignRequest).url();
    }

    /**
     * Xóa object khỏi S3.
     *
     * @param bucket tên bucket chứa object
     * @param key object key cần xóa
     */
    public void deleteObject(String bucket, String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(resolveBucket(bucket))
                .key(requireText(key, "key"))
                .build());
    }

    /**
     * Kiểm tra object có tồn tại trong S3 hay không.
     *
     * @param bucket tên bucket chứa object
     * @param key object key cần kiểm tra
     * @return {@code true} nếu object tồn tại, ngược lại trả về {@code false}
     */
    public boolean objectExists(String bucket, String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(resolveBucket(bucket))
                    .key(requireText(key, "key"))
                    .build());
            return true;
        } catch (NoSuchKeyException ex) {
            return false;
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                return false;
            }
            throw ex;
        }
    }

    private String resolveBucket(String bucket) {
        String effectiveBucket = bucket == null || bucket.isBlank() ? properties.getDefaultBucket() : bucket;
        return requireText(effectiveBucket, "bucket");
    }

    private String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
