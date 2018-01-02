package com.async.davidconsole.serial.command;

import com.async.common.serial.BaseSerialMessage;

/**
 * filename: com.eternal.davidconsole.serial.command.AlarmCommand.java
 * author: Ling Lin
 * created on: 2017/7/30 16:01
 * email: 10525677@qq.com
 * description:
 */

public class AlarmCommand extends BaseSerialMessage {

    public static final String COMMAND = "~ALARM %s" + CommandChar.ENTER;
    public static final String H = "H";
    public static final String M = "M";
    public static final String L = "L";

    String status;

    public AlarmCommand(String status) {
        this.status = status;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, status)).getBytes();
    }

}
