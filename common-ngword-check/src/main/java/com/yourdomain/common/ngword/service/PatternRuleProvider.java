package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.PatternRule;
import java.util.List;

/**
 * Cung cấp danh sách pattern rule dùng để validate input trước khi check NG.
 */
public interface PatternRuleProvider {

    /**
     * Lấy danh sách rule theo scope nghiệp vụ.
     *
     * @param scope phạm vi nghiệp vụ hoặc màn hình đang gọi check. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     * @return danh sách rule validate áp dụng cho scope tương ứng
     */
    List<PatternRule> getRules(String scope);
}
