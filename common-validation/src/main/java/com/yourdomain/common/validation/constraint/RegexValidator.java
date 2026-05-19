package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.Regex;
import com.yourdomain.common.validation.validator.RegexMatcher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegexValidator implements ConstraintValidator<Regex, String> {
    private String expression;
    private String value;
    private String param;

    @Override
    public void initialize(Regex constraintAnnotation) {
        this.expression = constraintAnnotation.pattern();
        this.value = constraintAnnotation.value();
        this.param = constraintAnnotation.param();

        if ((this.expression == null || this.expression.isBlank()) && value != null && !value.isBlank()) {
            this.expression = value;
        }
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null || input.isBlank()) {
            return true;
        }

        if ("yyyyMMdd".equals(value) && param != null && !param.isBlank()) {
            return validateDateRange(input, "yyyyMMdd", param);
        }

        if ("yyyyMM".equals(value) && param != null && !param.isBlank()) {
            return validateDateRange(input + "01", "yyyyMMdd", normalizeRangeForYearMonth(param));
        }

        return RegexMatcher.isValid(input, expression);
    }

    private boolean validateDateRange(String input, String pattern, String rangeParam) {
        String[] split = rangeParam.split(",");
        if (split.length != 2) {
            return false;
        }

        String startRaw = split[0].trim();
        String endRaw = split[1].trim();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate date = LocalDate.parse(input, formatter);
            LocalDate start = LocalDate.parse(startRaw, formatter);
            LocalDate end = LocalDate.parse(endRaw, formatter);

            if (start.isAfter(end)) {
                LocalDate tmp = start;
                start = end;
                end = tmp;
            }
            return (date.isEqual(start) || date.isAfter(start))
                    && (date.isEqual(end) || date.isBefore(end));
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private String normalizeRangeForYearMonth(String rawRange) {
        String[] split = rawRange.split(",");
        if (split.length != 2) {
            return rawRange;
        }
        return split[0].trim() + "01," + split[1].trim() + "01";
    }
}
