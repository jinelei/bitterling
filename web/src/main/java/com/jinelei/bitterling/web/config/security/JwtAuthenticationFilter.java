package com.jinelei.bitterling.web.config.security;

import com.jinelei.bitterling.web.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT 认证过滤器：每次请求拦截 Token 并完成身份校验
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    // 构造器注入（Spring 6.x 推荐）
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.info("===== JWT 过滤器开始处理请求：{} =====", request.getRequestURI());
        // 1. 从请求头提取 Token（前端通常放在 Authorization 头，格式：Bearer <token>）
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // 校验 Authorization 头格式
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // 截取 Bearer 后的 Token
            username = jwtTokenUtil.extractUsername(jwtToken);
        }

        // 2. Token 存在且 SecurityContext 未认证
        if (username != null && Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).map("anonymousUser"::equals).orElse(true)) {
            // 3. 加载用户详情
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. 验证 Token 有效性
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                // 5. 创建认证 Token 并放入 SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 核心：将认证信息存入上下文，后续接口可通过 SecurityContext 获取用户信息
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 继续执行过滤器链
        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 99;
    }
}
