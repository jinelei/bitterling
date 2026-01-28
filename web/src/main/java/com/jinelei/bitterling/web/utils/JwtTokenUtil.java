package com.jinelei.bitterling.web.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 工具类：生成、解析、验证 Token
 */
@Component
public class JwtTokenUtil {
    // 密钥（生产环境建议配置在 yaml 中，且足够复杂）
    @Value("${jwt.secret:your-secret-key-32bytes-long-1234567890}")
    private String secret;

    // Token 过期时间（3600秒 = 1小时）
    @Value("${jwt.expiration:3600000}")
    private long expiration;

    // 生成签名密钥
    private SecretKey getSigningKey() {
        // 密钥长度至少 256 位（32 字节），否则会报错
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 从 Token 中提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从 Token 中提取过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 通用提取 Claim 方法
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析 Token 获取所有 Claim
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 验证 Token 是否过期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 生成 Token（基于用户信息）
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // 生成 Token（基于用户信息）
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // 构建 Token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)          // 自定义载荷
                .subject(subject)        // 主题（用户名）
                .issuedAt(new Date(System.currentTimeMillis())) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(getSigningKey()) // 签名
                .compact();
    }

    // 验证 Token 有效性（用户名匹配 + 未过期）
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
