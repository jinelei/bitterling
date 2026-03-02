package com.jinelei.bitterling.config.security;

import com.jinelei.bitterling.config.security.attribute.AbacAuthRequest;
import com.jinelei.bitterling.config.security.attribute.EnvironmentAttribute;
import com.jinelei.bitterling.config.security.attribute.ResourceAttribute;
import com.jinelei.bitterling.config.security.attribute.UserAttribute;
import com.jinelei.bitterling.utils.AbacRedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AbacAuthorizer {
    private final AbacRedisUtil abacRedisUtil;

    /**
     * 核心授权方法：判断用户是否有权限操作资源
     */
    public boolean authorize(AbacAuthRequest request) {
        // 1. 从Redis读取最新属性（实时性保障）
        UserAttribute userAttr = abacRedisUtil.getUserAttribute(request.sessionId());
        ResourceAttribute resourceAttr = abacRedisUtil.getResourceAttribute(request.resourceId());
        EnvironmentAttribute envAttr = request.envAttr();

        // 基础校验：属性不存在直接拒绝
        if (userAttr == null || resourceAttr == null || envAttr == null) {
            return false;
        }

        // 2. 执行ABAC规则判断（组合多维度规则）
        return checkAdminRule(userAttr, resourceAttr)          // 管理员规则
                || checkOwnerRule(userAttr, resourceAttr)       // 资源归属者规则
                || checkTempAuthRule(userAttr, resourceAttr)    // 临时授权规则
                || checkEnvRule(userAttr, envAttr);             // 环境规则
    }

    // 规则1：管理员可操作本部门所有资源
    private boolean checkAdminRule(UserAttribute userAttr, ResourceAttribute resourceAttr) {
        return Optional.ofNullable(userAttr.roles()).map(s -> s.contains("admin")).orElse(false);
    }

    // 规则2：资源归属者可操作自己的资源
    private boolean checkOwnerRule(UserAttribute userAttr, ResourceAttribute resourceAttr) {
        return userAttr.username().equals(resourceAttr.owner());
    }

    // 规则3：临时授权资源可操作
    private boolean checkTempAuthRule(UserAttribute userAttr, ResourceAttribute resourceAttr) {
        return userAttr.tempAuthResourceList() != null && userAttr.tempAuthResourceList().contains(resourceAttr.id());
    }

    // 规则4：普通用户在办公IP+工作时间可操作公开资源
    private boolean checkEnvRule(UserAttribute userAttr, EnvironmentAttribute envAttr) {
        // 检查IP白名单
        boolean isIpValid = abacRedisUtil.isIpInWhiteList(envAttr.ip());
        // 检查工作时间（9:00-18:00）
        LocalDateTime now = envAttr.time();
        boolean isTimeValid = now.toLocalTime().isAfter(LocalTime.of(9, 0)) && now.toLocalTime().isBefore(LocalTime.of(18, 0));
        // 普通用户+IP有效+时间有效 → 允许
        return userAttr.roles().contains("user") && isIpValid && isTimeValid;
    }

    // 进阶：使用EL表达式执行动态规则（适合复杂规则）
    public boolean authorizeByEl(AbacAuthRequest request, String ruleEl) {
        UserAttribute userAttr = abacRedisUtil.getUserAttribute(request.sessionId());
        ResourceAttribute resourceAttr = abacRedisUtil.getResourceAttribute(request.resourceId());

        // 构建EL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", userAttr);
        context.setVariable("resource", resourceAttr);
        context.setVariable("env", request.envAttr());

        // 执行EL表达式（示例规则：user.role == 'admin' && user.dept == resource.dept）
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(ruleEl);
        return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }
}