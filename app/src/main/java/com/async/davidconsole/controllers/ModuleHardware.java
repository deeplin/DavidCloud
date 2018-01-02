package com.async.davidconsole.controllers;

import android.databinding.ObservableInt;

import com.async.common.serial.BaseSerialMessage;
import com.async.common.utils.ResourceUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.serial.command.module.ModuleGetCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/7/17 11:56
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class ModuleHardware implements BiConsumer<Boolean, BaseSerialMessage> {

    public ObservableInt updated = new ObservableInt(0);

    private boolean cameraInstalled;
    private String deviceModel;

    private int O2;
    private int HUM;
    private int SPO2;
    private int SCALE;

    @Inject
    public ModuleHardware() {
    }

    public void load() throws Exception {
        cameraInstalled = ResourceUtil.getBool(R.bool.camera_installed);
        deviceModel = ResourceUtil.getString(R.string.device_model);
    }

    public boolean isO2() {
        return O2 == 1;
    }

    public void setO2(int o2) {
        O2 = o2;
    }

    public boolean isHUM() {
        return HUM == 1;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }

    public boolean isSPO2() {
        return SPO2 == 1;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public boolean isSCALE() {
        return SCALE == 1;
    }

    public void setSCALE(int SCALE) {
        this.SCALE = SCALE;
    }

    public boolean isCameraInstalled() {
        return cameraInstalled;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) throws Exception {
        if (aBoolean) {
            ModuleGetCommand moduleSoftwareGetCommand = (ModuleGetCommand) baseSerialMessage;
            SPO2 = moduleSoftwareGetCommand.getSPO2();
            O2 = moduleSoftwareGetCommand.getO2();
            HUM = moduleSoftwareGetCommand.getHUM();
            SCALE = moduleSoftwareGetCommand.getSCALE();
            updated.set(updated.get() + 1);
        }
    }
}

