package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.List;
import java.util.Set;

/**
 * Repository abstraction cho dữ liệu NG word và whitelist.
 */
public interface NgWordRepository {

    /**
     * Lấy toàn bộ NG word đang active để phục vụ so khớp.
     *
     * @return danh sách NG word gốc từ DB
     */
    List<String> findActiveNgWords();

    /**
     * Lấy whitelist token theo scope.
     *
     * @param scope scope nghiệp vụ hoặc màn hình. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     * @return tập token whitelist dạng raw
     */
    Set<String> findWhitelistTokens(String scope);

    /**
     * Lấy whitelist rule nâng cao theo scope.
     *
     * @param scope scope nghiệp vụ hoặc màn hình. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     * @return tập rule whitelist kiểu EXACT/REGEX
     */
    Set<WhitelistRule> findWhitelistRules(String scope);
}
