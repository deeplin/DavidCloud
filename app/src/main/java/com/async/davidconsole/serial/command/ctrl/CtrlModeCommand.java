package com.async.davidconsole.serial.command.ctrl;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/22 12:44
 * email: 10525677@qq.com
 * description:
 */

public class CtrlModeCommand extends BaseSerialMessage {

    public static final String COMMAND = "~CTRL MODE %s" + CommandChar.ENTER;

    private String value;

    public CtrlModeCommand(String value) {
        this.value = value.toUpperCase();
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value)).getBytes();
    }

}