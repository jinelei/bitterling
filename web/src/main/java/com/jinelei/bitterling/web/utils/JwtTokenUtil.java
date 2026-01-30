package com.jinelei.bitterling.web.utils;

import com.jinelei.bitterling.core.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

/**
 * JWT 工具类：生成、解析、验证 Token
 */
@Slf4j
@Component
public class JwtTokenUtil implements InitializingBean {
    private SecretKey secretKey;
    private JwtParser jwtParser;
    @Value("${bitterling.jwt.secret:your-secret-key-32bytes-long-1234567890}")
    private String secret;
    @Value("${jwt.expiration:3600000}")
    private long expiration;

    private final Function<String, Claims> getClaim = token -> Optional.ofNullable(token)
            .map(String::trim)
            .filter(StringUtils::hasLength)
            .map(s -> {
                try {
                    return jwtParser.parseSignedClaims(s);
                } catch (Exception e) {
                    log.error("解析jwt失败: {}", e.getMessage());
                    return null;
                }
            })
            .map(Jwt::getPayload)
            .orElse(null);

    public <T> T resolveClaimObject(String token, Function<Claims, T> claimsResolver) {
        return Optional.ofNullable(token)
                .map(getClaim)
                .map(claimsResolver)
                .orElse(null);
    }

    public String resolveUsername(String token) {
        return resolveClaimObject(token, Claims::getSubject);
    }

    public Date resolveExpiration(String token) {
        return resolveClaimObject(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, String destUsername) {
        final String username = resolveUsername(token);
        return (username.equals(destUsername) && !resolveExpiration(token).before(new Date()));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public void afterPropertiesSet() {
        this.secretKey = Optional.ofNullable(secret)
                .map(String::getBytes)
                .map(Keys::hmacShaKeyFor)
                .orElseThrow(() -> new BusinessException("创建Jwt密钥失败"));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }
}
