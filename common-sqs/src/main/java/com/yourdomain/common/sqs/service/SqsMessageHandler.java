package com.yourdomain.common.sqs.service;

import software.amazon.awssdk.services.sqs.model.Message;

/**
 * Handler business cho từng message trong
 * {@link CommonSqsService#processMessages(String, com.yourdomain.common.sqs.model.ProcessMessageOptions, SqsMessageHandler)}.
 */
@FunctionalInterface
public interface SqsMessageHandler {
    /**
     * Xử lý business cho một message.
     *
     * @param message message nhận từ SQS
     * @throws Exception cho phép ném exception để common-sqs đánh dấu failed và không delete message
     */
    void handle(Message message) throws Exception;
}
