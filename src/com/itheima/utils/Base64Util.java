package com.itheima.utils;

import java.util.Base64;

public class Base64Util {

    public static String encode(String str) {
        byte[] bytes = str.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decode(String str) {
        byte[] decode = Base64.getDecoder().decode(str);
        return new String(decode);
    }
}
