package com.jinelei.bitterling.config.security.attribute;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "EnvironmentAttribute", description = "环境信息")
public record EnvironmentAttribute(
        @Schema(name = "ip", description = "客户端IP")
        String ip,
        @Schema(name = "time", description = "请求时间")
        LocalDateTime time,
        @Schema(name = "device", description = "客户端设备")
        String device) {
}