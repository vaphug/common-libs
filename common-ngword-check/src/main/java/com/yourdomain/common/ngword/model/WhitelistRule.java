package com.yourdomain.common.ngword.model;

/**
 * Rule whitelist áp dụng cho token NG sau normalize.
 *
 * @param mode kiểu match whitelist: EXACT hoặc REGEX
 * @param value giá trị token hoặc regex pattern
 */
public record WhitelistRule(
        WhitelistMatchMode mode,
        String value
) {
}
