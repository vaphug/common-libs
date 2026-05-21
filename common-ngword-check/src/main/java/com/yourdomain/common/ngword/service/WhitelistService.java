package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Collection;
import java.util.Set;

/**
 * Dịch vụ xử lý whitelist cho pipeline check NG.
 */
public interface WhitelistService {

    Set<String> normalizeWhitelist(Set<String> rawWhitelist);

    Set<WhitelistRule> normalizeWhitelistRules(Collection<WhitelistRule> rules);

    boolean isWhitelisted(String normalizedNgWord, Set<String> normalizedWhitelist, Set<WhitelistRule> normalizedRules);
}
