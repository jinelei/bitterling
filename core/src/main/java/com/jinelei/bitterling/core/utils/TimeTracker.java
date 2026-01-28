package com.jinelei.bitterling.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 代码耗时跟踪工具类（线程隔离）
 * 支持：总耗时统计、区间耗时统计、自定义跟踪点、线程安全
 */
public class TimeTracker {
    private static final Logger log = LoggerFactory.getLogger(TimeTracker.class);

    // ThreadLocal保证线程隔离：每个线程拥有独立的TimeTracker实例
    private static final ThreadLocal<TimeTracker> THREAD_LOCAL = new ThreadLocal<>();

    // 初始化时间（纳秒）
    private long initNanos;
    // 上一个跟踪点的时间（纳秒）
    private long lastMarkNanos;
    // 跟踪点名称 -> 时间戳（纳秒），记录所有跟踪点，LinkedHashMap保证顺序
    private final Map<String, Long> markPoints = new LinkedHashMap<>();

    /**
     * 私有构造器：初始化计时起点
     */
    private TimeTracker() {
        this.initNanos = System.nanoTime();
        this.lastMarkNanos = this.initNanos;
        // 记录初始跟踪点
        markPoints.put("INIT", this.initNanos);
    }

    // ===================== 核心API =====================

    /**
     * 获取当前线程的TimeTracker实例（懒加载，首次调用初始化）
     */
    public static TimeTracker getInstance() {
        TimeTracker tracker = THREAD_LOCAL.get();
        if (tracker == null) {
            tracker = new TimeTracker();
            THREAD_LOCAL.set(tracker);
        }
        return tracker;
    }

    /**
     * 添加跟踪点，并打印该跟踪点与上一个跟踪点的区间耗时
     * 
     * @param markName 跟踪点名称（如"接口参数校验"、"数据库查询"）
     */
    public TimeTracker mark(String markName) {
        long currentNanos = System.nanoTime();
        // 记录当前跟踪点时间
        markPoints.put(markName, currentNanos);
        // 计算区间耗时（上一个跟踪点 → 当前跟踪点）
        long intervalNanos = currentNanos - lastMarkNanos;
        // 更新上一个跟踪点时间
        this.lastMarkNanos = currentNanos;

        // 打印区间耗时（默认日志格式，支持毫秒/微秒，保留3位小数）
        log.info("[耗时跟踪-区间] 跟踪点：{} | 区间耗时：{}ms（{}μs）",
                markName,
                formatTime(intervalNanos, TimeUnit.MILLISECONDS),
                formatTime(intervalNanos, TimeUnit.MICROSECONDS));
        return this; // 链式调用
    }

    /**
     * 打印从初始化到当前的总耗时
     * 
     * @param desc 总耗时描述（如"接口处理总耗时"）
     */
    public TimeTracker printTotalTime(String desc) {
        long totalNanos = System.nanoTime() - initNanos;
        log.info("[耗时跟踪-总耗时] {} | 总耗时：{}ms（{}μs）",
                desc,
                formatTime(totalNanos, TimeUnit.MILLISECONDS),
                formatTime(totalNanos, TimeUnit.MICROSECONDS));
        return this; // 链式调用
    }

    /**
     * 打印所有跟踪点的完整耗时链路（含顺序、区间耗时、总耗时）
     * 
     * @param title 链路标题（如"接口/api/test 耗时链路"）
     */
    public TimeTracker printFullTrack(String title) {
        log.info("=====================================");
        log.info("[耗时跟踪-完整链路] {}", title);
        log.info("-------------------------------------");

        // 遍历所有跟踪点，计算每个区间耗时
        String[] markNames = markPoints.keySet().toArray(new String[0]);
        for (int i = 1; i < markNames.length; i++) {
            String prevMark = markNames[i - 1];
            String currMark = markNames[i];
            long prevTime = markPoints.get(prevMark);
            long currTime = markPoints.get(currMark);
            long intervalNanos = currTime - prevTime;

            log.info("{} → {}：{}ms（{}μs）",
                    prevMark, currMark,
                    formatTime(intervalNanos, TimeUnit.MILLISECONDS),
                    formatTime(intervalNanos, TimeUnit.MICROSECONDS));
        }

        // 打印总耗时
        long totalNanos = System.nanoTime() - initNanos;
        log.info("-------------------------------------");
        log.info("总耗时：{}ms（{}μs）",
                formatTime(totalNanos, TimeUnit.MILLISECONDS),
                formatTime(totalNanos, TimeUnit.MICROSECONDS));
        log.info("=====================================");
        return this;
    }

    /**
     * 重置当前线程的跟踪器（清空所有跟踪点，重新初始化计时）
     */
    public TimeTracker reset() {
        this.initNanos = System.nanoTime();
        this.lastMarkNanos = this.initNanos;
        this.markPoints.clear();
        this.markPoints.put("INIT", this.initNanos);
        return this;
    }

    /**
     * 移除当前线程的TimeTracker实例（避免内存泄漏，建议在任务结束时调用）
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

    // ===================== 私有工具方法 =====================

    /**
     * 格式化时间（纳秒转指定单位，保留3位小数）
     */
    private String formatTime(long nanos, TimeUnit targetUnit) {
        double value = switch (targetUnit) {
            case MILLISECONDS -> nanos / 1_000_000.0;
            case MICROSECONDS -> nanos / 1_000.0;
            case SECONDS -> nanos / 1_000_000_000.0;
            default -> nanos;
        };
        // 保留3位小数，避免科学计数法
        return String.format("%.3f", value);
    }
}
