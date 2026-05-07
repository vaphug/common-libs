package com.yourdomain.common.sqs;

import com.yourdomain.common.core.MessageService;
import com.yourdomain.common.messages.MessageConstants;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class SqsListenerService {

    private final MessageService messageService;

    public SqsListenerService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void demonstrateMessageUsage() {
        // Lấy message từ common-messages
        String systemErrorMsg = messageService.getMessage(MessageConstants.ERR_SYSTEM_ERROR);
        System.out.println("Common Message (System Error): " + systemErrorMsg);

        // Lấy message từ sqs-messages.properties (đã được cấu hình trong MessageCoreAutoConfig)
        String sqsSendFailedMsg = messageService.getMessage("sqs.send.failed");
        System.out.println("SQS Specific Message (Send Failed): " + sqsSendFailedMsg);

        // Lấy message với tham số và locale cụ thể
        String queueName = "my-test-queue";
        String sqsQueueNotFoundMsg = messageService.getMessage("sqs.queue.not.found", new Object[]{queueName}, Locale.ENGLISH);
        System.out.println("SQS Specific Message (Queue Not Found with args and locale): " + sqsQueueNotFoundMsg);

        // Lấy message từ MessageConstants nhưng nội dung có thể được override bởi sqs-messages.properties nếu có cùng key
        // (Trong trường hợp này, MessageConstants.SQS_SEND_FAILED và "sqs.send.failed" là cùng một key)
        String commonSqsSendFailedMsg = messageService.getMessage(MessageConstants.SQS_SEND_FAILED);
        System.out.println("Common SQS Message (Send Failed from Constants): " + commonSqsSendFailedMsg);
    }

    // Example of a method that might throw an exception and use messages
    public void simulateSqsOperation(boolean shouldFail, String queueName) {
        try {
            if (shouldFail) {
                // Simulate a failure that needs a message
                throw new RuntimeException("Simulated SQS send failure.");
            }
            System.out.println(messageService.getMessage("sqs.send.success"));
        } catch (RuntimeException e) {
            // Use a message from MessageConstants for a generic SQS failure
            String errorMsg = messageService.getMessage(MessageConstants.SQS_SEND_FAILED);
            System.err.println("Operation failed: " + errorMsg + " - Details: " + e.getMessage());

            // Or use a more specific message from sqs-messages.properties if needed
            if (e.getMessage().contains("queue")) {
                String notFoundMsg = messageService.getMessage("sqs.queue.not.found", new Object[]{queueName}, Locale.getDefault());
                System.err.println("Specific SQS error: " + notFoundMsg);
            }
        }
    }
}
