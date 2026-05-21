package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NgWordCheckResult;
import com.yourdomain.common.ngword.model.WhitelistRule;
import java.util.Set;

/**
 * Adapter dùng dữ liệu NG/whitelist từ repository để thực hiện check.
 */
public class NgWordDbCheckServiceImpl implements NgWordDbCheckService {

    private final NgWordRepository repository;
    private final NgWordCheckService checkService;

    /**
     * Khởi tạo DB check service.
     *
     * @param repository repository dữ liệu NG/whitelist
     * @param checkService service so khớp NG word
     */
    public NgWordDbCheckServiceImpl(NgWordRepository repository, NgWordCheckService checkService) {
        this.repository = repository;
        this.checkService = checkService;
    }

    /**
     * Check NG dựa trên whitelist caller truyền trực tiếp.
     *
     * @param rawInput input gốc cần kiểm tra
     * @param whitelist whitelist token inline
     * @param whitelistRules whitelist rule inline
     * @return kết quả check NG
     */
    @Override
    public NgWordCheckResult checkAgainstDb(String rawInput, Set<String> whitelist, Set<WhitelistRule> whitelistRules) {
        return checkService.check(rawInput, repository.findActiveNgWords(), whitelist, whitelistRules);
    }

    /**
     * Check NG theo scope, whitelist được lấy từ repository.
     *
     * @param rawInput input gốc cần kiểm tra
     * @param scope scope nghiệp vụ để truy xuất whitelist
     * @return kết quả check NG
     */
    @Override
    public NgWordCheckResult checkAgainstDbByScope(String rawInput, String scope) {
        return checkService.check(
                rawInput,
                repository.findActiveNgWords(),
                repository.findWhitelistTokens(scope),
                repository.findWhitelistRules(scope));
    }
}
