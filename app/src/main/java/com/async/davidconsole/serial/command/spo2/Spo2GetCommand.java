package com.async.davidconsole.serial.command.spo2;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.enums.AverageTimeMode;
import com.async.davidconsole.serial.command.CommandChar;
import com.async.davidconsole.utils.Constant;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:29
 * email: 10525677@qq.com
 * description:
 */
public class Spo2GetCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~SPO2 GET" + CommandChar.ENTER).getBytes();
    private String status;
    private int avg;
    private String sens;
    private String fastsat;

    public Spo2GetCommand() {
        status = "";
        avg = AverageTimeMode.None.getIndex();
        sens = "";
        fastsat = "";
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = Constant.SENSOR_NA;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public String getFastsat() {
        return fastsat;
    }

    public void setFastsat(String fastsat) {
        this.fastsat = fastsat;
    }
}