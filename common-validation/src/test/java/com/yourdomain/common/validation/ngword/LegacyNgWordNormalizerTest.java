package com.yourdomain.common.validation.ngword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LegacyNgWordNormalizerTest {

    private final LegacyNgWordNormalizer normalizer = new LegacyNgWordNormalizer();

    @Test
    void normalize_shouldMatchLegacyRules() {
        String normalized = normalizer.normalize("abc@# 123・");
        assertEquals("ＡＢＣ　１２３", normalized);
    }

    @Test
    void check_shouldDetectNgWordAfterNormalize() {
        NgWordCheckService service = new NgWordCheckService(normalizer);
        NgWordCheckResult result = service.check("ab@c", List.of("ＡＢＣ"));
        assertTrue(result.blocked());
        assertEquals("ＡＢＣ", result.normalizedMatchedWord());
    }

    @Test
    void normalize_shouldConvertHiraganaToKatakana() {
        assertEquals("カタカナ", normalizer.normalize("かたかな"));
    }

    @Test
    void normalize_shouldApplyNotationVariants() {
        assertEquals("高崎", normalizer.normalize("髙﨑"));
    }

    @Test
    void normalize_shouldConvertHalfWidthKanaToFullWidthKana() {
        assertEquals("カタカナ", normalizer.normalize("ｶﾀｶﾅ"));
    }

    @Test
    void check_shouldApplyWhitelistUsingRawTokens() {
        NgWordCheckService service = new NgWordCheckService(normalizer);
        NgWordCheckResult result = service.check("ab@c", List.of("ＡＢＣ"), Set.of("abc"));
        assertFalse(result.blocked());
    }
}
