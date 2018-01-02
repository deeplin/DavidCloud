package com.async.davidconsole.ui.sensorlist;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;

import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.controllers.ModuleSoftware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.enums.CtrlMode;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.utils.TimingData;
import com.async.davidconsole.utils.ViewUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/12/31 17:59
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class SensorListViewModel implements Consumer<String> {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    TimingData timingData;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean spo2Visible = new ObservableBoolean(true);
    public ObservableBoolean oxygenVisible = new ObservableBoolean(true);
    public ObservableBoolean humidityVisible = new ObservableBoolean(true);

    SensorListNavigator sensorListNavigator;

    Observable.OnPropertyChangedCallback objectiveCallback;

    @Inject
    public SensorListViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        shareMemory.systemMode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setSensorConfig();
            }
        });

        shareMemory.A2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SystemMode systemMode = shareMemory.systemMode.get();

                if (systemMode.equals(SystemMode.Cabin)) {
                    if (sensorListNavigator != null) {
                        sensorListNavigator
                                .displayTemp1Value(ViewUtil.formatTempValue(shareMemory.A2.get()));
                    }
                }
            }
        });

        shareMemory.S1B.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (sensorListNavigator != null) {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (systemMode.equals(SystemMode.Cabin)) {
                        sensorListNavigator
                                .displayTemp2Value(ViewUtil.formatTempValue(shareMemory.S1B.get()));
                    } else if (systemMode.equals(SystemMode.Warmer)) {
                        sensorListNavigator
                                .displayTemp1Value(ViewUtil.formatTempValue(shareMemory.S1B.get()));
                    }
                }
            }
        });

        shareMemory.S2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (sensorListNavigator != null) {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (systemMode.equals(SystemMode.Warmer)) {
                        sensorListNavigator
                                .displayTemp2Value(ViewUtil.formatTempValue(shareMemory.S2.get()));
                    }
                }
            }
        });

        shareMemory.O2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (sensorListNavigator != null) {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (systemMode.equals(SystemMode.Cabin)) {
                        sensorListNavigator
                                .displayOxygenValue(ViewUtil.formatOxygenValue(shareMemory.O2.get()));
                    }
                }
            }
        });

        objectiveCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (sensorListNavigator != null) {
                    CtrlMode ctrlMode = shareMemory.ctrlMode.get();
                    if (ctrlMode.equals(CtrlMode.Skin)) {
                        SystemMode systemMode = shareMemory.systemMode.get();
                        if (systemMode.equals(SystemMode.Cabin)) {
                            sensorListNavigator.displayTemp1Objective(null);
                            sensorListNavigator.displayTemp2Objective(ViewUtil.formatTempValue(shareMemory.skinObjective.get()));
                        } else if (systemMode.equals(SystemMode.Warmer)) {
                            sensorListNavigator.displayTemp1Objective(ViewUtil.formatTempValue(shareMemory.skinObjective.get()));
                            sensorListNavigator.displayTemp2Objective(null);
                        }
                    } else if (ctrlMode.equals(CtrlMode.Air)) {
                        sensorListNavigator.displayTemp1Objective(ViewUtil.formatTempValue(shareMemory.airObjective.get()));
                        sensorListNavigator.displayTemp2Objective(null);
                    } else {
                        sensorListNavigator.displayTemp1Objective(null);
                        sensorListNavigator.displayTemp2Objective(null);
                    }
                }
            }
        };

        shareMemory.H1.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (sensorListNavigator != null) {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (systemMode.equals(SystemMode.Cabin)) {
                        sensorListNavigator
                                .displayHumidityValue(ViewUtil.formatHumidityValue(shareMemory.H1.get()));
                    }
                }
            }
        });

        shareMemory.SC.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (sensorListNavigator != null) {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (systemMode.equals(SystemMode.Warmer)) {
                        sensorListNavigator
                                .displayOxygenValue(ViewUtil.formatScaleValue(shareMemory.SC.get()));
                    }
                }
            }
        });
    }

    public void attach() {
        if (sensorListNavigator != null)
            sensorListNavigator.displayHumidityValue("--.--");

        messageSender.getCtrlGet();
        messageSender.getSpo2Alert(shareMemory);
        messageSender.getPrAlert(shareMemory);

        setSensorConfig();

        shareMemory.ctrlMode.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.A2.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S1B.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S2.addOnPropertyChangedCallback(objectiveCallback);

        shareMemory.ctrlMode.notifyChange();
        shareMemory.A2.notifyChange();
        shareMemory.S1B.notifyChange();
        shareMemory.S2.notifyChange();
        shareMemory.O2.notifyChange();
        shareMemory.H1.notifyChange();
        shareMemory.SC.notifyChange();
    }

    public void detach() {
        timingData.setConsumer(null);

        shareMemory.S2.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S1B.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.A2.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(objectiveCallback);
    }

    public void setSensorListNavigator(SensorListNavigator sensorListNavigator) {
        this.sensorListNavigator = sensorListNavigator;
    }

    /*
     * 检测设备安装
     * */
    private void setSensorConfig() {
        if (moduleHardware != null && moduleSoftware != null && sensorListNavigator != null) {
            ViewUtil.displaySensor(moduleHardware.isSPO2(), moduleSoftware.isSPO2(), spo2Visible, sensorListNavigator::spo2ShowBorder);

            SystemMode systemMode = shareMemory.systemMode.get();
            if (systemMode.equals(SystemMode.Cabin)) {
                sensorListNavigator.setBackground(true);
                ViewUtil.displaySensor(moduleHardware.isO2(), moduleSoftware.isO2(),
                        oxygenVisible, sensorListNavigator::oxygenShowBorder);
                ViewUtil.displaySensor(moduleHardware.isHUM(), moduleSoftware.isHUM(),
                        humidityVisible, sensorListNavigator::humidityShowBorder);
            } else if (systemMode.equals(SystemMode.Warmer)) {
                sensorListNavigator.setBackground(false);
                ViewUtil.displaySensor(moduleHardware.isSCALE(), moduleSoftware.isSCALE(),
                        oxygenVisible, sensorListNavigator::scaleShowBorder);
                ViewUtil.displaySensor(true, true,
                        humidityVisible, sensorListNavigator::humidityShowBorder);

                timingData.setConsumer(this);
                if (timingData.isApgarStarted()) {
                    sensorListNavigator.setTimingValue(TimingData.APGAR);
                } else if (timingData.isCprStarted()) {
                    sensorListNavigator.setTimingValue(TimingData.CPR);
                } else {
                    sensorListNavigator.setTimingValue("");
                }
            }
        }
    }

    @Override
    public void accept(String timing) throws Exception {
        if (sensorListNavigator != null)
            sensorListNavigator.displayHumidityValue(timing);
    }
}
