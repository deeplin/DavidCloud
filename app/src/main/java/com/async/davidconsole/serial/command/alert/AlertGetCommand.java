package com.async.davidconsole.serial.command.alert;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.enums.AlertSettingMode;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * filename: com.eternal.davidconsole.serial.command.alertString.AlertGetCommand.java
 * author: Ling Lin
 * created on: 2017/7/22 22:01
 * email: 10525677@qq.com
 * description:
 */

public class AlertGetCommand extends BaseSerialMessage {
//    OK;ADev=20;SDev=20;

    public static final String COMMAND = "~ALERT GET %s" + CommandChar.ENTER;

    AlertSettingMode alertSettingMode;

    /*Offset*/
    private int Dev;
    private int ADev;
    private int SDev;

    /*Overheat*/
    private int T1;
    private int T2;
    private int Active;

    /*SPO2 PR*/
    private int limit;

    public AlertGetCommand(AlertSettingMode alertSettingMode) {
        this.alertSettingMode = alertSettingMode;
    }

    public int getDev() {
        return Dev;
    }

    public void setDev(int dev) {
        Dev = dev;
    }

    public int getADev() {
        return ADev;
    }

    public void setADev(int ADev) {
        this.ADev = ADev;
    }

    public int getSDev() {
        return SDev;
    }

    public void setSDev(int SDev) {
        this.SDev = SDev;
    }

    public AlertSettingMode getAlertSettingMode() {
        return alertSettingMode;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, alertSettingMode.getName())).getBytes();
    }

    public int getT1() {
        return T1;
    }

    public void setT1(int t1) {
        T1 = t1;
    }

    public int getT2() {
        return T2;
    }

    public void setT2(int t2) {
        T2 = t2;
    }

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

