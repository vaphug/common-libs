package com.yourdomain.common.validation.ngword;

import java.util.Set;

/**
 * Orchestrates DB-backed NG word checking.
 */
public class NgWordDbCheckService {

    private final NgWordRepository repository;
    private final NgWordCheckService checkService;

    public NgWordDbCheckService(NgWordRepository repository, NgWordCheckService checkService) {
        this.repository = repository;
        this.checkService = checkService;
    }

    public NgWordCheckResult check(String input) {
        return check(input, Set.of());
    }

    public NgWordCheckResult check(String input, Set<String> whitelist) {
        return checkService.check(input, repository.findActiveNgWords(), whitelist);
    }
}
