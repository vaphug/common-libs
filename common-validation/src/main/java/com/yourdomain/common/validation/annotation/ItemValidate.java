package com.yourdomain.common.validation.annotation;

import com.yourdomain.common.validation.constraint.ItemValidateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation validate linh hoạt theo rule cấu hình trong DomainValidationData.json.
 * <p>
 * Nếu không truyền item, validator sẽ cố gắng suy ra từ tên field runtime.
 */
@Documented
@Constraint(validatedBy = ItemValidateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemValidate {
    String message() default "{validation.item.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Key nghiệp vụ trong JSON. Nếu để trống, validator sẽ dùng tên field (nếu suy ra được).
     */
    String item() default "";
}
