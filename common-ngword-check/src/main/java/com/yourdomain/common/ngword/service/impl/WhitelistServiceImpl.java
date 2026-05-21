package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.WhitelistMatchMode;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Xử lý whitelist cho luồng check NG, gồm normalize token và đánh giá match rule.
 */
public class WhitelistServiceImpl implements WhitelistService {

    private final NgWordNormalizerService normalizer;

    /**
     * Khởi tạo service xử lý whitelist.
     *
     * @param normalizer normalizer dùng để chuẩn hóa token whitelist
     */
    public WhitelistServiceImpl(NgWordNormalizerService normalizer) {
        this.normalizer = normalizer;
    }

    /**
     * Chuẩn hóa whitelist dạng token raw về cùng chuẩn với pipeline check NG.
     *
     * @param rawWhitelist whitelist token từ request hoặc DB
     * @return tập token whitelist đã normalize
     */
    public Set<String> normalizeWhitelist(Set<String> rawWhitelist) {
        if (rawWhitelist == null || rawWhitelist.isEmpty()) {
            return Set.of();
        }
        Set<String> normalized = new HashSet<>();
        for (String token : rawWhitelist) {
            String normalizedToken = normalizer.normalize(token);
            if (!normalizedToken.isBlank()) {
                normalized.add(normalizedToken);
            }
        }
        return normalized;
    }

    /**
     * Chuẩn hóa whitelist rule.
     *
     * <p>Rule kiểu EXACT sẽ được normalize value trước khi so khớp; rule kiểu REGEX giữ nguyên.
     *
     * @param rules tập rule whitelist dạng raw
     * @return tập rule whitelist đã chuẩn hóa
     */
    public Set<WhitelistRule> normalizeWhitelistRules(Collection<WhitelistRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return Set.of();
        }
        Set<WhitelistRule> normalized = new HashSet<>();
        for (WhitelistRule rule : rules) {
            if (rule == null || rule.value() == null || rule.value().isBlank()) {
                continue;
            }
            if (rule.mode() == WhitelistMatchMode.EXACT) {
                normalized.add(new WhitelistRule(WhitelistMatchMode.EXACT, normalizer.normalize(rule.value())));
            } else {
                normalized.add(rule);
            }
        }
        return normalized;
    }

    /**
     * Kiểm tra một NG token đã normalize có thuộc whitelist hay không.
     *
     * @param normalizedNgWord NG token đã normalize
     * @param normalizedWhitelist tập whitelist token đã normalize
     * @param normalizedRules tập whitelist rule đã normalize
     * @return true nếu token được whitelist, ngược lại false
     */
    public boolean isWhitelisted(String normalizedNgWord, Set<String> normalizedWhitelist, Set<WhitelistRule> normalizedRules) {
        if (normalizedNgWord == null || normalizedNgWord.isBlank()) {
            return false;
        }
        if (normalizedWhitelist != null && normalizedWhitelist.contains(normalizedNgWord)) {
            return true;
        }
        if (normalizedRules == null || normalizedRules.isEmpty()) {
            return false;
        }
        for (WhitelistRule rule : normalizedRules) {
            if (rule.mode() == WhitelistMatchMode.EXACT && normalizedNgWord.equals(rule.value())) {
                return true;
            }
            if (rule.mode() == WhitelistMatchMode.REGEX) {
                try {
                    if (Pattern.compile(rule.value()).matcher(normalizedNgWord).matches()) {
                        return true;
                    }
                } catch (PatternSyntaxException ignored) {
                    // Ignore invalid regex rule to keep NG check pipeline resilient.
                }
            }
        }
        return false;
    }
}
