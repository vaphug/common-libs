package com.yourdomain.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validator implementation cho annotation {@link ValidPhoneNumber}.
 * <p>
 * Rule hiện tại:
 * <p>
 * - Cho phép dấu '+' ở đầu (tùy chọn).
 * <p>
 * - Chỉ nhận ký tự số.
 * <p>
 * - Độ dài từ 9 đến 15 chữ số.
 */
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{9,15}$");

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        // Null/rỗng được xem là invalid để trả về message của @ValidPhoneNumber.
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}
