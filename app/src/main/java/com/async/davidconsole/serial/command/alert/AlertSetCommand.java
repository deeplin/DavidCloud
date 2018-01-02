package com.async.davidconsole.serial.command.alert;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/22 21:55
 * email: 10525677@qq.com
 * description:
 */

public class AlertSetCommand extends BaseSerialMessage {

    public static final String COMMAND_SPO2 = "~ALERT SET %s %d" + CommandChar.ENTER;
    public static final String COMMAND_OFFSET = "~ALERT SET %s %d %d" + CommandChar.ENTER;
    public static final String COMMAND_OVERHEAT = "~ALERT SET %s %d %d %d" + CommandChar.ENTER;

    /*Spo2 Pr*/
    /*Offset*/
    String target;
    int value1;
    int value;

    /*OverHeat*/
    int T1;
    int T2;
    int active;

    public AlertSetCommand(String target, int value) {
        this.target = target;
        this.value = value;
        this.active = -2;
    }

    public AlertSetCommand(String target, int value1, int value2) {
        this.target = target;
        this.value1 = value1;
        this.value = value2;
        this.active = -1;
    }

    public AlertSetCommand(String target, int T1, int T2, int active) {
        this.target = target;
        this.T1 = T1;
        this.T2 = T2;
        this.active = active;
    }

    @Override
    public byte[] getRequest() {
        if (active == -1) {
            return (String.format(COMMAND_OFFSET, target, value1, value)).getBytes();
        } else if (active == -2) {
            return (String.format(COMMAND_SPO2, target, value)).getBytes();
        } else {
            return (String.format(COMMAND_OVERHEAT, target, T1, T2, active)).getBytes();
        }
    }
}

