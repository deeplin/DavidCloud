package com.async.davidconsole.utils;

import android.graphics.Color;

/**
 * author: Ling Lin
 * created on: 2017/7/17 12:23
 * email: 10525677@qq.com
 * description:
 */

public class SystemConfig {

    public static final int SCREEN_LOCK_SECOND = 30; //second
    public static final int BUTTON_CLICK_TIMEOUT = 500; //millisecond
    public static final int LONG_CLICK_DELAY = 100; //millisecond

    public static final int SAVE_ANALOG_SECOND = 60; //每分钟存储一次
    public static final int SAVE_WEIGHT_SECOND = 60; //每小时存储一次
    public static final String ADMIN_PASSWORD = "121212";
    public static int ANALOG_SAVED_IN_DATABASE = 43200; //43200 per month 存储一月
    public static int SCALE_SAVED_IN_DATABASE = 1440; //720 per month 存储二月
    public static int AXIS_COLOR = Color.parseColor("#999999");
    public static int AXIS_X_DOT_PER_STEP = 10;

    public static int TEMP_DISPLAY_UPPER = 650;
    public static int TEMP_DISPLAY_LOWER = 50;

    public static int SPO2_DISPLAY_UPPER = 1000;
    public static int SPO2_DISPLAY_LOWER = 10;

    public static int PR_DISPLAY_UPPER = 240;
    public static int PR_DISPLAY_LOWER = 25;

    public static int OXYGEN_DISPLAY_UPPER = 1000;
    public static int OXYGEN_DISPLAY_LOWER = 0;

    public static int HUMIDITY_DISPLAY_UPPER = 1000;
    public static int HUMIDITY_DISPLAY_LOWER = 0;

    public static int SCALE_DISPLAY_UPPER = 8000;
    public static int SCALE_DISPLAY_LOWER = 0;

    public static int PI_DISPLAY_UPPER = 20 * 10000;
    public static int PI_DISPLAY_LOWER = 200;
}
