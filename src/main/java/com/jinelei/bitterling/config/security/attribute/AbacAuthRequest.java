package com.jinelei.bitterling.config.security.attribute;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AbacAuthRequest", description = "ABAC权限请求")
public record AbacAuthRequest(
        @Schema(name = "sessionId", description = "会话ID")
        String sessionId,
        @Schema(name = "resourceId", description = "资源ID")
        String resourceId,
        @Schema(name = "action", description = "操作")
        String action,
        @Schema(name = "envAttr", description = "环境属性")
        EnvironmentAttribute envAttr
) {
}