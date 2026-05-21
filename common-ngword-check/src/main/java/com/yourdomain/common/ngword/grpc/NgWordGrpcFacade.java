package com.yourdomain.common.ngword.grpc;

/**
 * Facade cho API gRPC nghiệp vụ kiểm tra NG word.
 */
public interface NgWordGrpcFacade {

    /**
     * Xử lý request check NG word theo pipeline nghiệp vụ đầy đủ.
     *
     * @param request request gRPC chứa input, scope và whitelist tùy chọn. Ví dụ: input={@code "abc@1"}, scope={@code "default"}.
     * @return response gRPC chứa status, kết quả NG và thông tin match
     */
    CheckNgWordResponse checkNgWord(CheckNgWordRequest request);
}
