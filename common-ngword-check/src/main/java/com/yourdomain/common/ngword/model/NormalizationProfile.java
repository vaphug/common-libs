package com.yourdomain.common.ngword.model;

import java.util.Map;
import java.util.Set;

/**
 * Cấu hình rule chuẩn hóa cho pipeline check NG.
 *
 * @param uppercase bật/tắt chuyển chữ thường sang chữ hoa
 * @param hiraganaToKatakana bật/tắt chuyển Hiragana sang Katakana
 * @param halfwidthToFullwidth bật/tắt chuyển ký tự half-width sang full-width
 * @param removeAsciiSymbols bật/tắt loại bỏ ký hiệu ASCII sau khi full-width hóa
 * @param removableSymbols tập ký tự cần loại bỏ
 * @param notationVariants map biểu thị tương đương (表示ゆれ)
 */
public record NormalizationProfile(
        boolean uppercase,
        boolean hiraganaToKatakana,
        boolean halfwidthToFullwidth,
        boolean removeAsciiSymbols,
        Set<Character> removableSymbols,
        Map<String, String> notationVariants
) {
}
