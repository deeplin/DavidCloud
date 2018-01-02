package com.async.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/15 15:32
 * email: 10525677@qq.com
 * description:
 */

public class TimeUtil {
    public static final String AxisTime = "yyyy-MM-dd\nhh:mm a";
    public static final String ShortTime = "yy-MM-dd HH:mm:ss";
    public static final String Date = "yyyy-MM-dd";
    public static final String Minute = "hh:mm a";
    public static final String WeekDay = "E";

    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        return dateFormatter.format(date);
    }

    public static String getCurrentEnglishDate(String format) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormatter.format(date);
    }

    public static String longtoDateString(long time, String format) {
        java.util.Date date = new java.util.Date(time);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        return dateFormatter.format(date);
    }
}
