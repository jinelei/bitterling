package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.MemoResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SuppressWarnings("unused")
@Schema(description = "备忘分页返回结果")
public class MemoPageResult extends PageableResult<List<MemoResponse>> {
    public MemoPageResult() {
    }

    public MemoPageResult(List<MemoResponse> data, Long total, Integer pageNo, Integer pageSize) {
        super(data, total, pageNo, pageSize);
    }

    public MemoPageResult(Integer code, String message, List<MemoResponse> data, Long total, Integer pageNo, Integer pageSize) {
        super(code, message, data, total, pageNo, pageSize);
    }
}
