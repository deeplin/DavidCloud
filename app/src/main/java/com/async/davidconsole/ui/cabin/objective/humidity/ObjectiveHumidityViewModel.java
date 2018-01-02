package com.async.davidconsole.ui.cabin.objective.humidity;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ModuleSoftware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.dao.SensorRange;
import com.async.davidconsole.enums.FunctionMode;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.utils.Constant;
import com.async.davidconsole.utils.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/30 12:08
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveHumidityViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    protected ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean valueChanged = new ObservableBoolean();
    public ObservableField<String> valueField = new ObservableField<>();
    protected int value = Constant.SENSOR_NA_VALUE;

    private ObjectiveHumidityNavigator objectiveNavigator;

    private final Observable.OnPropertyChangedCallback enableCallback;

    protected final SensorRange sensorRange;

    protected int upperLimit = Constant.SENSOR_NA_VALUE;
    protected int lowerLimit = Constant.SENSOR_NA_VALUE;

    public ObjectiveHumidityViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        sensorRange = daoControl.getSensorRange();
        setLimit();

        enableCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean enable = getEnable();
                objectiveNavigator.enable(enable);
                valueChanged.set(false);
                value = getValue();
                valueField.set(getValueString());
            }
        };
    }

    @Override
    public synchronized void attach() {
        moduleSoftware.updated.addOnPropertyChangedCallback(enableCallback);
        moduleSoftware.updated.notifyChange();
    }

    @Override
    public synchronized void detach() {
        moduleSoftware.updated.removeOnPropertyChangedCallback(enableCallback);
    }

    protected boolean getEnable() {
        return moduleSoftware.isHUM();
    }

    protected int getValue() {
        return shareMemory.humidityObjective.get();
    }

    protected void setLimit() {
        upperLimit = sensorRange.getHumidityUpper();
        lowerLimit = sensorRange.getHumidityLower();
    }

    protected String getFunctionName() {
        return FunctionMode.Humidity.getName();
    }

    protected String getValueString() {
        return ViewUtil.formatHumidityValue(value);
    }

    public void setObjectiveNavigator(ObjectiveHumidityNavigator objectiveNavigator) {
        this.objectiveNavigator = objectiveNavigator;
    }

    public synchronized void increaseValue() {
        if (getEnable()) {
            if (value < upperLimit) {
                valueChanged.set(true);
                value += 10;
                valueField.set(getValueString());
            }
        }
    }

    public synchronized void decreaseValue() {
        if (getEnable()) {
            if (value > lowerLimit) {
                valueChanged.set(true);
                value -= 10;
                valueField.set(getValueString());
            }
        }
    }

    public void setEnable(boolean status) {
        messageSender.setModule(status, getFunctionName(),
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getConfig();
                    }
                });
    }

    public void setObjective() {
        if (getEnable()) {
            messageSender.setCtrlSet(SystemMode.Cabin.getName(), getFunctionName(), value, (aBoolean, baseSerialMessage) -> {
                if (aBoolean) {
                    valueChanged.set(false);
                    messageSender.getCtrlGet();
                }
            });
        }
    }
}
