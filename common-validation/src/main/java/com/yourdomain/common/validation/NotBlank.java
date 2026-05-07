package com.yourdomain.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation validate chuỗi bắt buộc có giá trị (không null, không rỗng, không toàn khoảng trắng).
 * <p>
 * Dùng khi muốn áp dụng rule not blank dạng reusable cho DTO/command object trong các module.
 */
@Documented
@Constraint(validatedBy = NotBlankValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {
    /**
     * Message key sẽ được resolve qua MessageSource đã cấu hình trong common-message-core.
     */
    String message() default "{validation.notblank}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
