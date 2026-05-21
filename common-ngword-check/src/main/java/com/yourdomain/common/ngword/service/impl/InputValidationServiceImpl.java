package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.InputValidationIssue;
import com.yourdomain.common.ngword.model.InputValidationResult;
import com.yourdomain.common.ngword.model.NgWordScopes;
import com.yourdomain.common.ngword.model.PatternRule;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Triển khai validate input theo danh sách pattern rule từ {@link PatternRuleProvider}.
 */
public class InputValidationServiceImpl implements InputValidationService {

    private final PatternRuleProvider patternRuleProvider;

    /**
     * Khởi tạo service validate input.
     *
     * @param patternRuleProvider provider trả về rule validate theo scope
     */
    public InputValidationServiceImpl(PatternRuleProvider patternRuleProvider) {
        this.patternRuleProvider = patternRuleProvider;
    }

    /**
     * Validate input theo scope và trả về toàn bộ lỗi vi phạm.
     *
     * <p>Luồng xử lý:
     * 1) Chuẩn hóa null input thành chuỗi rỗng để kiểm tra thống nhất.
     * 2) Duyệt toàn bộ rule theo scope và kiểm tra required/length/regex.
     * 3) Gom lỗi vào danh sách issue để caller phản hồi chi tiết cho client.
     *
     * @param rawInput text gốc từ request; có thể null
     * @param scope scope nghiệp vụ để lấy rule validate
     * @return kết quả validate gồm cờ hợp lệ và danh sách issue
     */
    @Override
    public InputValidationResult validate(String rawInput, String scope) {
        List<InputValidationIssue> issues = new ArrayList<>();
        String value = rawInput == null ? "" : rawInput;
        String effectiveScope = (scope == null || scope.isBlank()) ? NgWordScopes.DEFAULT : scope;

        for (PatternRule rule : patternRuleProvider.getRules(effectiveScope)) {
            if (rule.required() && value.isBlank()) {
                issues.add(new InputValidationIssue("REQUIRED", "Input is required", value));
                continue;
            }
            if (rule.minLength() != null && value.length() < rule.minLength()) {
                issues.add(new InputValidationIssue("MIN_LENGTH", "Input length is below minimum", value));
            }
            if (rule.maxLength() != null && value.length() > rule.maxLength()) {
                issues.add(new InputValidationIssue("MAX_LENGTH", "Input length exceeds maximum", value));
            }
            if (rule.regex() != null && !rule.regex().isBlank() && !Pattern.compile(rule.regex()).matcher(value).matches()) {
                issues.add(new InputValidationIssue("PATTERN_MISMATCH", "Input does not match rule " + rule.ruleName(), value));
            }
        }

        return new InputValidationResult(issues.isEmpty(), List.copyOf(issues));
    }
}
