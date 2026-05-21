package com.yourdomain.common.ngword.model;

/**
 * Rule validate input theo scope trước khi check NG.
 *
 * @param ruleName tên rule để trace log/lỗi
 * @param regex regex cần match; có thể null nếu không dùng
 * @param required true nếu input bắt buộc phải có dữ liệu
 * @param minLength độ dài tối thiểu; có thể null
 * @param maxLength độ dài tối đa; có thể null
 */
public record PatternRule(
        String ruleName,
        String regex,
        boolean required,
        Integer minLength,
        Integer maxLength
) {
}
