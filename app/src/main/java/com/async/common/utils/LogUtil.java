package com.async.common.utils;

import android.util.Log;

/**
 * author: Ling Lin
 * created on: 2017/7/15 17:16
 * email: 10525677@qq.com
 * description:
 */

public class LogUtil {

    private static String TAG_INFO = "deeplin";

    public static void i(Object object, String msg) {
        String tag;
        if (TAG_INFO == null) {
            tag = object.getClass().getSimpleName();
        } else {
            tag = TAG_INFO;
        }
        Log.i(tag, msg);
    }

    public static void w(Object object, String msg) {
        String tag;
        if (TAG_INFO == null) {
            tag = object.getClass().getSimpleName();
        } else {
            tag = TAG_INFO;
        }
        Log.w(tag, msg);
    }

    public static void e(Object object, String msg) {
        String tag;
        if (TAG_INFO == null) {
            tag = object.getClass().getSimpleName();
        } else {
            tag = TAG_INFO;
        }
        Log.e(tag, msg);
    }

    public static void e(Object object, Throwable e) {
        e(object, "");
        Log.getStackTraceString(e);
    }
}