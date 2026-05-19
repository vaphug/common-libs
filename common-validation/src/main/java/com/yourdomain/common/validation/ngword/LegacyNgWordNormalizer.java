package com.yourdomain.common.validation.ngword;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Legacy-equivalent normalizer based on FUNC_CNV_CHKWORD.
 *
 * <p>Order:
 * 1) lower -> upper
 * 2) compatibility normalize (NFKC) for Shift_JIS-like variants
 * 3) half-width -> full-width (ASCII explicitly, kana via NFKC)
 * 4) remove ASCII symbols (full-width forms) + middle dot
 * 5) notation variants mapping
 */
public class LegacyNgWordNormalizer implements NgWordNormalizer {

    private static final Set<Character> REMOVABLE_FULLWIDTH_SYMBOLS = new HashSet<>();
    private static final Map<String, String> NOTATION_VARIANTS = new LinkedHashMap<>();

    static {
        // Same symbol set as legacy SQL (full-width forms).
        char[] symbols = new char[] {
                '！', '”', '＃', '＄', '％', '＆', '′', '（', '）', '＊', '＋', '，', '‐', '．', '／',
                '：', '；', '＜', '＝', '＞', '？', '＠', '［', '￥', '］', '＾', '＿', '｛', '｜', '｝', '￣', '・'
        };
        for (char c : symbols) {
            REMOVABLE_FULLWIDTH_SYMBOLS.add(c);
        }

        // Display-variant (表示ゆれ) normalization extension point.
        NOTATION_VARIANTS.put("ヵ", "カ");
        NOTATION_VARIANTS.put("ヶ", "ケ");
        NOTATION_VARIANTS.put("髙", "高");
        NOTATION_VARIANTS.put("﨑", "崎");
        NOTATION_VARIANTS.put("塚", "塚");
    }

    @Override
    public String normalize(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String upper = input.toUpperCase(Locale.JAPAN);
        String katakana = hiraganaToKatakana(upper);
        String normalizedCompatibility = Normalizer.normalize(katakana, Normalizer.Form.NFKC);
        String zenkaku = toZenkakuAscii(normalizedCompatibility);
        String noSymbols = removeSymbols(zenkaku);
        return normalizeNotationVariants(noSymbols);
    }

    private String toZenkakuAscii(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c == ' ') {
                sb.append('\u3000');
            } else if (c >= 0x21 && c <= 0x7E) {
                sb.append((char) (c + 0xFEE0));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String removeSymbols(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (!REMOVABLE_FULLWIDTH_SYMBOLS.contains(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String hiraganaToKatakana(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c >= '\u3041' && c <= '\u3096') {
                sb.append((char) (c + 0x60));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String normalizeNotationVariants(String input) {
        String normalized = input;
        for (Map.Entry<String, String> entry : NOTATION_VARIANTS.entrySet()) {
            normalized = normalized.replace(entry.getKey(), entry.getValue());
        }
        return normalized;
    }
}
