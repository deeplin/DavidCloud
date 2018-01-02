package com.async.davidconsole.controllers;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableByte;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.async.common.serial.BaseSerialMessage;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.enums.AlertSettingMode;
import com.async.davidconsole.enums.AverageTimeMode;
import com.async.davidconsole.enums.CtrlMode;
import com.async.davidconsole.enums.Spo2SensMode;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.serial.command.CommandChar;
import com.async.davidconsole.serial.command.StatusCommand;
import com.async.davidconsole.serial.command.alert.AlertGetCommand;
import com.async.davidconsole.serial.command.ctrl.CtrlGetCommand;
import com.async.davidconsole.serial.command.spo2.Spo2GetCommand;
import com.async.davidconsole.utils.Constant;
import com.async.davidconsole.utils.FragmentPage;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.BiConsumer;

@Singleton
public class ShareMemory implements BiConsumer<Boolean, BaseSerialMessage> {

    /*Status Command*/
    public ObservableField<SystemMode> systemMode = new ObservableField<>(SystemMode.Init);
    public ObservableInt inc = new ObservableInt(0);
    public ObservableInt warm = new ObservableInt(0);
    public ObservableInt cTime = new ObservableInt(0);
    public ObservableField<String> alertID = new ObservableField<>(Constant.SENSOR_NA);
    /*Analog Command*/
    public ObservableInt S1B = new ObservableInt();
    public ObservableInt S2 = new ObservableInt();
    public ObservableInt A2 = new ObservableInt();
    public ObservableInt SPO2 = new ObservableInt();
    public ObservableInt PR = new ObservableInt();
    public ObservableInt O2 = new ObservableInt();
    public ObservableInt H1 = new ObservableInt();
    public ObservableInt SC = new ObservableInt(0);
    public ObservableInt PI = new ObservableInt();
    public ObservableInt VU = new ObservableInt();
    /*Ctrl Get Command*/
    public ObservableField<CtrlMode> ctrlMode = new ObservableField<>(CtrlMode.Prewarm);
    public ObservableBoolean above37 = new ObservableBoolean(false);
    public ObservableInt skinObjective = new ObservableInt();
    public ObservableInt airObjective = new ObservableInt();
    public ObservableInt oxygenObjective = new ObservableInt();
    public ObservableInt humidityObjective = new ObservableInt();
    public ObservableInt manObjective = new ObservableInt();
    /*AlertSettingMode Command*/
    public ObservableInt spo2UpperLimit = new ObservableInt();
    public ObservableInt spo2LowerLimit = new ObservableInt();
    public ObservableInt prUpperLimit = new ObservableInt();
    public ObservableInt prLowerLimit = new ObservableInt();
    /*System*/
    public ObservableBoolean lockScreen = new ObservableBoolean(false);
    public ObservableByte currentFragmentID = new ObservableByte(FragmentPage.MENU_NONE);
    /*Spo2 Get*/
    public ObservableField<Spo2SensMode> sensMode = new ObservableField<>(Spo2SensMode.Normal);
    public ObservableField<AverageTimeMode> averageTimeMode = new ObservableField<>(AverageTimeMode.Zero);
    public ObservableField<String> fastsatValue = new ObservableField<>(CommandChar.OFF);

    private int functionTag;

    private boolean login;

    @Inject
    public ShareMemory() {
        login = false;
    }

    public int getFunctionTag() {
        return this.functionTag;
    }

    public void setFunctionTag(int functionTag) {
        this.functionTag = functionTag;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) throws Exception {
        if (aBoolean) {
            if (baseSerialMessage instanceof StatusCommand) {
                StatusCommand statusCommand = (StatusCommand) baseSerialMessage;

                SystemMode system = SystemMode.getMode(statusCommand.getMode());

                //todo to be removed
                system = SystemMode.Cabin;
                systemMode.set(system);

                CtrlMode ctrl = ctrlMode.get();
                //设置预热控制模式时间
                if (ctrl.equals(CtrlMode.Prewarm) && lockScreen.get()) {
                    cTime.set(statusCommand.getCTime());
                } else {
                    cTime.set(0);
                }

                inc.set(statusCommand.getInc());
                warm.set(statusCommand.getWarm());
                alertID.set(statusCommand.getAlert());
            } else if (baseSerialMessage instanceof AnalogCommand) {
                AnalogCommand analogCommand = (AnalogCommand) baseSerialMessage;
                S1B.set(analogCommand.getS1B());
                S2.set(analogCommand.getS2());
                A2.set(analogCommand.getA2());
                O2.set(analogCommand.getO2());
                H1.set(analogCommand.getH1());

                int gap = analogCommand.getSC() - SC.get();
                if (gap > 5 || gap < -5)
                    SC.set(analogCommand.getSC());

                VU.set(analogCommand.getVU());
                VU.notifyChange();
                SPO2.set(analogCommand.getSP());
                PR.set(analogCommand.getPR());
                PI.set(analogCommand.getPI());
            } else if (baseSerialMessage instanceof CtrlGetCommand) {
                CtrlGetCommand ctrlGetCommand = (CtrlGetCommand) baseSerialMessage;
                SystemMode system = systemMode.get();
                CtrlMode ctrl = ctrlMode.get();
                CtrlMode newCtrl = CtrlMode.getMode(ctrlGetCommand.getCtrl());

                //todo to be removed
                newCtrl = CtrlMode.Skin;

                if (newCtrl.equals(CtrlMode.Standby)) {
                    if (system.equals(SystemMode.Cabin)) {
                        skinObjective.set(ctrlGetCommand.getC_skin());
                        if (ctrl.equals(CtrlMode.Prewarm) || (ctrl.equals(CtrlMode.Manual))) {
                            ctrlMode.set(CtrlMode.Air);
                        }
                    } else if (system.equals(SystemMode.Warmer)) {
                        skinObjective.set(ctrlGetCommand.getW_skin());
                        if (ctrl.equals(CtrlMode.Air))
                            ctrlMode.set(CtrlMode.Manual);
                    }
                } else {
                    ctrlMode.set(newCtrl);
                    if (system.equals(SystemMode.Cabin)) {
                        skinObjective.set(ctrlGetCommand.getC_skin());
                    } else if (system.equals(SystemMode.Warmer)) {
                        skinObjective.set(ctrlGetCommand.getW_skin());
                    }
                }

                oxygenObjective.set(ctrlGetCommand.getC_o2());
                humidityObjective.set(ctrlGetCommand.getC_hum());
                manObjective.set(ctrlGetCommand.getW_man());

                airObjective.set(ctrlGetCommand.getC_air());

                if (ctrl.equals(CtrlMode.Skin)) {
                    if (system.equals(SystemMode.Cabin)) {
                        setAbove37(ctrlGetCommand.getC_skin());
                    } else if (system.equals(SystemMode.Warmer)) {
                        setAbove37(ctrlGetCommand.getW_skin());
                    }
                } else if (ctrl.equals(CtrlMode.Air)) {
                    setAbove37(ctrlGetCommand.getC_air());
                } else {
                    above37.set(false);
                }
            } else if (baseSerialMessage instanceof AlertGetCommand) {
            /*显示Spo2 PR目标值*/
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                AlertSettingMode alertSettingMode = alertGetCommand.getAlertSettingMode();

                if (alertSettingMode.equals(AlertSettingMode.SPO2_OVH)) {
                    spo2UpperLimit.set(alertGetCommand.getLimit());
                } else if (alertSettingMode.equals(AlertSettingMode.SPO2_OVL)) {
                    spo2LowerLimit.set(alertGetCommand.getLimit());
                } else if (alertSettingMode.equals(AlertSettingMode.PR_OVH)) {
                    prUpperLimit.set(alertGetCommand.getLimit());
                } else if (alertSettingMode.equals(AlertSettingMode.PR_OVL)) {
                    prLowerLimit.set(alertGetCommand.getLimit());
                }
            } else if (baseSerialMessage instanceof Spo2GetCommand) {
                Spo2GetCommand spo2GetCommand = (Spo2GetCommand) baseSerialMessage;
                Spo2SensMode spo2SensMode = Spo2SensMode.getMode(spo2GetCommand.getSens());
                if (spo2SensMode != null) {
                    sensMode.set(spo2SensMode);
                }

                AverageTimeMode spo2AverageTimeMode = AverageTimeMode.getMode(spo2GetCommand.getAvg());
                if (spo2AverageTimeMode != null) {
                    averageTimeMode.set(spo2AverageTimeMode);
                }

                fastsatValue.set(spo2GetCommand.getFastsat());
            }
        }
    }

    private void setAbove37(int tempObjective) {
        if (tempObjective > Constant.TEMP_370) {
            above37.set(true);
        } else {
            above37.set(false);
        }
    }
}
