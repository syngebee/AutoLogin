package com.itheima.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getSysTime() {
        return sf.format(new Date());
    }

}
