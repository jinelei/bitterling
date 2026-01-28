package com.jinelei.bitterling.core.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:
 * Long 类型 ID 生成器（包含时间戳+机器ID+随机序列）
 * 特性：
 * 1. ID 包含秒级时间戳，可反向解析出生成时间
 * 2. 机器ID 适配分布式部署（0-1023）
 * 3. 随机序列避免同一秒内同一机器的ID冲突
 * @author: 605142
 * @create: 2026/1/11
 * @version v1.0.0
 **/
public class LongIdGenerator {
    // ===================== 核心配置 =====================
    /** 起始时间戳（2024-01-01 00:00:00），减少ID长度 */
    private static final long EPOCH_SECOND = 1704067200L;
    /** 机器ID位数（10位，最大1023） */
    private static final int MACHINE_ID_BITS = 10;
    /** 随机序列位数（24位，最大16777215） */
    private static final int SEQUENCE_BITS = 24;
    /** 机器ID左移位数（24位） */
    private static final int MACHINE_ID_SHIFT = SEQUENCE_BITS;
    /** 时间戳左移位数（34位 = 10+24） */
    private static final int TIMESTAMP_SHIFT = MACHINE_ID_BITS + SEQUENCE_BITS;
    /** 机器ID最大值（1023） */
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    /** 随机序列最大值（16777215） */
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    // ===================== 运行时变量 =====================
    /** 机器ID（初始化时指定，0-1023） */
    private final long machineId;
    /** 安全随机数生成器（避免伪随机） */
    private final SecureRandom secureRandom;
    /** 上一次生成ID的时间戳（秒），避免同一秒内序列重复 */
    private final AtomicLong lastSecond = new AtomicLong(0);

    /**
     * 构造函数（指定机器ID）
     * @param machineId 机器ID（0-1023），分布式部署时建议从配置/环境变量读取
     * @throws IllegalArgumentException 机器ID超出范围时抛出
     */
    public LongIdGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("机器ID必须在0-" + MAX_MACHINE_ID + "之间，当前值：" + machineId);
        }
        this.machineId = machineId;
        this.secureRandom = new SecureRandom();
    }

    /**
     * 构造函数
     * @throws IllegalArgumentException 机器ID超出范围时抛出
     */
    public LongIdGenerator() {
        this.machineId = 0;
        this.secureRandom = new SecureRandom();
    }

    /**
     * 生成唯一Long型ID
     * @return 包含时间+机器ID+随机序列的Long型ID
     */
    public long generateId() {
        // 1. 获取当前秒级时间戳
        long currentSecond = Instant.now().getEpochSecond() - EPOCH_SECOND;
        if (currentSecond < 0) {
            throw new RuntimeException("时间戳异常，当前时间早于起始时间");
        }

        // 2. 处理同一秒内的并发（确保序列唯一）
        long last = lastSecond.get();
        if (currentSecond == last) {
            // 同一秒，生成随机序列（避免重复）
            long sequence = secureRandom.nextLong() & MAX_SEQUENCE;
            return (currentSecond << TIMESTAMP_SHIFT)
                    | (machineId << MACHINE_ID_SHIFT)
                    | sequence;
        } else {
            // 不同秒，更新时间戳并生成随机序列
            lastSecond.compareAndSet(last, currentSecond);
            long sequence = secureRandom.nextLong() & MAX_SEQUENCE;
            return (currentSecond << TIMESTAMP_SHIFT)
                    | (machineId << MACHINE_ID_SHIFT)
                    | sequence;
        }
    }

    /**
     * 反向解析ID，获取其中的时间、机器ID、序列（用于排查问题）
     * @param id 生成的Long型ID
     * @return 解析结果（时间戳、机器ID、序列）
     */
    public IdParseResult parseId(long id) {
        // 解析时间戳（秒）
        long second = (id >>> TIMESTAMP_SHIFT) + EPOCH_SECOND;
        // 解析机器ID
        long machineId = (id >>> MACHINE_ID_SHIFT) & MAX_MACHINE_ID;
        // 解析序列
        long sequence = id & MAX_SEQUENCE;
        return new IdParseResult(second, machineId, sequence);
    }

    /**
     * 解析结果封装类
     */
    public static class IdParseResult {
        private final long timestampSecond; // 生成时间（秒级时间戳）
        private final long machineId;       // 机器ID
        private final long sequence;        // 随机序列

        public IdParseResult(long timestampSecond, long machineId, long sequence) {
            this.timestampSecond = timestampSecond;
            this.machineId = machineId;
            this.sequence = sequence;
        }

        // Getter
        public long getTimestampSecond() {
            return timestampSecond;
        }

        public long getMachineId() {
            return machineId;
        }

        public long getSequence() {
            return sequence;
        }

        @Override
        public String toString() {
            return "ID解析结果：" +
                    "生成时间戳(秒)=" + timestampSecond +
                    " (北京时间=" + Instant.ofEpochSecond(timestampSecond).atZone(java.time.ZoneId.of("Asia/Shanghai")) + "), " +
                    "机器ID=" + machineId +
                    ", 随机序列=" + sequence;
        }
    }

    // ===================== 测试示例 =====================
    public static void main(String[] args) {
        // 初始化生成器（机器ID建议从配置文件读取，比如0）
        LongIdGenerator generator = new LongIdGenerator(0);

        // 生成10个ID测试
        for (int i = 0; i < 10; i++) {
            long id = generator.generateId();
            System.out.println("生成ID：" + id);
            System.out.println(generator.parseId(id));
            System.out.println("------------------------");
        }
    }
}
