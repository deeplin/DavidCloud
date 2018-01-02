package com.async.common.serial;

import io.reactivex.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/7/17 21:49
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseSerialMessage {

    private static final int REPEAT_TIME = 3;

    private MessageMode messageMode;
    private byte[] response;
    private BiConsumer<Boolean, BaseSerialMessage> onCompleted;
    private int repeatTime;

    public BaseSerialMessage() {
        this.messageMode = MessageMode.Start;
        response = null;
        this.onCompleted = null;
        repeatTime = REPEAT_TIME;
    }

    public MessageMode getMessageMode() {
        return messageMode;
    }

    public void setMessageMode(MessageMode messageMode) {
        this.messageMode = messageMode;
    }

    public abstract byte[] getRequest();

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public void setOnCompleted(BiConsumer<Boolean, BaseSerialMessage> onCompleted) {
        this.onCompleted = onCompleted;
    }

    public BiConsumer<Boolean, BaseSerialMessage> getOnCompleted() {
        return onCompleted;
    }

    public synchronized void decreaseRepeatTime() {
        repeatTime--;
    }

    public synchronized void resetRepeatTime() {
        repeatTime = REPEAT_TIME;
    }

    public synchronized int getRepeatTime() {
        return repeatTime;
    }
}