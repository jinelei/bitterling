package com.jinelei.bitterling.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Java原生MD5加密工具类
 */
public class MD5Util {

    /**
     * 字符串MD5加密（返回32位小写十六进制字符串）
     * @param input 待加密的字符串
     * @return 32位MD5加密结果，加密失败返回null
     */
    public static String encrypt(String input) {
        // 空值处理
        if (input == null || input.isEmpty()) {
            return null;
        }

        try {
            // 1. 获取MD5算法的MessageDigest实例
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            
            // 2. 将字符串转换为字节数组（指定UTF-8编码避免乱码）
            byte[] inputBytes = input.getBytes("UTF-8");
            
            // 3. 计算MD5摘要（核心步骤）
            byte[] digestBytes = md5.digest(inputBytes);
            
            // 4. 将二进制摘要转换为32位十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digestBytes) {
                // 将字节转换为两位十六进制数（补0避免位数不足）
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // 算法不存在（理论上不会发生，MD5是Java内置算法）
            System.err.println("MD5算法不存在：" + e.getMessage());
            return null;
        } catch (Exception e) {
            // 其他异常（如编码异常）
            System.err.println("MD5加密失败：" + e.getMessage());
            return null;
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：普通字符串
        String testStr = "123456";
        String md5Result = MD5Util.encrypt(testStr);
        System.out.println("字符串\"" + testStr + "\"的MD5：" + md5Result);
        // 输出：e10adc3949ba59abbe56e057f20f883e（123456的标准MD5结果）

        // 测试用例2：含中文的字符串
        String chineseStr = "Java原生MD5加密";
        System.out.println("字符串\"" + chineseStr + "\"的MD5：" + MD5Util.encrypt(chineseStr));
    }
}