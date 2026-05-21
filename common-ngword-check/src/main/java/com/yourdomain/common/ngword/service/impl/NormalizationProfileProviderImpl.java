package com.yourdomain.common.ngword.service.impl;

import com.yourdomain.common.ngword.model.NormalizationProfile;
import com.yourdomain.common.ngword.service.NormalizationProfileProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cung cấp profile normalize mặc định bám theo rule legacy đã confirm.
 */
public class NormalizationProfileProviderImpl implements NormalizationProfileProvider {

    /**
     * Tạo profile normalize mặc định cho scope hiện tại.
     *
     * @param domainOrScreen mã domain hoặc màn hình nghiệp vụ
     * @return profile normalize bật các bước upper, hira-kata, half-full, remove symbols
     */
    @Override
    public NormalizationProfile getProfile(String domainOrScreen) {
        return new NormalizationProfile(
                true,
                true,
                true,
                true,
                defaultRemovableSymbols(),
                defaultNotationVariants());
    }

    /**
     * Trả mapping biểu thị tương đương mặc định.
     *
     * @param domainOrScreen mã domain hoặc màn hình nghiệp vụ
     * @return map ký tự nguồn-đích cho bước normalize 表示ゆれ
     */
    @Override
    public Map<String, String> getNotationVariantMap(String domainOrScreen) {
        return defaultNotationVariants();
    }

    private Set<Character> defaultRemovableSymbols() {
        return Set.of(
                '！', '”', '＃', '＄', '％', '＆', '′', '（', '）', '＊', '＋', '，', '‐', '．', '／',
                '：', '；', '＜', '＝', '＞', '？', '＠', '［', '￥', '］', '＾', '＿', '｛', '｜', '｝', '￣', '・');
    }

    private Map<String, String> defaultNotationVariants() {
        Map<String, String> variants = new LinkedHashMap<>();
        variants.put("ヵ", "カ");
        variants.put("ヶ", "ケ");
        variants.put("髙", "高");
        variants.put("﨑", "崎");
        variants.put("塚", "塚");
        return variants;
    }
}
