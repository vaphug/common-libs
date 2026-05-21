package com.yourdomain.common.ngword.service.impl;

import com.yourdomain.common.ngword.model.PatternRule;
import com.yourdomain.common.ngword.service.PatternRuleProvider;
import java.util.List;

/**
 * Cung cấp bộ pattern validate mặc định cho luồng check NG.
 */
public class PatternRuleProviderImpl implements PatternRuleProvider {

    /**
     * Lấy rule validate mặc định theo scope.
     *
     * @param scope scope nghiệp vụ hoặc màn hình. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     *              Nếu null/blank thì caller nên chuẩn hóa về {@code "default"}.
     * @return danh sách rule validate gồm required, length và pattern ký tự cơ bản
     */
    @Override
    public List<PatternRule> getRules(String scope) {
        return List.of(
                new PatternRule("required", null, true, 1, 2000),
                new PatternRule("basic-printable", "^[\\p{L}\\p{N}\\p{P}\\p{Zs}ぁ-ゟ゠-ヿ一-龯]*$", false, null, null));
    }
}
