package com.jinelei.bitterling.web.controller;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.domain.result.GenericResult;
import com.jinelei.bitterling.web.domain.dto.UserInfoResponse;
import com.jinelei.bitterling.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户控制器
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2026-01-31
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController extends BaseController {
    private final UserService userService;

    @PostMapping("/info")
    @Operation(operationId = "getUserInfo", summary = "用户信息", description = "获取当前登录用户相关信息")
    public GenericResult<UserInfoResponse> getUserInfo(Principal principal) {
        UserInfoResponse response = userService.getUserInfo(principal);
        return GenericResult.success(response);
    }

}
