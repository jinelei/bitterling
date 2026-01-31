package com.jinelei.bitterling.web.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 *
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2026-01-31
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserInfoResponse", description = "用户信息")
public class UserInfoResponse implements Serializable {
    @Schema(name = "username", description = "用户名称")
    private String username;
    @Schema(name = "permissions", description = "权限集合")
    private Set<String> permissions;
    @Schema(name = "roles", description = "角色集合")
    private Set<String> roles;
}
