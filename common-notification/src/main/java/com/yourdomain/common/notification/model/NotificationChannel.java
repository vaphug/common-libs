package com.yourdomain.common.notification.model;

/**
 * Danh sách channel notification được module hỗ trợ.
 */
public enum NotificationChannel {
    /** Gửi mail qua provider WEBCAS HTTP configurable. */
    WEBCAS_MAIL,
    /** Gửi push notification qua Firebase Cloud Messaging HTTP v1. */
    PUSH,
    /** Gửi push message qua LINE Messaging API. */
    LINE,
    /** Gửi SMS qua Twilio Messages API. */
    SMS,
    /** Gửi mail qua AWS Simple Email Service. */
    AWS_MAIL
}
