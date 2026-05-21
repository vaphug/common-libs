package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NgWordCheckResult;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Collection;
import java.util.Set;

/**
 * Dịch vụ so khớp NG word trên dữ liệu đã normalize.
 */
public interface NgWordCheckService {

    NgWordCheckResult check(
            String rawInput,
            Collection<String> ngWords,
            Set<String> whitelist,
            Set<WhitelistRule> whitelistRules
    );
}
