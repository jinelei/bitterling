package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.MemoResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("unused")
@Schema(description = "备忘单个返回结果")
public class MemoSingleResult extends GenericResult<MemoResponse> {
    public MemoSingleResult() {
    }

    public MemoSingleResult(MemoResponse data) {
        super(data);
    }

    public MemoSingleResult(Integer code, String message, MemoResponse data) {
        super(code, message, data);
    }
}