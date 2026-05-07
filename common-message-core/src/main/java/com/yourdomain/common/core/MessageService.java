package com.yourdomain.common.core;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service dùng chung để đọc nội dung message theo message code (messageId).
 * <p>
 * Message được resolve thông qua {@link MessageSource} đã được cấu hình ở module core.
 * Nếu không tìm thấy key tương ứng, hệ thống trả về chính messageId để dễ debug.
 */
@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Lấy message theo messageId, dùng locale hiện tại trong request context.
     *
     * @param messageId key cần tra cứu trong file i18n.
     * @return nội dung message đã resolve.
     */
    public String getMessage(String messageId) {
        return messageSource.getMessage(messageId, null, messageId, LocaleContextHolder.getLocale());
    }

    /**
     * Lấy message theo messageId với locale chỉ định.
     *
     * @param messageId key cần tra cứu trong file i18n.
     * @param locale locale mong muốn (vi, en, ...).
     * @return nội dung message đã resolve.
     */
    public String getMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId, null, messageId, locale);
    }

    /**
     * Lấy message có tham số động, ví dụ: {@code "queue {0} not found"}.
     *
     * @param messageId key cần tra cứu trong file i18n.
     * @param args danh sách tham số để format message.
     * @param locale locale mong muốn (vi, en, ...).
     * @return nội dung message đã resolve và format.
     */
    public String getMessage(String messageId, Object[] args, Locale locale) {
        return messageSource.getMessage(messageId, args, messageId, locale);
    }
}
