package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.CheckStatus;
import com.yourdomain.common.ngword.model.InputValidationResult;
import com.yourdomain.common.ngword.model.NgWordCheckOutcome;
import com.yourdomain.common.ngword.model.NgWordCheckResult;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Điều phối toàn bộ pipeline nghiệp vụ check NG theo luồng gRPC.
 */
public class NgWordCheckOrchestratorServiceImpl implements NgWordCheckOrchestratorService {

    private final InputValidationService validationService;
    private final NgWordRepository repository;
    private final NgWordCheckService checkService;

    /**
     * Khởi tạo orchestrator.
     *
     * @param validationService service validate input theo scope
     * @param repository repository đọc NG word và whitelist
     * @param checkService service so khớp NG word
     */
    public NgWordCheckOrchestratorServiceImpl(
            InputValidationService validationService,
            NgWordRepository repository,
            NgWordCheckService checkService) {
        this.validationService = validationService;
        this.repository = repository;
        this.checkService = checkService;
    }

    /**
     * Thực thi pipeline đầy đủ: validate -> merge whitelist -> check NG -> trả trạng thái.
     *
     * <p>Luồng xử lý:
     * 1) Validate input theo scope; nếu invalid thì trả {@code INVALID_INPUT}.
     * 2) Merge whitelist từ repository với whitelist inline từ caller.
     * 3) Merge whitelist rule từ repository với rule inline từ caller.
     * 4) Check input với tập NG word active và trả {@code OK} hoặc {@code NG}.
     *
     * @param rawInput input gốc từ request
     * @param scope scope nghiệp vụ dùng cho validate và load whitelist
     * @param inlineWhitelist whitelist token caller truyền thêm
     * @param inlineRules whitelist rule caller truyền thêm
     * @return kết quả nghiệp vụ tổng hợp gồm status, validation và kết quả check
     */
    public NgWordCheckOutcome check(String rawInput, String scope, Set<String> inlineWhitelist, Set<WhitelistRule> inlineRules) {
        InputValidationResult validation = validationService.validate(rawInput, scope);
        if (!validation.valid()) {
            return new NgWordCheckOutcome(CheckStatus.INVALID_INPUT, validation, new NgWordCheckResult(false, "", null, null));
        }

        Set<String> mergedWhitelist = new HashSet<>(repository.findWhitelistTokens(scope));
        if (inlineWhitelist != null) {
            mergedWhitelist.addAll(inlineWhitelist);
        }

        Set<WhitelistRule> mergedRules = new HashSet<>(repository.findWhitelistRules(scope));
        if (inlineRules != null) {
            mergedRules.addAll(inlineRules);
        }

        List<String> ngWords = repository.findActiveNgWords();
        NgWordCheckResult result = checkService.check(rawInput, ngWords, mergedWhitelist, mergedRules);
        CheckStatus status = result.ng() ? CheckStatus.NG : CheckStatus.OK;
        return new NgWordCheckOutcome(status, validation, result);
    }
}
