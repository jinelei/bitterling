package com.jinelei.bitterling.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.bitterling.domain.result.GenericResult;
import com.jinelei.bitterling.config.security.JwtAuthenticationFilter;
import com.jinelei.bitterling.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.jinelei.bitterling.service.MessageService;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${bitterling.administrator.username:admin}")
    private String username;
    @Value("${bitterling.administrator.password:admin}")
    private String password;

    /**
     * 1. 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 2. 自定义用户认证
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.withUsername(username)
                .password(passwordEncoder().encode(password))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(adminUser);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new NullSecurityContextRepository();
    }

    /**
     * 核心安全过滤链（配置权限规则、登录、退出等）
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/static/**", "/api-docs/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "REMEMBER_ME")
                        .permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()))
                .rememberMe(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(SpringBeanUtils.getBean(JwtAuthenticationFilter.class), UsernamePasswordAuthenticationFilter.class);

        ;

        return http.build();
    }

    /**
     * 自定义登录失败处理器
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, authentication) -> {
            GenericResult<String> message = GenericResult.failure(500, "登录失败", Optional.ofNullable(authentication).map(Throwable::getMessage).orElse("账号或密码错误"));
            SpringBeanUtils.getBean(ObjectMapper.class).writeValue(response.getWriter(), message);
        };
    }

    /**
     * 自定义登录成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            SpringBeanUtils.getBean(MessageService.class).userLoginNotify(authentication.getName());
            final String jwtToken = SpringBeanUtils.getBean(JwtTokenUtil.class).generateToken(authentication.getName());
            GenericResult<String> message = GenericResult.of(200, "登录成功", jwtToken);
            SpringBeanUtils.getBean(ObjectMapper.class).writeValue(response.getWriter(), message);
        };
    }

    /**
     * 自定义登出成功处理器
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            SpringBeanUtils.getBean(MessageService.class).userLoginNotify(authentication.getName());
            GenericResult<String> message = GenericResult.of(200, "登出成功", "");
            SpringBeanUtils.getBean(ObjectMapper.class).writeValue(response.getWriter(), message);
        };
    }

    /**
     * 自定义校验失败处理器
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authentication) -> {
            GenericResult<String> message = GenericResult.of(401, "用户未登录", authentication.getMessage());
            SpringBeanUtils.getBean(ObjectMapper.class).writeValue(response.getWriter(), message);
            response.setStatus(HttpStatus.OK.value());
        };
    }

}
