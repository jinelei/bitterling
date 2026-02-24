package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户信息单个返回结果")
public class UserInfoSingleResult extends GenericResult<UserInfoResponse> {
    public UserInfoSingleResult() {
    }

    public UserInfoSingleResult(UserInfoResponse data) {
        super(data);
    }

    public UserInfoSingleResult(Integer code, String message, UserInfoResponse data) {
        super(code, message, data);
    }
}