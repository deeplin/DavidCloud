package com.async.davidconsole.serial.command;

import com.async.common.serial.BaseSerialMessage;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:39
 * email: 10525677@qq.com
 * description:
 */
public class LEDCommand extends BaseSerialMessage {

    public static final String LED37 = "37";

    static final String COMMAND = "~LED %s %s" + CommandChar.ENTER;

    private String value;
    private String status;

    public LEDCommand(String value, boolean status) {
        if (status) {
            this.status = CommandChar.ON;
        } else {
            this.status = CommandChar.OFF;
        }
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value, status)).getBytes();
    }
}
