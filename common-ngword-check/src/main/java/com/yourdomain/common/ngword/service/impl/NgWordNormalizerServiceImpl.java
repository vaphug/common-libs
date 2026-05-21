package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NormalizationProfile;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Normalizer tương thích rule legacy trong FUNC_CNV_CHKWORD và các mở rộng đã confirm.
 */
public class NgWordNormalizerServiceImpl implements NgWordNormalizerService {

    private final NormalizationProfileProvider profileProvider;
    private final String domainOrScreen;

    /**
     * Khởi tạo normalizer theo scope nghiệp vụ.
     *
     * @param profileProvider provider trả profile normalize theo scope
     * @param domainOrScreen scope nghiệp vụ hoặc màn hình
     */
    public NgWordNormalizerServiceImpl(NormalizationProfileProvider profileProvider, String domainOrScreen) {
        this.profileProvider = profileProvider;
        this.domainOrScreen = domainOrScreen;
    }

    /**
     * Chuẩn hóa chuỗi theo rule legacy phục vụ so khớp NG word.
     *
     * @param text chuỗi gốc cần chuẩn hóa
     * @return chuỗi đã normalize theo profile scope hiện tại
     */
    @Override
    public String normalize(String text) {
        return normalizeInternal(text);
    }

    private String normalizeInternal(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        NormalizationProfile profile = profileProvider.getProfile(domainOrScreen);
        String value = input;

        if (profile.uppercase()) {
            value = value.toUpperCase(Locale.JAPAN);
        }
        if (profile.hiraganaToKatakana()) {
            value = hiraganaToKatakana(value);
        }

        value = Normalizer.normalize(value, Normalizer.Form.NFKC);

        if (profile.halfwidthToFullwidth()) {
            value = toZenkakuAscii(value);
        }
        if (profile.removeAsciiSymbols()) {
            value = removeSymbols(value, profile.removableSymbols());
        }

        return normalizeNotationVariants(value, profile.notationVariants());
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

    private String removeSymbols(String input, Set<Character> removableSymbols) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (!removableSymbols.contains(c)) {
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

    private String normalizeNotationVariants(String input, Map<String, String> variants) {
        String normalized = input;
        for (Map.Entry<String, String> entry : variants.entrySet()) {
            normalized = normalized.replace(entry.getKey(), entry.getValue());
        }
        return normalized;
    }
}
