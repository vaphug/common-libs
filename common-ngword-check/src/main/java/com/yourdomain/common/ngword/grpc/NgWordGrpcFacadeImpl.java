package com.yourdomain.common.ngword.grpc;

import com.yourdomain.common.ngword.model.NgWordCheckOutcome;
import com.yourdomain.common.ngword.service.NgWordCheckOrchestratorService;
import java.util.List;

/**
 * Triển khai facade gRPC mặc định cho nghiệp vụ check NG word.
 */
public class NgWordGrpcFacadeImpl implements NgWordGrpcFacade {

    private final NgWordCheckOrchestratorService orchestrator;

    /**
     * Khởi tạo facade gRPC.
     *
     * @param orchestrator orchestrator điều phối pipeline check NG
     */
    public NgWordGrpcFacadeImpl(NgWordCheckOrchestratorService orchestrator) {
        this.orchestrator = orchestrator;
    }

    /**
     * Thực thi check NG và chuyển đổi outcome nghiệp vụ về response gRPC.
     *
     * @param request request gRPC chứa input, scope và whitelist tùy chọn
     * @return response gRPC gồm status, kết quả NG và danh sách lỗi validate (nếu có)
     */
    @Override
    public CheckNgWordResponse checkNgWord(CheckNgWordRequest request) {
        if (request == null) {
            return new CheckNgWordResponse(
                    com.yourdomain.common.ngword.model.CheckStatus.INVALID_INPUT,
                    false,
                    "",
                    null,
                    null,
                    List.of());
        }

        NgWordCheckOutcome outcome = orchestrator.check(
                request.input(),
                request.scope(),
                request.inlineWhitelist(),
                request.inlineWhitelistRules());

        return new CheckNgWordResponse(
                outcome.status(),
                outcome.checkResult().ng(),
                outcome.checkResult().normalizedInput(),
                outcome.checkResult().matchedRawNgWord(),
                outcome.checkResult().matchedNormalizedNgWord(),
                outcome.validation().issues());
    }
}
