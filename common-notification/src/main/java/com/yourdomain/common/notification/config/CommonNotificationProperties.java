package com.yourdomain.common.notification.config;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình runtime cho module {@code common-notification}.
 *
 * <p>Mỗi channel có cấu hình riêng để service có thể gọi đúng provider thực tế.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.notification")
public class CommonNotificationProperties {

    /** Bật hoặc tắt auto-configuration của module notification. */
    private boolean enabled = true;
    /** Cấu hình HTTP tổng quát cho provider WEBCAS mail. */
    private HttpChannelProperties webCasMail = new HttpChannelProperties();
    /** Cấu hình provider push dùng Firebase Cloud Messaging HTTP v1. */
    private FcmProperties push = new FcmProperties();
    /** Cấu hình provider LINE Messaging API. */
    private LineProperties line = new LineProperties();
    /** Cấu hình provider SMS dùng Twilio Messages API. */
    private TwilioSmsProperties sms = new TwilioSmsProperties();
    /** Cấu hình provider email qua AWS SES. */
    private AwsMailProperties awsMail = new AwsMailProperties();

    /**
     * Cấu hình HTTP dùng chung cho các provider không có contract riêng trong source.
     */
    @Getter
    @Setter
    public static class HttpChannelProperties {

        /** Base URL của provider HTTP. */
        private String baseUrl;
        /** Relative path của endpoint gửi notification. */
        private String path;
        /** Bearer token dùng để xác thực nếu provider yêu cầu. */
        private String bearerToken;
        /** Tên header chứa API key nếu provider yêu cầu. */
        private String apiKeyHeader;
        /** Giá trị API key tương ứng với {@code apiKeyHeader}. */
        private String apiKey;
        /** Timeout cho một lần gọi provider HTTP. */
        private Duration timeout = Duration.ofSeconds(30);
        /** Header tĩnh được áp dụng cho mọi request của channel. */
        private Map<String, String> headers = new LinkedHashMap<>();

    }

    /**
     * Cấu hình Firebase Cloud Messaging HTTP v1 cho channel push.
     */
    @Getter
    @Setter
    public static class FcmProperties {

        /** Base URL của Firebase Cloud Messaging HTTP v1 API. */
        private String baseUrl = "https://fcm.googleapis.com";
        /** Firebase project ID dùng trong path gửi message. */
        private String projectId;
        /** Đường dẫn file service account JSON; nếu rỗng sẽ dùng Google ADC. */
        private String serviceAccountPath;
        /** Timeout cho một lần gọi FCM API. */
        private Duration timeout = Duration.ofSeconds(30);

    }

    /**
     * Cấu hình LINE Messaging API cho channel gửi push message.
     */
    @Getter
    @Setter
    public static class LineProperties {

        /** Base URL của LINE Messaging API. */
        private String baseUrl = "https://api.line.me";
        /** Channel access token do LINE cấp để gọi push message API. */
        private String channelAccessToken;
        /** Timeout cho một lần gọi LINE API. */
        private Duration timeout = Duration.ofSeconds(30);

    }

    /**
     * Cấu hình Twilio Messages API cho channel gửi SMS.
     */
    @Getter
    @Setter
    public static class TwilioSmsProperties {

        /** Base URL của Twilio REST API. */
        private String baseUrl = "https://api.twilio.com";
        /** Account SID của Twilio account. */
        private String accountSid;
        /** Auth token của Twilio account. */
        private String authToken;
        /** Số điện thoại gửi mặc định nếu caller không truyền sender riêng. */
        private String from;
        /** Messaging Service SID mặc định nếu gửi qua messaging service thay vì số điện thoại cụ thể. */
        private String messagingServiceSid;
        /** Timeout cho một lần gọi Twilio API. */
        private Duration timeout = Duration.ofSeconds(30);

    }

    /**
     * Cấu hình AWS SES cho channel gửi email.
     */
    @Getter
    @Setter
    public static class AwsMailProperties {

        /** AWS region dùng để khởi tạo SES client. */
        private String region;
        /** Địa chỉ email gửi mặc định nếu caller không truyền sender riêng. */
        private String fromAddress;

    }
}
