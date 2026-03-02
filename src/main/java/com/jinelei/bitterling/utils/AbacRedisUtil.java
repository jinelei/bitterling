package com.jinelei.bitterling.utils;

import com.jinelei.bitterling.config.security.attribute.ResourceAttribute;
import com.jinelei.bitterling.config.security.attribute.UserAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AbacRedisUtil {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 从Redis获取用户全量属性（会话属性+扩展属性）
     */
    public UserAttribute getUserAttribute(String sessionId) {
        // 1. 读取会话中的基础属性
        final Map<Object, Object> sessionMap = redisTemplate.opsForHash().entries(String.format("session:%s", sessionId));
        if (sessionMap.isEmpty()) {
            return null;
        }
        // 2. 读取用户扩展属性
        final String username = Optional.ofNullable(sessionMap.get("username")).map(Object::toString).orElse(null);
        final Map<Object, Object> userAttributeMap = redisTemplate.opsForHash().entries(String.format("user:attr:%s", username));
        // 3. 合并为UserAttribute对象
        final Set<String> roles = Optional.ofNullable(userAttributeMap.get("roles"))
                .map(Object::toString)
                .map(s -> objectMapper.readValue(s, new TypeReference<Set<String>>() {
                })).orElse(null);
        final Set<String> permissions = Optional.ofNullable(userAttributeMap.get("permissions"))
                .map(Object::toString)
                .map(s -> objectMapper.readValue(s, new TypeReference<Set<String>>() {
                })).orElse(null);
        final List<String> tempAuthResourceList = Optional.ofNullable(userAttributeMap.get("tempAuthResourceList"))
                .map(Object::toString)
                .map(s -> objectMapper.readValue(s, new TypeReference<List<String>>() {
                })).orElse(null);
        final String loginDevice = Optional.ofNullable(userAttributeMap.get("loginDevice"))
                .map(Object::toString)
                .orElse(null);
        return new UserAttribute(username, roles, permissions, tempAuthResourceList, loginDevice);

    }

    /**
     * 从Redis获取资源属性
     */
    public ResourceAttribute getResourceAttribute(String resourceId) {
        final Map<Object, Object> resourceMap = redisTemplate.opsForHash().entries(String.format("resource:attr:%s", resourceId));
        if (resourceMap.isEmpty()) {
            return null;
        }
        final String id = Optional.ofNullable(resourceMap.get("resourceId"))
                .map(Object::toString)
                .orElse(null);
        final String type = Optional.ofNullable(resourceMap.get("type"))
                .map(Object::toString)
                .orElse(null);
        final String owner = Optional.ofNullable(resourceMap.get("owner"))
                .map(Object::toString)
                .orElse(null);
        final String securityLevel = Optional.ofNullable(resourceMap.get("securityLevel"))
                .map(Object::toString)
                .orElse(null);
        return new ResourceAttribute(id, type, owner, securityLevel);
    }

    /**
     * 检查IP是否在白名单
     */
    public boolean isIpInWhiteList(String ip) {
        return redisTemplate.opsForSet().isMember("env:ip_white_list", ip);
    }
}