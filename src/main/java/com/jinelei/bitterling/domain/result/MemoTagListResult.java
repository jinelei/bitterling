package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.MemoTagResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SuppressWarnings("unused")
@Schema(description = "备忘列表返回结果")
public class MemoTagListResult extends CollectionResult<List<MemoTagResponse>> {
    public MemoTagListResult() {
    }

    public MemoTagListResult(List<MemoTagResponse> data) {
        super(data);
    }

    public MemoTagListResult(List<MemoTagResponse> data, Long total) {
        super(data, total);
    }

    public MemoTagListResult(Integer code, String message, List<MemoTagResponse> data) {
        super(code, message, data);
    }

    public MemoTagListResult(Integer code, String message, List<MemoTagResponse> data, Long total) {
        super(code, message, data, total);
    }
}