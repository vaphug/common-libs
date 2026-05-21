package com.yourdomain.common.ngword.grpc;

import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Set;

/**
 * Request chuẩn cho API gRPC kiểm tra NG word.
 *
 * @param input text người dùng nhập cần kiểm tra
 * @param scope scope nghiệp vụ để chọn rule validate/whitelist theo cấu hình
 * @param inlineWhitelist whitelist token truyền trực tiếp theo request
 * @param inlineWhitelistRules whitelist rule dạng EXACT/REGEX truyền trực tiếp
 */
public record CheckNgWordRequest(
        String input,
        String scope,
        Set<String> inlineWhitelist,
        Set<WhitelistRule> inlineWhitelistRules
) {
}
