package com.jinelei.bitterling.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;

@Component
public class ProxyUtil {

    @SuppressWarnings("unchecked")
    public <T> T unProxy(T entity) {
        if (entity == null) {
            return null;
        }
        if (entity instanceof HibernateProxy proxy) {
            Hibernate.initialize(proxy);
            return (T) proxy.getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }

}
