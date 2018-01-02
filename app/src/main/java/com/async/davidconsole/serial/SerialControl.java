package com.async.davidconsole.serial;

import com.async.common.serial.BaseSerialControl;
import com.async.common.serial.BaseSerialMessage;
import com.async.common.serial.MessageMode;
import com.async.common.utils.LogUtil;
import com.async.davidconsole.utils.Constant;
import com.lztek.toolkit.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * filename: com.eternal.davidconsole.serial.SerialControl.java
 * author: Ling Lin
 * created on: 2017/7/18 15:16
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class SerialControl extends BaseSerialControl {

    private SerialPort serialPort = null;
    private Consumer<BaseSerialMessage> messageParser;

    private List<BaseSerialMessage> sessionList;
    private List<BaseSerialMessage> repeatSessionList;

    @Inject
    public SerialControl() {
        super();
        sessionList = new ArrayList<>();
        repeatSessionList = new ArrayList<>();
    }

    public synchronized void start(int bufferSize, Consumer<BaseSerialMessage> messageParser) throws Exception {
        if (serialPort == null) {
            this.messageParser = messageParser;
            int speed = 115200;
            int dataBit = 8;
            int parity = 0;
            int stopBits = 1;
            int dataFlow = 0;
            serialPort = SerialPort.open(Constant.COM1, speed, dataBit, parity, stopBits, dataFlow);
            if (serialPort == null) {
                throw new Exception("Error occur in opening serial port.");
            }
            super.open(bufferSize);
        }
    }

    public synchronized void stop() {
        super.close();
        if (serialPort != null) {
            try {
                Thread.sleep(60);
                SerialPort.close(serialPort);
                serialPort = null;
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
    }

    @Override
    protected OutputStream getOutputStream() {
        return serialPort.getOutputStream();
    }

    @Override
    protected InputStream getInputStream() {
        return serialPort.getInputStream();
    }

    public synchronized void addSession(BaseSerialMessage serialMessage) {
        sessionList.add(serialMessage);
        MessageMode messageMode = serialMessage.getMessageMode();
        if (messageMode.equals(MessageMode.Start)) {
            serialMessage.setMessageMode(MessageMode.InProcess);
            sendAsync(serialMessage);
        }
    }

    public synchronized void addRepeatSession(BaseSerialMessage serialMessage) {
        repeatSessionList.add(serialMessage);
        MessageMode messageMode = serialMessage.getMessageMode();
        if (messageMode.equals(MessageMode.Start)) {
            serialMessage.setMessageMode(MessageMode.InProcess);
            sendAsync(serialMessage);
        }
    }

    public synchronized void refresh() {
        startSession();
        startRepeatSession();
    }

    private void startSession() {
        Iterator<BaseSerialMessage> iterator = sessionList.iterator();
        while (iterator.hasNext()) {
            BaseSerialMessage serialMessage = iterator.next();
            MessageMode messageMode = serialMessage.getMessageMode();
            if (messageMode.equals(MessageMode.Error)) {
                if (serialMessage.getRepeatTime() <= 0) {
                    serialMessage.setOnCompleted(null);
                    iterator.remove();
                } else {
                    serialMessage.setMessageMode(MessageMode.InProcess);
                    sendAsync(serialMessage);
                }
            } else if (messageMode.equals(MessageMode.Completed)) {
                serialMessage.setOnCompleted(null);
                iterator.remove();
            }
        }
    }

    private void startRepeatSession() {
        for (BaseSerialMessage serialMessage : repeatSessionList) {
            serialMessage.setMessageMode(MessageMode.InProcess);
            sendAsync(serialMessage);
        }
    }

    private void sendAsync(BaseSerialMessage serialMessage) {
        Observable.just(serialMessage)
                .observeOn(Schedulers.io())
                .subscribe((message) -> super.sendReceive(message),
                        (error) -> {
                            serialMessage.setResponse(null);
                            serialMessage.decreaseRepeatTime();
                            serialMessage.setMessageMode(MessageMode.Error);
                            messageParser.accept(serialMessage);
                        }, () -> {
                            serialMessage.resetRepeatTime();
                            serialMessage.setMessageMode(MessageMode.Completed);
                            messageParser.accept(serialMessage);
                        });
    }
}
