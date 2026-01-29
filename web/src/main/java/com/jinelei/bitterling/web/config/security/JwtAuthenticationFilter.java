package com.jinelei.bitterling.web.config.security;

import com.jinelei.bitterling.web.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
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
import java.util.function.Supplier;

/**
 * JWT 认证过滤器：每次请求拦截 Token 并完成身份校验
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String BEARER_ = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final Supplier<Boolean> isAnonymous;

    // 构造器注入（Spring 6.x 推荐）
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.isAnonymous = () -> Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).map(ANONYMOUS_USER::equals).orElse(true);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        log.info("===== JWT 过滤器开始处理请求：{} =====", request.getRequestURI());
        final String authorization = request.getHeader(AUTHORIZATION);
        String username = null;
        String jwtToken = null;

        if (authorization != null && authorization.startsWith(BEARER_)) {
            jwtToken = authorization.substring(BEARER_.length());
            username = jwtTokenUtil.extractUsername(jwtToken);
        }
        if (username != null && isAnonymous.get()) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                log.info("用户登录成功[JWT]: {}", token.getPrincipal());
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 99;
    }
}
