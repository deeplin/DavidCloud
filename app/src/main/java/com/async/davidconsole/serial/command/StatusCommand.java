package com.async.davidconsole.serial.command;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.utils.Constant;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:30
 * email: 10525677@qq.com
 * description:
 */
public class StatusCommand extends BaseSerialMessage {

//    OK;Mode=Transit;Ctrl=Standby;Time=3735;CTime=3735;Warm=0;Inc=0;Hum=0;O2=0;Alert=SYS.PWR;

    public static final byte[] COMMAND = ("~STATUS" + CommandChar.ENTER).getBytes();
    private String mode;
    private String ctrl;
    private int time;
    private int CTime;
    private int warm;
    private int inc;
    /*湿度阀门*/
    private int hum;
    /*氧气阀门*/
    private int o2;
    private String alert;

    public StatusCommand() {
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCtrl() {
        return this.ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getWarm() {
        return warm;
    }

    public void setWarm(int warm) {
        this.warm = warm;
    }

    public int getInc() {
        return inc;
    }

    public void setInc(int inc) {
        this.inc = inc;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public int getO2() {
        return o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = Constant.SENSOR_NA;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public int getCTime() {
        return CTime;
    }

    public void setCTime(int CTime) {
        this.CTime = CTime;
    }
}

