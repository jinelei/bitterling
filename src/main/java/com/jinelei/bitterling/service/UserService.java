package com.jinelei.bitterling.service;

import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.domain.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserInfoResponse getUserInfo(Principal principal) {
        UsernamePasswordAuthenticationToken authorization = Optional.ofNullable(principal)
                .filter(i -> i instanceof UsernamePasswordAuthenticationToken)
                .map(UsernamePasswordAuthenticationToken.class::cast)
                .orElseThrow(() -> new BusinessException("获取当前登录用户失败"));
        return getUserInfo(authorization);
    }

    public UserInfoResponse getUserInfo(Authentication authentication) {
        final UserInfoResponse response = new UserInfoResponse();
        response.setPermissions(new HashSet<>());
        response.setRoles(new HashSet<>());
        User user = Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter(i -> i instanceof User)
                .map(User.class::cast)
                .orElseThrow(() -> new BusinessException("获取当前登录用户失败"));
        Optional.ofNullable(user)
                .map(User::getUsername)
                .filter(StringUtils::hasLength)
                .ifPresent(response::setUsername);
        Optional.ofNullable(user)
                .map(User::getAuthorities)
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasLength)
                .forEach(role -> {
                    if (role.startsWith("ROLE_")) {
                        response.getRoles().add(role);
                    } else {
                        response.getPermissions().add(role);
                    }
                });
        return response;
    }

}
