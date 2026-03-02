package com.jinelei.bitterling.config.security.attribute;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResourceAttribute", description = "资源属性")
public record ResourceAttribute(
        @Schema(name = "id", description = "资源ID")
        String id,
        @Schema(name = "type", description = "资源类型")
        String type,
        @Schema(name = "owner", description = "所属用户")
        String owner,
        @Schema(name = "securityLevel", description = "密级")
        String securityLevel
) {
}
