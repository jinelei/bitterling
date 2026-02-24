package com.jinelei.bitterling.domain.result;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.BiPredicate;

public class ResultFactory {

    public static final BiPredicate<Class<?>, Class<?>> isAssignableTo = (clz, targetSuper) -> {
        Class<?> current = clz;
        while (current != null && current != GenericResult.class) {
            Class<?> superClass = current.getSuperclass();
            if (superClass == targetSuper) {
                return true;
            }
            current = superClass;
        }
        return false;
    };

    public static <T extends GenericResult<?>> T create(Class<T> clazz, Object... args) {
        if (args == null || args.length == 0) {
            return create(clazz);
        }
        try {
            Constructor<T> constructor = getMatchingConstructor(clazz, args);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(clazz.getSimpleName() + "没有匹配的构造器，参数类型不匹配", e);
        } catch (Exception e) {
            throw new RuntimeException("创建" + clazz.getSimpleName() + "并赋值失败：" + e.getMessage(), e);
        }
    }

    /**
     * 智能获取构造器：支持基本类型和包装类型自动匹配
     *
     * @param clazz 目标类
     * @param args  构造器参数值（实际传入的参数，如 Integer、Long 等）
     * @return 匹配的构造器
     * @throws NoSuchMethodException 无匹配的构造器时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getMatchingConstructor(Class<T> clazz, Object... args) throws NoSuchMethodException {
        if (args == null) {
            args = new Object[0];
        }

        // 第一步：尝试直接匹配（参数类型完全一致）
        Class<?>[] paramTypes = getParameterTypes(args);
        try {
            return clazz.getDeclaredConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            // 直接匹配失败，进入宽松匹配逻辑
        }

        // 第二步：遍历所有构造器，进行宽松类型匹配
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            // 先校验参数数量是否一致
            if (parameters.length != args.length) {
                continue;
            }

            // 校验每个参数的类型是否兼容（基本类型/包装类型互转）
            boolean match = true;
            for (int i = 0; i < parameters.length; i++) {
                Class<?> paramType = parameters[i].getType();
                Class<?> argType = args[i] == null ? null : args[i].getClass();
                if (!isTypeCompatible(paramType, argType)) {
                    match = false;
                    break;
                }
            }

            // 找到匹配的构造器，强制类型转换返回
            if (match) {
                return (Constructor<T>) constructor;
            }
        }

        // 所有构造器都不匹配，抛出异常
        throw new NoSuchMethodException(
                "类 " + clazz.getName() + " 没有匹配的构造器，参数类型：" + Arrays.toString(paramTypes)
        );
    }

    /**
     * 获取参数值对应的类型数组（用于初始匹配）
     */
    private static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i] == null ? Object.class : args[i].getClass();
        }
        return paramTypes;
    }

    /**
     * 核心：判断参数类型是否兼容（支持基本类型/包装类型互转）
     *
     * @param paramType 构造器声明的参数类型（如 int）
     * @param argType   实际传入参数的类型（如 Integer）
     * @return 是否兼容
     */
    private static boolean isTypeCompatible(Class<?> paramType, Class<?> argType) {
        // 1. 空值兼容所有引用类型（基本类型不能传null）
        if (argType == null) {
            return !paramType.isPrimitive();
        }

        // 2. 类型完全一致
        if (paramType == argType) {
            return true;
        }

        // 3. 基本类型 <-> 包装类型 互转匹配
        Map<Class<?>, Class<?>> primitiveToWrapper = getPrimitiveToWrapperMap();
        Map<Class<?>, Class<?>> wrapperToPrimitive = getWrapperToPrimitiveMap();

        // 情况1：构造器参数是基本类型，传入的是包装类型
        if (paramType.isPrimitive() && wrapperToPrimitive.containsKey(argType)) {
            return paramType == wrapperToPrimitive.get(argType);
        }

        // 情况2：构造器参数是包装类型，传入的是基本类型（极少出现，因为基本类型值会自动装箱）
        if (primitiveToWrapper.containsKey(paramType) && argType.isPrimitive()) {
            return primitiveToWrapper.get(paramType) == argType;
        }

        // 4. 其他兼容情况（如子类赋值给父类）
        return paramType.isAssignableFrom(argType);
    }

    /**
     * 基本类型 -> 包装类型 映射
     */
    private static Map<Class<?>, Class<?>> getPrimitiveToWrapperMap() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
        map.put(char.class, Character.class);
        return map;
    }

    /**
     * 包装类型 -> 基本类型 映射
     */
    private static Map<Class<?>, Class<?>> getWrapperToPrimitiveMap() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Integer.class, int.class);
        map.put(Long.class, long.class);
        map.put(Boolean.class, boolean.class);
        map.put(Byte.class, byte.class);
        map.put(Short.class, short.class);
        map.put(Float.class, float.class);
        map.put(Double.class, double.class);
        map.put(Character.class, char.class);
        return map;
    }

}
