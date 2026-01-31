package com.jinelei.bitterling.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class ProxyHeaderConfig {

    /**
     * 强制识别 Nginx 透传的 X-Forwarded-* 头信息
     */
    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterBean = new FilterRegistrationBean<>();
        ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
        filterBean.setFilter(filter);
        filterBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterBean;
    }
}
