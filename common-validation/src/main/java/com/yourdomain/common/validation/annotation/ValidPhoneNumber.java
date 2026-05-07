package com.yourdomain.common.validation.annotation;

import com.yourdomain.common.validation.constraint.PhoneNumberValidator;

import com.yourdomain.common.messages.MessageConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation validate số điện thoại theo rule chuẩn hóa tại {@link PhoneNumberValidator}.
 * <p>
 * Message mặc định sử dụng key từ {@link MessageConstants} để dùng chung toàn hệ thống.
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    /**
     * Message key tham chiếu tới common-messages (ví dụ valid.phone.required).
     */
    String message() default "{" + MessageConstants.VALID_PHONE_REQUIRED + "}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
