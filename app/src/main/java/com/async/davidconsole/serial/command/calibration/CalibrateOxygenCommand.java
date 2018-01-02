package com.async.davidconsole.serial.command.calibration;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * filename: com.eternal.davidconsole.serial.command.calibration.CalibrateOxygenCommand.java
 * author: Ling Lin
 * created on: 2017/7/28 9:54
 * email: 10525677@qq.com
 * description:
 */

public class CalibrateOxygenCommand extends BaseSerialMessage {

    public static final String COMMAND = "~CAL O2 %d %d" + CommandChar.ENTER;

    private int id;
    private int value;

    public CalibrateOxygenCommand(int id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, id, value)).getBytes();
    }
}
