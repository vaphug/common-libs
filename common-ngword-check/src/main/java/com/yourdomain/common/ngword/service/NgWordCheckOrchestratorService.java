package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NgWordCheckOutcome;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Set;

/**
 * Dịch vụ điều phối toàn bộ pipeline check NG.
 */
public interface NgWordCheckOrchestratorService {

    NgWordCheckOutcome check(String rawInput, String scope, Set<String> inlineWhitelist, Set<WhitelistRule> inlineRules);
}
