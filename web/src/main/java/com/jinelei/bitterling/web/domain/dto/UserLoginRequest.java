package com.jinelei.bitterling.web.domain.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "用户登录请求")
public class UserLoginRequest implements Serializable {
    @Schema(name = "用户名")
    private String username;
    @Schema(name = "密码")
    private String password;
    @Schema(name = "是否记住登录")
    Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserLoginRequest other = (UserLoginRequest) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LoginRequest [username=" + username + ", password=" + password + "]";
    }

}
