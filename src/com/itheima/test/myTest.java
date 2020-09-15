package com.itheima.test;

import com.itheima.utils.Base64Util;
import com.itheima.utils.MD5Util;
import org.junit.Test;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class myTest {

    @Test
    public void Md5Test() {
        String md5 = MD5Util.getMD5("");
        System.out.println(md5);
    }

    @Test
    public void CalendarTest() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        long time = calendar.getTime().getTime();
        System.out.println(time);
    }

    @Test
    public void Base64Test() {
        String test = "陈涛涛:1312313212:asdofashfqwbefkb12912bj";
        String encode = Base64Util.encode(test);
        System.out.println("encode = " + encode);
        String decode = Base64Util.decode(encode);
        System.out.println("decode = " + decode);
    }
}
