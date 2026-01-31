package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.web.domain.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDetailsService userDetailsService;

    public UserInfoResponse getUserInfo(Principal principal) {
        final UserInfoResponse response = new UserInfoResponse();
        final String username = Optional.ofNullable(principal)
                .map(Principal::toString)
                .filter(StringUtils::hasLength)
                .orElseThrow(() -> new BusinessException("获取当前登录用户名失败"));
        response.setUsername(username);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Optional.ofNullable(userDetails).orElseThrow(() -> new BusinessException("获取登录用户信息失败"));
        response.setPermissions(new HashSet<>());
        response.setRoles(new HashSet<>());
        if (!CollectionUtils.isEmpty(userDetails.getAuthorities())) {
            userDetails.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .filter(StringUtils::hasLength)
                    .forEach(role -> {
                        if (role.startsWith("ROLE_")) {
                            response.getRoles().add(role);
                        } else {
                            response.getPermissions().add(role);
                        }
                    });
        }
        return response;
    }

}
