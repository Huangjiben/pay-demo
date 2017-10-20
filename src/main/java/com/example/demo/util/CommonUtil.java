package com.example.demo.util;

public class CommonUtil {
    /**
     *   
     *  * 转化字符串为十六进制编码  
     *  * @param s  
     *  * @return  
     *  
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
}
