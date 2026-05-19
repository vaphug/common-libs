package com.yourdomain.common.sqs.config;

import com.yourdomain.common.sqs.idempotency.InMemoryMessageIdempotencyStore;
import com.yourdomain.common.sqs.idempotency.MessageIdempotencyStore;
import com.yourdomain.common.sqs.service.CommonSqsService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Auto-configuration cho common-sqs.
 */
@AutoConfiguration
@ConditionalOnClass(SqsClient.class)
@EnableConfigurationProperties(CommonSqsProperties.class)
@ConditionalOnProperty(prefix = "common.sqs", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CommonSqsAutoConfiguration {

    /**
     * Tạo SQS client mặc định.
     *
     * @return {@link SqsClient} mặc định dùng credential/provider chain chuẩn của AWS SDK
     */
    @Bean
    @ConditionalOnMissingBean
    public SqsClient sqsClient() {
        return SqsClient.create();
    }

    /**
     * Idempotency mặc định dạng in-memory.
     *
     * @return store in-memory phù hợp cho local/dev hoặc single instance
     */
    @Bean
    @ConditionalOnMissingBean(MessageIdempotencyStore.class)
    public MessageIdempotencyStore messageIdempotencyStore() {
        return new InMemoryMessageIdempotencyStore();
    }

    /**
     * Service thao tác SQS common.
     *
     * @param sqsClient SQS client dùng để gọi AWS API
     * @param properties cấu hình mặc định của module common-sqs
     * @param idempotencyStore store dùng để chống xử lý trùng message
     * @return service facade cho các thao tác send, receive, delete, và process message
     */
    @Bean
    @ConditionalOnMissingBean
    public CommonSqsService commonSqsService(
            SqsClient sqsClient,
            CommonSqsProperties properties,
            MessageIdempotencyStore idempotencyStore
    ) {
        return new CommonSqsService(sqsClient, properties, idempotencyStore);
    }
}
