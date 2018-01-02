package com.async.davidconsole.controllers;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.enums.AlertSettingMode;
import com.async.davidconsole.serial.SerialControl;
import com.async.davidconsole.serial.command.LEDCommand;
import com.async.davidconsole.serial.command.alert.AlertGetCommand;
import com.async.davidconsole.serial.command.alert.AlertMuteCommand;
import com.async.davidconsole.serial.command.alert.AlertResumeAllCommand;
import com.async.davidconsole.serial.command.alert.AlertSetCommand;
import com.async.davidconsole.serial.command.calibration.CalibrateOxygenCommand;
import com.async.davidconsole.serial.command.calibration.CalibrateScaleCommand;
import com.async.davidconsole.serial.command.calibration.CalibrateTempCommand;
import com.async.davidconsole.serial.command.calibration.ShowCalibrationCommand;
import com.async.davidconsole.serial.command.ctrl.CtrlGetCommand;
import com.async.davidconsole.serial.command.ctrl.CtrlModeCommand;
import com.async.davidconsole.serial.command.ctrl.CtrlSetCommand;
import com.async.davidconsole.serial.command.ctrl.CtrlStandbyCommand;
import com.async.davidconsole.serial.command.module.ModuleGetCommand;
import com.async.davidconsole.serial.command.module.ModuleSetCommand;
import com.async.davidconsole.serial.command.spo2.Spo2GetCommand;
import com.async.davidconsole.serial.command.spo2.Spo2SetCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/7/8 12:23
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class MessageSender {

    @Inject
    SerialControl serialControl;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    ShareMemory shareMemory;

    @Inject
    public MessageSender() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    //读取下位机配置信息
    public void getConfig() {
        ModuleGetCommand moduleGetCommand = new ModuleGetCommand(true);
        moduleGetCommand.setOnCompleted(moduleHardware);
        serialControl.addSession(moduleGetCommand);
        ModuleGetCommand moduleSoftwareGetCommand = new ModuleGetCommand(false);
        moduleSoftwareGetCommand.setOnCompleted(moduleSoftware);
        serialControl.addSession(moduleSoftwareGetCommand);
    }

    public void getCtrlGet() {
        CtrlGetCommand ctrlGetCommand = new CtrlGetCommand();
        ctrlGetCommand.setOnCompleted(shareMemory);
        serialControl.addSession(ctrlGetCommand);
    }

    public void getSpo2Alert(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertGetCommand spo2OvHAlertGetCommand = new AlertGetCommand(AlertSettingMode.SPO2_OVH);
        spo2OvHAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(spo2OvHAlertGetCommand);

        AlertGetCommand spo2OvLAlertGetCommand = new AlertGetCommand(AlertSettingMode.SPO2_OVL);
        spo2OvLAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(spo2OvLAlertGetCommand);
    }

    public void getPrAlert(BiConsumer<Boolean, BaseSerialMessage> onComplete){
        AlertGetCommand prOvHAlertGetCommand = new AlertGetCommand(AlertSettingMode.PR_OVH);
        prOvHAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(prOvHAlertGetCommand);

        AlertGetCommand prOvLAlertGetCommand = new AlertGetCommand(AlertSettingMode.PR_OVL);
        prOvLAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(prOvLAlertGetCommand);
    }

    public void getSpo2Config() {
        Spo2GetCommand spo2GetCommand = new Spo2GetCommand();
        spo2GetCommand.setOnCompleted(shareMemory);
        serialControl.addSession(spo2GetCommand);
    }

    public void getCalibration(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        ShowCalibrationCommand showCalibrationCommand = new ShowCalibrationCommand();
        showCalibrationCommand.setOnCompleted(onComplete);
        serialControl.addSession(showCalibrationCommand);
    }

    public void getAlertOffset(AlertSettingMode alertSettingMode, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertGetCommand alertGetCommand = new AlertGetCommand(alertSettingMode);
        alertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertGetCommand);
    }

    public void setLED37(boolean status, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        LEDCommand ledCommand = new LEDCommand(LEDCommand.LED37, status);
        ledCommand.setOnCompleted(onComplete);
        serialControl.addSession(ledCommand);
    }

    public void setStandBy(boolean status) {
        CtrlStandbyCommand ctrlStandbyCommand = new CtrlStandbyCommand(status);
        serialControl.addSession(ctrlStandbyCommand);
    }

    public void setMute(String alertId, boolean longMute, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertMuteCommand alertMuteCommand = new AlertMuteCommand(alertId, "AB", longMute);
        alertMuteCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertMuteCommand);
    }

    public void resumeMuteAll() {
        AlertResumeAllCommand alertResumeAllCommand = new AlertResumeAllCommand();
        serialControl.addSession(alertResumeAllCommand);
    }

    public void setCtrlMode(String ctrlMode, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CtrlModeCommand controlModeCommand = new CtrlModeCommand(ctrlMode);
        controlModeCommand.setOnCompleted(onComplete);
        serialControl.addSession(controlModeCommand);
    }

    public void setCtrlSet(String mode, String ctrlMode, int target, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        if (target >= 0) {
            CtrlSetCommand ctrlSetCommand =
                    new CtrlSetCommand(mode, ctrlMode, String.valueOf(target));
            ctrlSetCommand.setOnCompleted(onComplete);
            serialControl.addSession(ctrlSetCommand);
        }
    }

    public void setModule(boolean status, String sensorName, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        /*关闭开启模块*/
        ModuleSetCommand moduleSetCommand = new ModuleSetCommand(status, sensorName);
        moduleSetCommand.setOnCompleted(onComplete);
        serialControl.addSession(moduleSetCommand);
    }

    public void setAlertConfig(String alertSetting, int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertSetCommand alertSetCommand = new AlertSetCommand(alertSetting, value);
        alertSetCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertSetCommand);
    }

    public void setAlertConfig(String alertSetting, int value1, int value2, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertSetCommand alertSetCommand = new AlertSetCommand(alertSetting, value1, value2);
        alertSetCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertSetCommand);
    }

    public void setSpo2(String target, String value) {
        Spo2SetCommand spo2SetCommand = new Spo2SetCommand(target, value);
        serialControl.addSession(spo2SetCommand);
    }

    public void setOxygen(int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CalibrateOxygenCommand calibrateO2Command1 = new CalibrateOxygenCommand(1, value);
        calibrateO2Command1.setOnCompleted(onComplete);
        serialControl.addSession(calibrateO2Command1);
        CalibrateOxygenCommand calibrateO2Command2 = new CalibrateOxygenCommand(2, value);
        calibrateO2Command2.setOnCompleted(onComplete);
        serialControl.addSession(calibrateO2Command2);
    }

    public void setScale(int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CalibrateScaleCommand calibrateScaleCommand = new CalibrateScaleCommand(value);
        calibrateScaleCommand.setOnCompleted(onComplete);
        serialControl.addSession(calibrateScaleCommand);
    }

    public void setCalibration(String id, String value) {
        CalibrateTempCommand calibrateTempCommand = new CalibrateTempCommand(id, value);
        serialControl.addSession(calibrateTempCommand);
    }
}

