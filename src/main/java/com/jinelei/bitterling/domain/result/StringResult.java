package com.jinelei.bitterling.domain.result;

import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("unused")
@Schema(description = "字符串返回结果")
public class StringResult extends GenericResult<String> {
    public StringResult() {
        super();
    }

    public StringResult(String data) {
        super(data);
    }

    public StringResult(Integer code, String message, String data) {
        super(code, message, data);
    }
}