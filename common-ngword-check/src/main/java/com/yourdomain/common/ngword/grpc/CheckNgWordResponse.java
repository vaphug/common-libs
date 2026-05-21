package com.yourdomain.common.ngword.grpc;

import com.yourdomain.common.ngword.model.CheckStatus;
import com.yourdomain.common.ngword.model.InputValidationIssue;
import java.util.List;

/**
 * Response chuẩn cho API gRPC kiểm tra NG word.
 *
 * @param status trạng thái nghiệp vụ tổng thể: OK, NG hoặc INVALID_INPUT
 * @param ng cờ true nếu phát hiện NG word
 * @param normalizedInput chuỗi input sau chuẩn hóa để đối soát
 * @param matchedRawNgWord NG word gốc khớp trong DB
 * @param matchedNormalizedNgWord NG word sau normalize được dùng để match
 * @param validationIssues danh sách lỗi validate đầu vào; rỗng nếu input hợp lệ
 */
public record CheckNgWordResponse(
        CheckStatus status,
        boolean ng,
        String normalizedInput,
        String matchedRawNgWord,
        String matchedNormalizedNgWord,
        List<InputValidationIssue> validationIssues
) {
}
