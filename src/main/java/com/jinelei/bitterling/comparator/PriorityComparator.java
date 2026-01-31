package com.jinelei.bitterling.comparator;
import java.util.*;
import java.util.Comparator;

/**
 * 自定义优先级排序器
 * @param <T> 排序元素的类型
 */
public class PriorityComparator<T> implements Comparator<T> {

    // 基准优先级映射：元素 -> 优先级（数值越小，排序越靠前）
    private final Map<T, Integer> priorityMap;
    // 兜底排序器（元素不在基准数组时使用）
    private final Comparator<T> fallbackComparator;

    /**
     * 构造方法
     * @param priorityArray 基准优先级数组（数组前的元素优先级更高）
     * @param fallbackComparator 兜底排序器（可为 null，null 时未匹配元素默认放最后，且相互间无序）
     */
    public PriorityComparator(T[] priorityArray, Comparator<T> fallbackComparator) {
        // 初始化基准优先级映射：数组索引 = 优先级（索引0优先级最高）
        this.priorityMap = new HashMap<>();
        for (int i = 0; i < priorityArray.length; i++) {
            T element = priorityArray[i];
            if (element != null) {
                priorityMap.put(element, i);
            }
        }
        // 兜底排序器：null 时默认使用“未匹配元素放最后”的排序规则
        this.fallbackComparator = fallbackComparator != null 
                ? fallbackComparator 
                : (o1, o2) -> 0;
    }

    @Override
    public int compare(T o1, T o2) {
        // 1. 获取两个元素的基准优先级（未匹配则赋值为 Integer.MAX_VALUE，放最后）
        int p1 = priorityMap.getOrDefault(o1, Integer.MAX_VALUE);
        int p2 = priorityMap.getOrDefault(o2, Integer.MAX_VALUE);

        // 2. 优先按基准优先级排序
        if (p1 != p2) {
            return Integer.compare(p1, p2);
        }

        // 3. 基准优先级相同（都匹配/都未匹配），使用兜底排序器
        return fallbackComparator.compare(o1, o2);
    }

    // 工具方法：快速创建排序器（简化调用）
    public static <T> PriorityComparator<T> of(T[] priorityArray, Comparator<T> fallbackComparator) {
        return new PriorityComparator<>(priorityArray, fallbackComparator);
    }

    // 工具方法：创建“未匹配元素按自然顺序排序”的排序器
    public static <T extends Comparable<T>> PriorityComparator<T> ofNaturalOrder(T[] priorityArray) {
        return new PriorityComparator<>(priorityArray, Comparator.naturalOrder());
    }

    // 工具方法：创建“未匹配元素按逆序排序”的排序器
    public static <T extends Comparable<T>> PriorityComparator<T> ofReverseOrder(T[] priorityArray) {
        return new PriorityComparator<>(priorityArray, Comparator.reverseOrder());
    }
}
