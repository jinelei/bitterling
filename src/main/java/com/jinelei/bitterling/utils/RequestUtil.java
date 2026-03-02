package com.jinelei.bitterling.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;

/**
 * HttpServletRequest工具类：获取IP、设备类型等信息
 */
public class RequestUtil {

    // 移动端设备关键词匹配正则（覆盖主流手机/平板）
    private static final Pattern MOBILE_PATTERN = Pattern.compile(
            "(android|iphone|ipad|ipod|mobile|phone|ios|symbianos|windows phone|webos|blackberry|iemobile|opera mini)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 获取客户端真实IP地址（兼容代理/反向代理）
     *
     * @param request HttpServletRequest
     * @return 真实IP，获取失败返回空字符串
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        // 1. 优先从代理请求头获取（X-Forwarded-For：反向代理如Nginx常用）
        String ip = request.getHeader("X-Forwarded-For");
        if (isInvalidIp(ip)) {
            // 2. 其次获取X-Real-IP（Nginx/apache等反向代理常用）
            ip = request.getHeader("X-Real-IP");
            if (isInvalidIp(ip)) {
                // 3. 最后获取原生IP
                ip = request.getRemoteAddr();
            }
        }

        // 处理X-Forwarded-For多IP情况（格式：clientIP, proxy1IP, proxy2IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 本地环境IP处理（127.0.0.1/0:0:0:0:0:0:0:1）
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            // 本地环境可返回固定标识，或根据需求调整
            return "127.0.0.1";
        }

        return ip == null ? "" : ip;
    }

    /**
     * 判断IP是否无效（空/unknown）
     */
    private static boolean isInvalidIp(String ip) {
        return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }

    /**
     * 判断客户端是否为移动端（手机/平板）
     *
     * @param request HttpServletRequest
     * @return true=移动端（手机/平板），false=电脑端
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        // 获取User-Agent请求头
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return false;
        }
        // 匹配移动端关键词
        return MOBILE_PATTERN.matcher(userAgent).find();
    }

    /**
     * 获取客户端设备类型描述（电脑/手机/平板）
     *
     * @param request HttpServletRequest
     * @return 设备类型字符串
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }
        userAgent = userAgent.toLowerCase();
        // 优先判断平板
        if (userAgent.contains("ipad") || (userAgent.contains("android") && !userAgent.contains("mobile"))) {
            return "Tablet";
        }
        // 判断手机
        else if (MOBILE_PATTERN.matcher(userAgent).find()) {
            return "Mobile";
        }
        // 其余判定为电脑
        else {
            return "Computer";
        }
    }

}