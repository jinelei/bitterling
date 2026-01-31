package com.jinelei.bitterling.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Spring Bean 获取工具类
 * 实现 ApplicationContextAware 接口，自动注入 Spring 上下文，支持多种方式获取 Bean
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {

    // 静态存储 Spring 应用上下文
    private static ApplicationContext applicationContext;

    /**
     * Spring 容器初始化时，自动调用该方法注入 ApplicationContext
     * 
     * @param applicationContext 应用上下文
     * @throws BeansException 上下文获取异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 避免重复赋值（比如多容器场景）
        if (SpringBeanUtils.applicationContext == null) {
            SpringBeanUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 获取 Spring 应用上下文
     * 
     * @return ApplicationContext 实例
     * @throws IllegalStateException 上下文未初始化时抛出
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 根据 Bean 名称获取 Bean 实例
     * 
     * @param beanName Bean 名称（通常是类名首字母小写，或 @Component 注解指定的名称）
     * @param <T>      Bean 类型泛型
     * @return Bean 实例
     * @throws BeansException Bean 获取失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        checkApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 根据 Bean 类型获取 Bean 实例（适用于该类型唯一的 Bean）
     * 
     * @param clazz Bean 类型
     * @param <T>   Bean 类型泛型
     * @return Bean 实例
     * @throws BeansException Bean 获取失败（如多个同类型 Bean）时抛出
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据 Bean 名称 + 类型获取 Bean 实例（精准匹配，避免类型转换问题）
     * 
     * @param beanName Bean 名称
     * @param clazz    Bean 类型
     * @param <T>      Bean 类型泛型
     * @return Bean 实例
     * @throws BeansException Bean 获取失败时抛出
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(beanName, clazz);
    }

    /**
     * 检查上下文是否初始化，未初始化则抛出异常
     */
    private static void checkApplicationContext() {
        Assert.state(applicationContext != null,
                "Spring ApplicationContext 未初始化！请确认工具类已被 Spring 扫描，或 Spring 容器已正常启动。");
    }

    /**
     * 判断指定名称的 Bean 是否存在
     * 
     * @param beanName Bean 名称
     * @return true-存在，false-不存在
     */
    public static boolean containsBean(String beanName) {
        checkApplicationContext();
        return applicationContext.containsBean(beanName);
    }

    /**
     * 判断指定类型的 Bean 是否存在（至少一个）
     * 
     * @param clazz Bean 类型
     * @return true-存在，false-不存在
     */
    public static boolean containsBean(Class<?> clazz) {
        checkApplicationContext();
        return !applicationContext.getBeansOfType(clazz).isEmpty();
    }
}
