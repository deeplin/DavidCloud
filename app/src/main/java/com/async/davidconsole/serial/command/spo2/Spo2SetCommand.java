package com.async.davidconsole.serial.command.spo2;


import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * filename: com.eternal.davidconsole.serial.command.spo2.Spo2SetCommand.java
 * author: Ling Lin
 * created on: 2017/8/1 22:22
 * email: 10525677@qq.com
 * description:
 */

public class Spo2SetCommand extends BaseSerialMessage {

    public static final String COMMAND = "~SPO2 SET %s %s" + CommandChar.ENTER;

    private String target;
    private String value;

    public Spo2SetCommand(String target, String value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, target, value)).getBytes();
    }

}
