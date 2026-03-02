package com.jinelei.bitterling.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.bitterling.config.security.attribute.AbacAuthRequest;
import com.jinelei.bitterling.config.security.attribute.EnvironmentAttribute;
import com.jinelei.bitterling.domain.result.GenericResult;
import com.jinelei.bitterling.domain.result.ResultFactory;
import com.jinelei.bitterling.domain.result.StringResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AbacInterceptor implements HandlerInterceptor {
    private final AbacAuthorizer abacAuthorizer;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 提取请求中的核心信息
        String sessionId = getSessionId(request); // 从Cookie/请求头获取SessionID
        String resourceId = request.getRequestURI(); // 资源ID = 接口路径
        String action = request.getMethod(); // 操作 = 请求方法（GET/POST）

        // 2. 构建环境属性
        EnvironmentAttribute envAttr = new EnvironmentAttribute(request.getRemoteAddr(), LocalDateTime.now(), request.getHeader("Device"));

        // 3. 构建授权请求
        AbacAuthRequest authRequest = new AbacAuthRequest(sessionId, resourceId, action, envAttr);

        // 4. 执行ABAC授权判断
        boolean hasPermission = abacAuthorizer.authorize(authRequest);
        if (!hasPermission) {
            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");
            StringResult message = ResultFactory.create(StringResult.class, GenericResult.CODE_FAILURE_UNAUTHORIZED, GenericResult.USER_NOT_LOGGED_IN, GenericResult.LOGIN_FAILED);
            response.setStatus(HttpStatus.OK.value());
            objectMapper.writeValue(response.getWriter(), message);
            return false;
        }

        // 5. 会话续期（无感续期）
        redisTemplate.expire("session:" + sessionId, 2, TimeUnit.HOURS);
        return true;
    }

    // 从Cookie/请求头获取SessionID
    private String getSessionId(HttpServletRequest request) {
        // 优先从Cookie获取（推荐）
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("Session-ID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        // 备用：从请求头获取
        return request.getHeader("Session-ID");
    }
}