package com.jinelei.bitterling.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtils {
    
    /**
     * 获取异常的完整堆栈信息字符串
     * @param e 异常对象（可为null）
     * @return 格式化后的堆栈信息字符串，异常为null时返回空字符串
     */
    public static String getStackTraceAsString(Throwable e) {
        if (e == null) {
            return "";
        }
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        } catch (Exception ex) {
            return "获取异常堆栈信息失败：" + ex.getMessage();
        }
    }
}
