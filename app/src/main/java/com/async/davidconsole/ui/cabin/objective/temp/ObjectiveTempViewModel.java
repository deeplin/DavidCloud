package com.async.davidconsole.ui.cabin.objective.temp;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.async.common.ui.IViewModel;
import com.async.common.utils.LogUtil;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.dao.SensorRange;
import com.async.davidconsole.enums.CtrlMode;
import com.async.davidconsole.enums.LanguageMode;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.utils.Constant;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/29 19:25
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveTempViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    DaoControl daoControl;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean valueChanged = new ObservableBoolean();
    public ObservableInt valueField = new ObservableInt();

    private ObjectiveTempNavigator objectiveNavigator;

    private final Observable.OnPropertyChangedCallback ctrlModeCallback;

    private final Observable.OnPropertyChangedCallback above37Callback;
    protected final SensorRange sensorRange;

    private int upperLimit = Constant.SENSOR_NA_VALUE;
    private int lowerLimit = Constant.SENSOR_NA_VALUE;

    @Inject
    public ObjectiveTempViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        sensorRange = daoControl.getSensorRange();

        ctrlModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                CtrlMode status = ((ObservableField<CtrlMode>) observable).get();
                if (objectiveNavigator != null) {
                    if (status.equals(CtrlMode.Air)) {
                        objectiveNavigator.enableAir(true);
                        valueChanged.set(false);
                        valueField.set(shareMemory.airObjective.get());
                        upperLimit = sensorRange.getAirUpper();
                        lowerLimit = sensorRange.getAirLower();
                    } else if (status.equals(CtrlMode.Skin)) {
                        objectiveNavigator.enableAir(false);
                        valueChanged.set(false);
                        valueField.set(shareMemory.skinObjective.get());
                        upperLimit = sensorRange.getSkinUpper();
                        if (sensorRange.getLanguageIndex() == LanguageMode.Chinese.getIndex()) {
                            lowerLimit = sensorRange.getSkinLowerChinese();
                        } else {
                            lowerLimit = sensorRange.getSkinLowerNonChinese();
                        }
                    }
                }
            }
        };

        above37Callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                if (objectiveNavigator != null)
                    objectiveNavigator.selectAbove37(status);
            }
        };
    }

    @Override
    public synchronized void attach() {
        shareMemory.ctrlMode.addOnPropertyChangedCallback(ctrlModeCallback);
        shareMemory.above37.addOnPropertyChangedCallback(above37Callback);

        shareMemory.ctrlMode.notifyChange();
        shareMemory.above37.notifyChange();
    }

    @Override
    public synchronized void detach() {
        shareMemory.above37.removeOnPropertyChangedCallback(above37Callback);
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(ctrlModeCallback);
    }

    public void setObjectiveNavigator(ObjectiveTempNavigator objectiveNavigator) {
        this.objectiveNavigator = objectiveNavigator;
    }

    public synchronized void increaseValue() {
        int value = valueField.get();
        if ((value > 0 && value < Constant.TEMP_370) ||
                (value >= Constant.TEMP_370 && shareMemory.above37.get())) {
            if (value < upperLimit) {
                valueChanged.set(true);
                valueField.set(value + 1);
            }
        }
    }

    public synchronized void decreaseValue() {
        int value = valueField.get();
        if ((value > 0) && (value > lowerLimit)) {
            valueChanged.set(true);
            value--;
            valueField.set(value);
            if (value <= Constant.TEMP_370) {
                shareMemory.above37.set(false);
            }
        }
    }

    public void setEnable(CtrlMode ctrlMode) {
        messageSender.setCtrlMode(ctrlMode.getName(), (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                valueChanged.set(false);
                messageSender.getCtrlGet();
                if (objectiveNavigator != null) {
                    if (ctrlMode.equals(CtrlMode.Air)) {
                        shareMemory.ctrlMode.set(CtrlMode.Air);
                        objectiveNavigator.enableAir(true);
                    } else if (ctrlMode.equals(CtrlMode.Skin)) {
                        shareMemory.ctrlMode.set(CtrlMode.Skin);
                        objectiveNavigator.enableAir(false);
                    }
                }
            }
        });
    }

    public void setObjective() {
        CtrlMode ctrlMode = shareMemory.ctrlMode.get();
        messageSender.setCtrlSet(SystemMode.Cabin.getName(), ctrlMode.getName(), valueField.get(), (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                valueChanged.set(false);
                messageSender.getCtrlGet();
            }
        });
    }
}