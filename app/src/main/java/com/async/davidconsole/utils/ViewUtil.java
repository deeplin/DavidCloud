package com.async.davidconsole.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.databinding.ObservableBoolean;

import com.async.common.actions.Action;

import java.text.DecimalFormat;

/**
 * author: Ling Lin
 * created on: 2017/7/16 15:19
 * email: 10525677@qq.com
 * description:
 */

public class ViewUtil {
    public static void changeFragment(FragmentManager fragmentManager, Fragment fromFragment,
                                      Fragment toFragment, int toFragmentId, int resourceID) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fromFragment != null) {
            fragmentTransaction.remove(fromFragment);
        }
        if(toFragment != null) {
            if (toFragment.isAdded()) {
                fragmentTransaction.show(toFragment);
            } else {
                fragmentTransaction.add(resourceID, toFragment, String.valueOf(toFragmentId));
            }
        }
        fragmentTransaction.commit();
    }

    /*设置是否显示SPO2 PR Oxygen Humidity*/
    public static void displaySensor(boolean installed, boolean enabled, ObservableBoolean visible, Action<Boolean> action) {
        if (installed) {
            if (enabled) {
                visible.set(false);
            } else {
                visible.set(true);
                action.accept(false);
            }
        } else {
            visible.set(true);
            action.accept(true);
        }
    }

    public static String formatTempValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.TEMP_DISPLAY_UPPER && value >= SystemConfig.TEMP_DISPLAY_LOWER) {
            DecimalFormat decimalFormat = new DecimalFormat("00.0");
            return decimalFormat.format(value / 10.0);
        } else {
            return "--.-";
        }
    }

    public static String formatSpo2Value(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.SPO2_DISPLAY_UPPER && value >= SystemConfig.SPO2_DISPLAY_LOWER) {
            return String.valueOf(value / 10);
        } else {
            return "--";
        }
    }

    public static String formatPrValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.PR_DISPLAY_UPPER && value >= SystemConfig.PR_DISPLAY_LOWER) {
            return String.valueOf(value);
        } else {
            return "--";
        }
    }

    public static String formatOxygenValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.OXYGEN_DISPLAY_UPPER && value >= SystemConfig.OXYGEN_DISPLAY_LOWER) {
            return String.valueOf(value / 10);
        } else {
            return "--";
        }
    }

    public static String formatHumidityValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.HUMIDITY_DISPLAY_UPPER && value >= SystemConfig.HUMIDITY_DISPLAY_LOWER) {
            return String.valueOf(value / 10);
        } else {
            return "--";
        }
    }

    public static String formatScaleValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.SCALE_DISPLAY_UPPER && value >= SystemConfig.SCALE_DISPLAY_LOWER) {
            return String.valueOf(value);
        } else {
            return "----";
        }
    }

    public static String formatPiValue(int value) {
        if (value == Constant.SENSOR_NA_VALUE) {
            return Constant.SENSOR_NA;
        } else if (value <= SystemConfig.PI_DISPLAY_UPPER && value >= SystemConfig.PI_DISPLAY_LOWER) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(value / 10000.0) + "%";
        } else {
            return "--.-";
        }
    }
}