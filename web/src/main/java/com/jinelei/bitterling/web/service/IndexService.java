package com.jinelei.bitterling.web.service;

import java.time.LocalDateTime;

public class IndexService {

    public String getGreeting() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (hour >= 5 && hour < 9) {
            return "早上好";
        } else if (hour >= 9 && hour < 12) {
            return "上午好";
        } else if (hour >= 12 && hour < 14) {
            return "中午好";
        } else if (hour >= 14 && hour < 18) {
            return "下午好";
        } else if (hour >= 18 && hour < 22) {
            return "晚上好";
        } else {
            return "夜深了";
        }
    };
}
