package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.MemoTagResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SuppressWarnings("unused")
@Schema(description = "备忘标签分页返回结果")
public class MemoTagPageResult extends PageableResult<List<MemoTagResponse>> {
    public MemoTagPageResult() {
    }

    public MemoTagPageResult(List<MemoTagResponse> data, Long total, Integer pageNo, Integer pageSize) {
        super(data, total, pageNo, pageSize);
    }

    public MemoTagPageResult(Integer code, String message, List<MemoTagResponse> data, Long total, Integer pageNo, Integer pageSize) {
        super(code, message, data, total, pageNo, pageSize);
    }
}
