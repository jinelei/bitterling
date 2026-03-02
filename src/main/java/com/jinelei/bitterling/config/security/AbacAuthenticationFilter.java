package com.jinelei.bitterling.config.security;

import com.jinelei.bitterling.config.security.attribute.EnvironmentAttribute;
import com.jinelei.bitterling.config.security.attribute.ResourceAttribute;
import com.jinelei.bitterling.config.security.attribute.UserAttribute;
import com.jinelei.bitterling.utils.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbacAuthenticationFilter extends OncePerRequestFilter implements Ordered {
    private final AbacAuthorizer abacAuthorizer;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final UserDetailsService userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String sessionId = abacAuthorizer.getSessionId(request);
        final ResourceAttribute resourceAttribute = abacAuthorizer.getResourceAttribute(request);
        final EnvironmentAttribute environmentAttribute = abacAuthorizer.getEnvironmentAttribute(request);
        final UserAttribute userAttribute = abacAuthorizer.getUserAttribute(sessionId);
        if (abacAuthorizer.authorize(userAttribute, resourceAttribute, environmentAttribute)) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userAttribute.username());
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
            log.debug("abac filter url, {} >>> {}", request.getRequestURI(), userDetails.getUsername());
            redisTemplate.expire("session:" + sessionId, 2, TimeUnit.HOURS);
        }
        chain.doFilter(request, response);
    }


    @Override
    public int getOrder() {
        return 99;
    }
}