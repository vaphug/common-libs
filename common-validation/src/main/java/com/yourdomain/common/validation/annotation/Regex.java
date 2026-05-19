package com.yourdomain.common.validation.annotation;

import com.yourdomain.common.validation.constraint.RegexValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RegexValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {
    String message() default "{validation.regex.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Backward-compatible field for existing usage: @Regex(pattern="^[0-9]+$")
     */
    String pattern() default "";

    /**
     * Flexible value field. Can be a regex expression or named format like yyyyMMdd.
     */
    String value() default "";

    /**
     * Optional parameters. Example: "20261010,20201030" for date-range check.
     */
    String param() default "";
}
