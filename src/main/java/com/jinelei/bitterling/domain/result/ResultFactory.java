package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.request.PageableRequest;
import com.jinelei.bitterling.exception.BusinessException;

import java.util.Collection;
import java.util.Optional;

import static com.jinelei.bitterling.constant.PageableProperty.DEFAULT_PAGE_NO;
import static com.jinelei.bitterling.constant.PageableProperty.DEFAULT_PAGE_SIZE;

public class ResultFactory {

    public static <E, T> T create(Class<T> clazz, Integer code, String message, E data) {
        return switch (judgeResultMode(Optional.ofNullable(clazz).orElseThrow(() -> new BusinessException("Class参数不能为空")))) {
            case RAW_GENERIC -> clazz.cast(GenericResult.of(code, message, data));
            case COLLECTION -> clazz.cast(CollectionResult.of(code, message, data));
            case PAGEABLE -> clazz.cast(PageableResult.of(code, message, data));
        };
    }

    public static <E extends Collection<?>, T> T create(Class<T> clazz, Integer code, String message, E data, Long total, PageableRequest<?> req) {
        Integer pageNo = Optional.ofNullable(req).map(PageableRequest::getPageNo).orElse(DEFAULT_PAGE_NO);
        Integer pageSize = Optional.ofNullable(req).map(PageableRequest::getPageNo).orElse(DEFAULT_PAGE_SIZE);
        return switch (judgeResultMode(Optional.ofNullable(clazz).orElseThrow(() -> new BusinessException("Class参数不能为空")))) {
            case RAW_GENERIC -> throw new BusinessException("集合类型不能使用该返回类型");
            case COLLECTION -> clazz.cast(CollectionResult.of(code, message, data));
            case PAGEABLE -> clazz.cast(PageableResult.of(code, message, pageNo, pageSize, data, total));
        };
    }


    public enum ResultMode {
        RAW_GENERIC,    // 模式1：裸继承GenericResult
        COLLECTION,     // 模式2：继承CollectionResult
        PAGEABLE        // 模式3：继承PageableResult
    }

    /**
     * 核心方法：判断Class对应的继承模式
     *
     * @param clazz 要判断的GenericResult子类Class
     * @return 对应的模式枚举
     * @throws IllegalArgumentException 非GenericResult子类时抛出
     */
    public static ResultMode judgeResultMode(Class<?> clazz) {
        // 前置校验：必须是GenericResult的子类
        if (!GenericResult.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + " 不是GenericResult的子类");
        }

        // 1. 判断是否是PageableResult的子类（模式3）
        if (isAssignableTo(clazz, PageableResult.class)) {
            // 额外校验：泛型是否符合 T extends Collection<?>（可选，根据你的需求）
            validateCollectionGeneric(clazz, PageableResult.class);
            return ResultMode.PAGEABLE;
        }

        // 2. 判断是否是CollectionResult的子类（模式2）
        if (isAssignableTo(clazz, CollectionResult.class)) {
            validateCollectionGeneric(clazz, CollectionResult.class);
            return ResultMode.COLLECTION;
        }

        // 3. 剩下的就是裸继承GenericResult（模式1）
        return ResultMode.RAW_GENERIC;
    }

    /**
     * 工具方法：判断clazz是否是targetSuper的子类（排除自身，只判断继承关系）
     * （因为PageableResult/CollectionResult本身是抽象类，实际传入的是子类）
     */
    private static boolean isAssignableTo(Class<?> clazz, Class<?> targetSuper) {
        Class<?> current = clazz;
        // 递归遍历继承链，直到GenericResult
        while (current != null && current != GenericResult.class) {
            Class<?> superClass = current.getSuperclass();
            if (superClass == targetSuper) {
                return true;
            }
            current = superClass;
        }
        return false;
    }

    /**
     * 可选校验：确保泛型T符合 extends Collection<?> 的约束
     * （如果你的场景中不需要严格校验泛型，可注释此方法）
     */
    private static void validateCollectionGeneric(Class<?> clazz, Class<?> parentClass) {
        try {
            // 获取父类的泛型类型
            java.lang.reflect.ParameterizedType parameterizedType =
                    (java.lang.reflect.ParameterizedType) clazz.getGenericSuperclass();
            // 获取泛型参数T的实际类型
            Class<?> actualType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            // 校验是否是Collection的子类
            if (!Collection.class.isAssignableFrom(actualType)) {
                throw new IllegalArgumentException(
                        clazz.getName() + " 的泛型类型 " + actualType.getName() +
                                " 不符合 " + parentClass.getName() + " 的约束：T extends Collection<?>");
            }
        } catch (Exception e) {
            // 泛型擦除等情况时的容错（可选抛出或忽略）
            throw new IllegalArgumentException(
                    "无法校验 " + clazz.getName() + " 的泛型约束", e);
        }
    }
}
