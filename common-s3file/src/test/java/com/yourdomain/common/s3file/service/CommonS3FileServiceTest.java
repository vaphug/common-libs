package com.yourdomain.common.s3file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yourdomain.common.s3file.config.CommonS3FileProperties;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

class CommonS3FileServiceTest {

    @Test
    void putObjectUploadsUtf8TextWithContentType() throws Exception {
        S3Client s3Client = mock(S3Client.class);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());
        CommonS3FileService service = new CommonS3FileService(
                s3Client,
                mock(S3Presigner.class),
                new CommonS3FileProperties());

        service.putObject("bucket-a", "templates.csv", "hello", "text/csv");

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> bodyCaptor = ArgumentCaptor.forClass(RequestBody.class);
        verify(s3Client).putObject(requestCaptor.capture(), bodyCaptor.capture());
        assertThat(requestCaptor.getValue().bucket()).isEqualTo("bucket-a");
        assertThat(requestCaptor.getValue().key()).isEqualTo("templates.csv");
        assertThat(requestCaptor.getValue().contentType()).isEqualTo("text/csv");
        assertThat(bodyCaptor.getValue().contentStreamProvider().newStream().readAllBytes())
                .isEqualTo("hello".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void objectExistsReturnsTrueWhenHeadObjectSucceeds() {
        S3Client s3Client = mock(S3Client.class);
        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(HeadObjectResponse.builder().build());
        CommonS3FileService service = new CommonS3FileService(
                s3Client,
                mock(S3Presigner.class),
                new CommonS3FileProperties());

        assertThat(service.objectExists("bucket-a", "templates.csv")).isTrue();
    }
}
