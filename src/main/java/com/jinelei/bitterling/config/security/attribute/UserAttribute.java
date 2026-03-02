package com.jinelei.bitterling.config.security.attribute;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

@Schema(name = "UserAttribute", description = "用户属性")
public record UserAttribute(
        @Schema(name = "username", description = "用户名")
        String username,
        @Schema(name = "roles", description = "角色集合")
        Set<String> roles,
        @Schema(name = "permissions", description = "角色集合")
        Set<String> permissions,
        @Schema(name = "tempAuthResourceList", description = "临时授权资源集合")
        List<String> tempAuthResourceList,
        @Schema(name = "loginDevice", description = "登录设备")
        String loginDevice
) {
}
