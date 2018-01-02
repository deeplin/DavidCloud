package com.async.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.async.davidconsole.controllers.MainApplication;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/17 11:58
 * email: 10525677@qq.com
 * description:
 */

public class ResourceUtil {
    public static int getInt(int resourceId) {
        Context context = MainApplication.getInstance();
        return context.getResources().getInteger(resourceId);
    }

    public static boolean getBool(int resourceId) {
        Context context = MainApplication.getInstance();
        return context.getResources().getBoolean(resourceId);
    }

    public static String getString(int resourceId) {
        Context context = MainApplication.getInstance();
        return context.getResources().getString(resourceId);
    }

    public static void setLocalLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = context.getResources().getConfiguration();
        configuration.locale = locale;
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
