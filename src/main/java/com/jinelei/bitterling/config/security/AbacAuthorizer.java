package com.jinelei.bitterling.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.bitterling.config.security.attribute.EnvironmentAttribute;
import com.jinelei.bitterling.config.security.attribute.ResourceAttribute;
import com.jinelei.bitterling.config.security.attribute.UserAttribute;
import com.jinelei.bitterling.constant.PermissionConstants;
import com.jinelei.bitterling.utils.MD5Util;
import com.jinelei.bitterling.utils.NetworkUtil;
import com.jinelei.bitterling.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbacAuthorizer {
    public static final String BEARER_ = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER = "user";
    public static final String USERNAME = "username";
    public static final String SESSION_S = "session:%s";
    public static final String USER_ATTR_S = "user:attr:%s";
    public static final String PERMISSIONS = "permissions";
    public static final String TEMP_AUTH_RESOURCE_LIST = "tempAuthResourceList";
    public static final String LOGIN_DEVICE = "loginDevice";
    public static final String ROLES = "roles";
    public static final String CLIENT_IP = "clientIp";
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 用户登录
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 授权对象
     * @return 登录是否成功
     */
    public boolean userLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws JsonProcessingException {
        final Optional<String> username = Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername)
                .filter(StringUtils::hasLength);
        if (username.isEmpty()) {
            return false;
        }
        final List<String> roles = Optional.of(authentication.getAuthorities())
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(i -> i.startsWith("ROLE_"))
                .map(i -> i.substring(5))
                .toList();
        final List<String> permissions = Optional.of(authentication.getAuthorities())
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(i -> !i.startsWith("ROLE_"))
                .map(i -> i.substring(5))
                .toList();
        final String clientIp = RequestUtil.getClientIp(request);
        final String deviceType = RequestUtil.getDeviceType(request);
        final String sessionId = username.map(MD5Util::encrypt).orElse("");
        redisTemplate.opsForHash().putAll(String.format(SESSION_S, sessionId),
                Map.of(USERNAME, username.get(),
                        LOGIN_DEVICE, deviceType,
                        CLIENT_IP, clientIp
                ));
        redisTemplate.opsForHash().putAll(String.format(USER_ATTR_S, username.get()),
                Map.of(USERNAME, username.get(),
                        ROLES, objectMapper.writeValueAsString(roles),
                        PERMISSIONS, objectMapper.writeValueAsString(permissions),
                        LOGIN_DEVICE, deviceType
                ));
        return redisTemplate.expire(String.format(SESSION_S, sessionId), 2, TimeUnit.HOURS);
    }

    /**
     * 用户登出
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 授权对象
     * @return 登出是否成功
     */
    public boolean userLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional.ofNullable(getSessionId(request))
                .filter(StringUtils::hasText)
                .ifPresent(sessionId -> {
                    redisTemplate.delete(String.format(SESSION_S, sessionId));
                    Optional.ofNullable(getUserAttribute(sessionId))
                            .map(UserAttribute::username)
                            .ifPresent(username -> redisTemplate.delete(String.format(USER_ATTR_S, username)));
                });
        return true;
    }

    /**
     * 核心授权方法：判断用户是否有权限操作资源
     *
     * @param userAttr     用户属性
     * @param resourceAttr 资源属性
     * @param envAttr      环境属性
     * @return 是否授权
     */
    public boolean authorize(UserAttribute userAttr, ResourceAttribute resourceAttr, EnvironmentAttribute envAttr) {
        boolean valid = false;
        valid = checkAdminRule(userAttr, resourceAttr);
        if (valid) {
            return valid;
        }
        valid = checkOwnerRule(userAttr, resourceAttr);
        if (valid) {
            return valid;
        }
        valid = checkTempAuthRule(userAttr, resourceAttr);
        if (valid) {
            return valid;
        }
//        valid = checkEnvRule(userAttr, envAttr);
        return valid;
    }


    /**
     * 进阶：使用EL表达式执行动态规则（适合复杂规则）
     *
     * @param userAttr     用户属性
     * @param resourceAttr 资源属性
     * @param envAttr      环境属性
     * @param ruleEl       EL表达式
     * @return 是否授权
     */
    public boolean authorizeByEl(UserAttribute userAttr, ResourceAttribute resourceAttr, EnvironmentAttribute envAttr, String ruleEl) {

        // 构建EL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", userAttr);
        context.setVariable("resource", resourceAttr);
        context.setVariable("env", envAttr);

        // 执行EL表达式（示例规则：user.role == 'admin' && user.dept == resource.dept）
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(ruleEl);
        return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }

    /**
     * 规则：管理员可操作本部门所有资源
     *
     * @param userAttr     用户属性
     * @param resourceAttr 资源属性
     * @return 是否授权
     */
    private boolean checkAdminRule(UserAttribute userAttr, ResourceAttribute resourceAttr) {
        return Optional.ofNullable(userAttr).map(UserAttribute::roles).map(s -> s.contains(PermissionConstants.ADMIN)).orElse(false);
    }

    /**
     * 规则：资源归属者可操作自己的资源
     *
     * @param userAttr     用户属性
     * @param resourceAttr 资源属性
     * @return 是否授权
     */
    private boolean checkOwnerRule(UserAttribute userAttr, ResourceAttribute resourceAttr) {
        return Optional.ofNullable(userAttr).map(UserAttribute::username)
                .map(s -> Optional.ofNullable(resourceAttr).map(ResourceAttribute::owner).map(s::equals).orElse(false))
                .orElse(false);
    }

    /**
     * 规则3：临时授权资源可操作
     *
     * @param userAttr     用户属性
     * @param resourceAttr 资源属性
     * @return 是否授权
     */
    private boolean checkTempAuthRule(UserAttribute userAttr, ResourceAttribute resourceAttr) {
        return Optional.ofNullable(userAttr)
                .map(UserAttribute::tempAuthResourceList)
                .map(s -> Optional.ofNullable(resourceAttr).map(ResourceAttribute::id).map(s::contains).orElse(false))
                .orElse(false);
    }

    /**
     * 规则：普通用户在办公IP+工作时间可操作公开资源
     *
     * @param userAttr 用户属性
     * @param envAttr  环境属性
     * @return 是否授权
     */
    private boolean checkEnvRule(UserAttribute userAttr, EnvironmentAttribute envAttr) {
        boolean matched = false;
        matched = isIpInWhiteList(envAttr.ip());
        if (matched && Optional.ofNullable(userAttr).isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * 从请求头中获取SessionId
     *
     * @param request 请求对象
     * @return SessionId
     */
    public String getSessionId(HttpServletRequest request) {
        final String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null && authorization.startsWith(BEARER_)) {
            return authorization.substring(BEARER_.length());
        } else {
            return authorization;
        }
    }

    /**
     * 从请求中获取资源属性
     *
     * @param request 请求对象
     * @return 资源属性
     */
    public ResourceAttribute getResourceAttribute(HttpServletRequest request) {
        String resourceId = request.getRequestURI();
        return new ResourceAttribute("", "", "", "");
    }

    /**
     * 从请求中获取环境属性
     *
     * @param request 请求对象
     * @return 环境属性
     */
    public EnvironmentAttribute getEnvironmentAttribute(HttpServletRequest request) {
        return new EnvironmentAttribute(RequestUtil.getClientIp(request), LocalDateTime.now(), RequestUtil.getDeviceType(request));
    }


    /**
     * 从Redis获取用户全量属性（会话属性+扩展属性）
     *
     * @param sessionId 会话ID
     * @return 用户全量属性
     */
    public UserAttribute getUserAttribute(String sessionId) {
        final Map<Object, Object> sessionMap = redisTemplate.opsForHash().entries(String.format(SESSION_S, sessionId));
        if (sessionMap.isEmpty()) {
            return null;
        }
        final String username = Optional.ofNullable(sessionMap.get(USERNAME)).map(Object::toString).orElse(null);
        final Map<Object, Object> userAttributeMap = redisTemplate.opsForHash().entries(String.format(USER_ATTR_S, username));
        final Set<String> roles = Optional.ofNullable(userAttributeMap.get(ROLES))
                .map(Object::toString)
                .map(s -> {
                    try {
                        return objectMapper.readValue(s, new TypeReference<Set<String>>() {
                        });
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                }).orElse(null);
        final Set<String> permissions = Optional.ofNullable(userAttributeMap.get(PERMISSIONS))
                .map(Object::toString)
                .map(s -> {
                    try {
                        return objectMapper.readValue(s, new TypeReference<Set<String>>() {
                        });
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                }).orElse(null);
        final List<String> tempAuthResourceList = Optional.ofNullable(userAttributeMap.get(TEMP_AUTH_RESOURCE_LIST))
                .map(Object::toString)
                .map(s -> {
                    try {
                        return objectMapper.readValue(s, new TypeReference<List<String>>() {
                        });
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                }).orElse(null);
        final String loginDevice = Optional.ofNullable(userAttributeMap.get(LOGIN_DEVICE))
                .map(Object::toString)
                .orElse(null);
        return new UserAttribute(username, roles, permissions, tempAuthResourceList, loginDevice);
    }

    /**
     * 检查IP是否在白名单
     */
    public boolean isIpInWhiteList(String ip) {
        Set<@NonNull String> members = redisTemplate.opsForSet().members("env:ip_white_list");
        return members.stream().anyMatch(s -> NetworkUtil.matchIp(ip, s));
    }


}