package com.yourdomain.common.validation.validator;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Bộ hàm validate nghiệp vụ dùng lại cho ItemValidate và các annotation riêng.
 */
public final class DomainValidator {
    private static final Pattern HALF_WIDTH_PATTERN = Pattern.compile("^[\\x20-\\x7E]+$");
    private static final Pattern ASCII_DIGITS = Pattern.compile("^[0-9]+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^(0|[1-9][0-9]*)(\\.[0-9]+)?$");
    private static final Pattern NUMBER_WITH_MINUS_PATTERN = Pattern.compile("^-?(0|[1-9][0-9]*)(\\.[0-9]+)?$");
    private static final Pattern INTEGER_WITH_MINUS_PATTERN = Pattern.compile("^-?(0|[1-9][0-9]*)$");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^(0|[1-9][0-9]*)$");
    private static final Pattern DATETIME_12 = Pattern.compile("^[0-9]{12}$");
    private static final Pattern YYYYMMDD = Pattern.compile("^[0-9]{8}$");
    private static final Pattern YYYYMM = Pattern.compile("^[0-9]{6}$");
    private static final Pattern YYYY = Pattern.compile("^[0-9]{4}$");

    private DomainValidator() {
    }

    public static boolean validate(Object value, ValidationType type, Map<String, String> params) {
        return switch (type) {
            case ANY_TEXT -> true;
            case FULL_WIDTH -> isFullWidth(text(value));
            case HALF_WIDTH -> isHalfWidth(text(value));
            case FULL_AND_HALF_WIDTH -> isFullAndHalfWidth(text(value));
            case DIGITS_ONLY -> isDigitsOnly(text(value));
            case NUMBER -> isNumber(text(value), false);
            case NUMBER_WITH_MINUS -> isNumber(text(value), true);
            case NUMBER_GT_ZERO -> isGreaterThanZeroNumber(text(value));
            case AMOUNT -> isAmount(text(value), false);
            case AMOUNT_WITH_MINUS -> isAmount(text(value), true);
            case AMOUNT_MINUS_ONLY -> isMinusOnlyAmount(text(value));
            case AMOUNT_GT_ZERO -> isAmountGreaterThanZero(text(value));
            case POSITIVE_NUMBER -> isPositiveNumber(number(value));
            case NEGATIVE_NUMBER -> isNegativeNumber(number(value));
            case DECIMAL_NUMBER -> isDecimalNumber(number(value));
            case MONEY_DENOMINATION -> isAllowedMoneyDenomination(number(value), params);
            case DATE_YYYY -> isDateYYYY(text(value));
            case DATE_YYYYMM -> isDateYYYYMM(text(value));
            case DATE_YYYYMMDD -> isDateYYYYMMDD(text(value));
            case DATETIME_YYYYMMDDHHMM -> isDateTimeYYYYMMDDHHMM(text(value));
            case ENUM_IN_SET -> isEnumInSet(text(value), params);
            case REQUIRED -> isRequired(value);
        };
    }

    public static boolean isFullWidth(String input) {
        if (input == null || input.isBlank()) {
            return true;
        }
        for (char ch : input.toCharArray()) {
            if (String.valueOf(ch).getBytes(StandardCharsets.UTF_8).length < 2) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHalfWidth(String input) {
        if (input == null || input.isBlank()) {
            return true;
        }
        return HALF_WIDTH_PATTERN.matcher(input).matches();
    }

    public static boolean isFullAndHalfWidth(String input) {
        if (input == null || input.isBlank()) {
            return true;
        }
        boolean hasFull = false;
        boolean hasHalf = false;
        for (char ch : input.toCharArray()) {
            if (String.valueOf(ch).getBytes(StandardCharsets.UTF_8).length < 2) {
                hasHalf = true;
            } else {
                hasFull = true;
            }
            if (hasFull && hasHalf) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDigitsOnly(String input) {
        return input != null && ASCII_DIGITS.matcher(input).matches();
    }

    public static boolean isNumber(String input, boolean allowMinus) {
        if (input == null) {
            return false;
        }
        return (allowMinus ? NUMBER_WITH_MINUS_PATTERN : NUMBER_PATTERN).matcher(input).matches();
    }

    public static boolean isGreaterThanZeroNumber(String input) {
        if (!isNumber(input, true)) {
            return false;
        }
        return new BigDecimal(input).compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isAmount(String input, boolean allowMinus) {
        if (input == null) {
            return false;
        }
        return (allowMinus ? INTEGER_WITH_MINUS_PATTERN : INTEGER_PATTERN).matcher(input).matches();
    }

    public static boolean isMinusOnlyAmount(String input) {
        if (!INTEGER_WITH_MINUS_PATTERN.matcher(input == null ? "" : input).matches()) {
            return false;
        }
        return new BigDecimal(input).compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isAmountGreaterThanZero(String input) {
        if (!INTEGER_PATTERN.matcher(input == null ? "" : input).matches()) {
            return false;
        }
        return new BigDecimal(input).compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isPositiveNumber(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegativeNumber(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isDecimalNumber(BigDecimal number) {
        return number != null && number.scale() > 0;
    }

    public static boolean isAllowedMoneyDenomination(BigDecimal number, Map<String, String> params) {
        if (number == null) {
            return false;
        }

        String allowedRaw = params.getOrDefault("allowed", "");
        if (allowedRaw.isBlank()) {
            return true;
        }

        Set<BigDecimal> allowed = new HashSet<>();
        Arrays.stream(allowedRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(BigDecimal::new)
                .forEach(allowed::add);

        return allowed.contains(number.stripTrailingZeros());
    }

    public static boolean isDateYYYY(String input) {
        if (input == null || !YYYY.matcher(input).matches()) {
            return false;
        }
        try {
            Year.of(Integer.parseInt(input));
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }

    public static boolean isDateYYYYMM(String input) {
        if (input == null || !YYYYMM.matcher(input).matches()) {
            return false;
        }
        try {
            YearMonth.of(
                    Integer.parseInt(input.substring(0, 4)),
                    Integer.parseInt(input.substring(4, 6))
            );
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }

    public static boolean isDateYYYYMMDD(String input) {
        if (input == null || !YYYYMMDD.matcher(input).matches()) {
            return false;
        }
        try {
            LocalDate.of(
                    Integer.parseInt(input.substring(0, 4)),
                    Integer.parseInt(input.substring(4, 6)),
                    Integer.parseInt(input.substring(6, 8))
            );
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }

    public static boolean isDateTimeYYYYMMDDHHMM(String input) {
        if (input == null || !DATETIME_12.matcher(input).matches()) {
            return false;
        }
        try {
            LocalDate.of(
                    Integer.parseInt(input.substring(0, 4)),
                    Integer.parseInt(input.substring(4, 6)),
                    Integer.parseInt(input.substring(6, 8))
            );
            int hour = Integer.parseInt(input.substring(8, 10));
            int minute = Integer.parseInt(input.substring(10, 12));
            return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
        } catch (DateTimeException | NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isEnumInSet(String input, Map<String, String> params) {
        if (input == null || input.isBlank()) {
            return false;
        }
        String allowedRaw = params.getOrDefault("allowed", "");
        if (allowedRaw.isBlank()) {
            return false;
        }
        return Arrays.stream(allowedRaw.split(","))
                .map(String::trim)
                .anyMatch(input::equals);
    }

    public static boolean isRequired(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String s) {
            return !s.isBlank();
        }
        return true;
    }

    private static BigDecimal number(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        if (value instanceof String s && !s.isBlank()) {
            try {
                return new BigDecimal(s.trim());
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }

    private static String text(Object value) {
        return value == null ? null : value.toString();
    }
}
