package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NgWordCheckResult;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Collection;
import java.util.Set;

/**
 * Thực hiện so khớp NG word trên dữ liệu đã chuẩn hóa.
 */
public class NgWordCheckServiceImpl implements NgWordCheckService {

    private final NgWordNormalizerService normalizer;
    private final WhitelistService whitelistService;

    /**
     * Khởi tạo service so khớp NG word.
     *
     * @param normalizer normalizer dùng cho input và NG token
     * @param whitelistService service xử lý whitelist token/rule
     */
    public NgWordCheckServiceImpl(NgWordNormalizerService normalizer, WhitelistService whitelistService) {
        this.normalizer = normalizer;
        this.whitelistService = whitelistService;
    }

    /**
     * Kiểm tra input có chứa NG word hay không.
     *
     * <p>Luồng xử lý:
     * 1) Normalize input, whitelist token và whitelist rule về cùng chuẩn so khớp.
     * 2) Duyệt danh sách NG word, normalize từng token và bỏ qua token rỗng.
     * 3) Bỏ qua token nằm trong whitelist, sau đó kiểm tra {@code contains}.
     *
     * @param rawInput text gốc người dùng nhập
     * @param ngWords danh sách NG word gốc từ nguồn dữ liệu
     * @param whitelist whitelist dạng token
     * @param whitelistRules whitelist dạng rule EXACT/REGEX
     * @return kết quả check gồm cờ NG, input normalize và token match (nếu có)
     */
    public NgWordCheckResult check(
            String rawInput,
            Collection<String> ngWords,
            Set<String> whitelist,
            Set<WhitelistRule> whitelistRules
    ) {
        String normalizedInput = normalizer.normalize(rawInput);
        Set<String> normalizedWhitelist = whitelistService.normalizeWhitelist(whitelist);
        Set<WhitelistRule> normalizedRules = whitelistService.normalizeWhitelistRules(whitelistRules);

        if (ngWords == null || ngWords.isEmpty()) {
            return new NgWordCheckResult(false, normalizedInput, null, null);
        }

        for (String rawNgWord : ngWords) {
            String normalizedNgWord = normalizer.normalize(rawNgWord);
            if (normalizedNgWord.isBlank()) {
                continue;
            }
            if (whitelistService.isWhitelisted(normalizedNgWord, normalizedWhitelist, normalizedRules)) {
                continue;
            }
            if (normalizedInput.contains(normalizedNgWord)) {
                return new NgWordCheckResult(true, normalizedInput, rawNgWord, normalizedNgWord);
            }
        }

        return new NgWordCheckResult(false, normalizedInput, null, null);
    }
}
