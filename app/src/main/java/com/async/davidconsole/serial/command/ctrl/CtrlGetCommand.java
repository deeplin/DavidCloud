package com.async.davidconsole.serial.command.ctrl;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:21
 * email: 10525677@qq.com
 * description:
 */
public class CtrlGetCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~CTRL GET" + CommandChar.ENTER).getBytes();
    private String ctrl;
    private int c_air;
    private int c_hum;
    private int c_o2;
    private int c_skin;
    private int w_skin;
    private int w_man;
    private int w_inc;

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public String getCtrl() {
        return ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public int getC_air() {
        return c_air;
    }

    public void setC_air(int c_air) {
        this.c_air = c_air;
    }

    public int getC_hum() {
        return c_hum;
    }

    public void setC_hum(int c_hum) {
        this.c_hum = c_hum;
    }

    public int getC_o2() {
        return c_o2;
    }

    public void setC_o2(int c_o2) {
        this.c_o2 = c_o2;
    }

    public int getC_skin() {
        return c_skin;
    }

    public void setC_skin(int c_skin) {
        this.c_skin = c_skin;
    }

    public int getW_skin() {
        return w_skin;
    }

    public void setW_skin(int w_skin) {
        this.w_skin = w_skin;
    }

    public int getW_man() {
        return w_man;
    }

    public void setW_man(int w_man) {
        this.w_man = w_man;
    }

    public int getW_inc() {
        return w_inc;
    }

    public void setW_inc(int w_inc) {
        this.w_inc = w_inc;
    }
}
