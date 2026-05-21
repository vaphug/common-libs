package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NgWordCheckResult;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Set;

/**
 * Dịch vụ check NG dựa trên dữ liệu lấy từ repository.
 */
public interface NgWordDbCheckService {

    NgWordCheckResult checkAgainstDb(String rawInput, Set<String> whitelist, Set<WhitelistRule> whitelistRules);

    NgWordCheckResult checkAgainstDbByScope(String rawInput, String scope);
}
