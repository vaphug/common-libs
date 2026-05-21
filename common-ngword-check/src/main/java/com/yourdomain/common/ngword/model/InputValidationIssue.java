package com.yourdomain.common.ngword.model;

/**
 * Mô tả một lỗi validate cụ thể của input.
 *
 * @param code mã lỗi nghiệp vụ, ví dụ REQUIRED hoặc PATTERN_MISMATCH
 * @param message thông điệp lỗi để trả về cho caller
 * @param rejectedValue giá trị đầu vào gây lỗi
 */
public record InputValidationIssue(
        String code,
        String message,
        String rejectedValue
) {
}
