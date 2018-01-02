package com.async.davidconsole.serial.command.alert;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.serial.command.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/20 19:31
 * email: 10525677@qq.com
 * description:
 */

public class AlertDisableCommand extends BaseSerialMessage {

    public static final String COMMAND = "~ALERT DISABLE %s" + CommandChar.ENTER;

    private String alertId;

    public AlertDisableCommand(String alertId) {
        this.alertId = alertId;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, alertId)).getBytes();
    }

}

