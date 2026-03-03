package com.jinelei.bitterling.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 扩展版IP匹配工具类
 * 支持：1. 单个IP精确匹配  2. CIDR网段匹配
 */
public class NetworkUtil {

    /**
     * 通用IP匹配方法（推荐使用）
     * 支持单个IP精确匹配 和 CIDR网段匹配
     *
     * @param ipAddress 要判断的IP地址（如：192.168.1.100）
     * @param matchRule 匹配规则：可以是单个IP（如192.168.1.100）或CIDR网段（如192.168.1.0/24）
     * @return true-匹配成功，false-匹配失败
     */
    public static boolean matchIp(String ipAddress, String matchRule) {
        // 校验IP格式合法性（提前拦截无效IP）
        validateIpFormat(ipAddress);

        // 规则不含/，判定为单个IP精确匹配
        if (!matchRule.contains("/")) {
            // 先校验匹配规则的IP格式
            validateIpFormat(matchRule);
            // 忽略大小写、前后空格，直接比较IP字符串
            return ipAddress.trim().equalsIgnoreCase(matchRule.trim());
        }

        // 规则包含/，判定为CIDR网段匹配
        return isIpInCidr(ipAddress, matchRule);
    }

    /**
     * 原有方法：判断IP是否在指定CIDR网段内（保持兼容）
     *
     * @param ipAddress 要判断的IP地址
     * @param cidr      网段（如：192.168.1.0/24）
     * @return true-在网段内，false-不在网段内
     */
    public static boolean isIpInCidr(String ipAddress, String cidr) {
        try {
            String[] cidrParts = cidr.split("/");
            if (cidrParts.length != 2) {
                throw new IllegalArgumentException("CIDR格式错误，正确格式如：192.168.1.0/24");
            }

            String networkIp = cidrParts[0];
            int prefixLength = Integer.parseInt(cidrParts[1]);

            // 转换为长整型
            long ipLong = ipToLong(InetAddress.getByName(ipAddress));
            long networkIpLong = ipToLong(InetAddress.getByName(networkIp));

            // 计算子网掩码
            long subnetMask = createSubnetMask(prefixLength);

            // 按位与运算后比较
            long ipNetwork = ipLong & subnetMask;
            long network = networkIpLong & subnetMask;

            return ipNetwork == network;

        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("IP地址格式错误: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("CIDR前缀长度格式错误: " + e.getMessage());
        }
    }

    /**
     * 校验IP地址格式是否合法
     *
     * @param ip IP地址字符串
     */
    private static void validateIpFormat(String ip) {
        try {
            InetAddress.getByName(ip.trim());
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("IP地址格式非法: " + ip);
        }
    }

    /**
     * 将InetAddress转换为长整型
     *
     * @param inetAddress IP地址对象
     * @return 32位整数形式的IP
     */
    private static long ipToLong(InetAddress inetAddress) {
        byte[] ipBytes = inetAddress.getAddress();
        long result = 0;
        for (byte b : ipBytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }

    /**
     * 根据前缀长度创建子网掩码
     *
     * @param prefixLength 前缀长度（0-32）
     * @return 子网掩码的整数形式
     */
    private static long createSubnetMask(int prefixLength) {
        if (prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("前缀长度必须在0-32之间");
        }
        return (prefixLength == 0) ? 0 : 0xFFFFFFFFL << (32 - prefixLength);
    }

}