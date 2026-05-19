package com.yourdomain.common.database.util;

import java.util.regex.Pattern;

/**
 * Validator cho tên bảng/cột dynamic nhằm giảm rủi ro SQL injection ở phần identifier.
 */
public final class SqlIdentifierValidator {

    private static final Pattern IDENTIFIER = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private SqlIdentifierValidator() {
    }

    /**
     * Chuẩn hoá và xác thực tên identifier SQL.
     *
     * @param raw tên đầu vào
     * @return tên đã trim
     */
    public static String safeIdentifier(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("SQL identifier không được null");
        }
        String value = raw.trim();
        if (!IDENTIFIER.matcher(value).matches()) {
            throw new IllegalArgumentException("SQL identifier không hợp lệ: " + raw);
        }
        return value;
    }
}
