package com.jinelei.bitterling.web.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.web.domain.dto.UserLoginRequest;

import jakarta.validation.constraints.NotNull;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${bitterling.administrator.username:admin}")
    private String username;
    @Value("${bitterling.administrator.password:admin}")
    private String password;

    public void login(@NotNull UserLoginRequest request) {
        // mock
        final Optional<String> optUsername = Optional.ofNullable(request)
                .map(UserLoginRequest::getUsername)
                .filter(i -> StringUtils.hasLength(i));
        final Optional<String> optPassword = Optional.ofNullable(request)
                .map(UserLoginRequest::getPassword)
                .filter(i -> StringUtils.hasLength(i));
        if (optUsername.isEmpty() || optPassword.isEmpty()) {
            log.error("用户名或密码不存在: {}, {}", optUsername, optPassword);
            throw new BusinessException("用户名或密码不存在");
        }
        if (!optUsername.map(username::equals).orElse(false)
                || !optPassword.map(password::equals).orElse(false)) {
            log.error("用户名或密码不匹配: {}, {}", optUsername, optPassword);
            throw new BusinessException("用户名或密码不匹配");
        }
        log.info("用户登录成功: {}, {}", optUsername, optPassword);
    }

}
