package com.async.davidconsole.serial.command.module;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/9/19 13:00
 * email: 10525677@qq.com
 * description:
 */

public class ModuleGetCommand extends BaseSerialMessage {

    public static final String COMMAND = "~MODULE %s" + CommandChar.ENTER;

    private boolean hardware;
    private String target;
    private int O2;
    private int HUM;
    private int SPO2;
    private int SCALE;

    public ModuleGetCommand(boolean hardware) {
        this.hardware = hardware;
        if (hardware) {
            target = "HARDWARE";
        } else {
            target = "GET";
        }
    }

    public boolean isHardware() {
        return hardware;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, target)).getBytes();
    }

    public int getO2() {
        return O2;
    }

    public void setO2(int o2) {
        O2 = o2;
    }

    public int getHUM() {
        return HUM;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }

    public int getSPO2() {
        return SPO2;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public int getSCALE() {
        return SCALE;
    }

    public void setSCALE(int SCALE) {
        this.SCALE = SCALE;
    }
}
