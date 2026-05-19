package com.yourdomain.common.validation.ngword;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NgWordCheckService {

    private final NgWordNormalizer normalizer;

    public NgWordCheckService(NgWordNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    public NgWordCheckResult check(String input, Collection<String> ngWords) {
        return check(input, ngWords, Set.of());
    }

    /**
     * @param whitelist raw tokens that should be ignored even if they match NG words.
     */
    public NgWordCheckResult check(String input, Collection<String> ngWords, Set<String> whitelist) {
        String normalizedInput = normalizer.normalize(input);
        Set<String> normalizedWhitelist = normalizeWhitelist(whitelist);
        if (ngWords == null || ngWords.isEmpty()) {
            return new NgWordCheckResult(false, normalizedInput, null, null);
        }

        for (String ngWord : ngWords) {
            String normalizedNgWord = normalizer.normalize(ngWord);
            if (normalizedNgWord.isBlank()) {
                continue;
            }
            if (normalizedWhitelist.contains(normalizedNgWord)) {
                continue;
            }
            if (normalizedInput.contains(normalizedNgWord)) {
                return new NgWordCheckResult(true, normalizedInput, ngWord, normalizedNgWord);
            }
        }

        return new NgWordCheckResult(false, normalizedInput, null, null);
    }

    private Set<String> normalizeWhitelist(Set<String> whitelist) {
        if (whitelist == null || whitelist.isEmpty()) {
            return Set.of();
        }
        Set<String> normalized = new HashSet<>();
        for (String token : whitelist) {
            String normalizedToken = normalizer.normalize(token);
            if (!normalizedToken.isBlank()) {
                normalized.add(normalizedToken);
            }
        }
        return normalized;
    }
}
