package com.async.davidconsole.ui.cabin.objective.spo2;

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
import com.async.davidconsole.enums.AlertSettingMode;
import com.async.davidconsole.enums.FunctionMode;
import com.async.davidconsole.utils.Constant;
import com.async.davidconsole.utils.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:59
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveSpo2ViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    protected ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;
    @Inject
    protected MessageSender messageSender;

    public ObservableBoolean upperSelected = new ObservableBoolean(true);
    public ObservableBoolean valueChanged = new ObservableBoolean();

    public ObservableField<String> upperValueField = new ObservableField<>();
    public ObservableField<String> lowerValueField = new ObservableField<>();
    protected int upperValue = Constant.SENSOR_NA_VALUE;
    protected int lowerValue = Constant.SENSOR_NA_VALUE;

    protected final Observable.OnPropertyChangedCallback enableCallback;
    protected Observable.OnPropertyChangedCallback upperValueCallback;
    protected Observable.OnPropertyChangedCallback lowerValueCallback;

    protected int upperTopLimit;
    protected int upperBottomLimit;
    protected int lowerTopLimit;
    protected int lowerBottomLimit;

    protected SensorRange sensorRange;
    ObjectiveSpo2Navigator objectiveNavigator;

    public ObjectiveSpo2ViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        sensorRange = daoControl.getSensorRange();
        setLimit();

        enableCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean enable = getEnable();
                objectiveNavigator.enable(enable);
                valueChanged.set(false);
                setValue();
            }
        };

        upperSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                objectiveNavigator.selectUpperValue(status);
                valueChanged.set(false);
                setValue();
            }
        });
    }

    @Override
    public synchronized void attach() {
        upperValueCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                upperValue = shareMemory.spo2UpperLimit.get();
                upperValueField.set(getValueString(upperValue));
            }
        };

        lowerValueCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                lowerValue = shareMemory.spo2LowerLimit.get();
                lowerValueField.set(getValueString(lowerValue));
            }
        };
        moduleSoftware.updated.addOnPropertyChangedCallback(enableCallback);
        shareMemory.spo2UpperLimit.addOnPropertyChangedCallback(upperValueCallback);
        shareMemory.spo2LowerLimit.addOnPropertyChangedCallback(lowerValueCallback);

        moduleSoftware.updated.notifyChange();
        shareMemory.spo2UpperLimit.notifyChange();
        shareMemory.spo2LowerLimit.notifyChange();
        upperSelected.notifyChange();
    }

    @Override
    public synchronized void detach() {
        shareMemory.spo2LowerLimit.removeOnPropertyChangedCallback(lowerValueCallback);
        shareMemory.spo2UpperLimit.removeOnPropertyChangedCallback(upperValueCallback);
        moduleSoftware.updated.removeOnPropertyChangedCallback(enableCallback);
    }

    public void setObjectiveNavigator(ObjectiveSpo2Navigator objectiveNavigator) {
        this.objectiveNavigator = objectiveNavigator;
    }

    protected boolean getEnable() {
        return moduleSoftware.isSPO2();
    }

    protected String getValueString(int value) {
        return ViewUtil.formatSpo2Value(value);
    }

    protected void setLimit() {
        upperTopLimit = sensorRange.getSpo2UpperTop();
        upperBottomLimit = sensorRange.getSpo2UpperBottom();
        lowerTopLimit = sensorRange.getSpo2LowerTop();
        lowerBottomLimit = sensorRange.getSpo2LowerBottom();
    }

    protected void setValue() {
        upperValue = shareMemory.spo2UpperLimit.get() / 10 * 10;
        lowerValue = shareMemory.spo2LowerLimit.get() / 10 * 10;
        upperValueField.set(getValueString(upperValue));
        lowerValueField.set(getValueString(lowerValue));
    }

    protected int getStep() {
        return 10;
    }

    public synchronized void increaseValue() {
        if (getEnable()) {
            valueChanged.set(true);
            if (upperSelected.get()) {
                if (upperValue < upperTopLimit) {
                    upperValue += getStep();
                    upperValueField.set(getValueString(upperValue));
                }
            } else {
                if ((lowerValue + getStep()) < upperValue && lowerValue < lowerTopLimit) {
                    lowerValue += getStep();
                    lowerValueField.set(getValueString(lowerValue));
                }
            }
        }
    }

    public synchronized void decreaseValue() {
        if (getEnable()) {
            valueChanged.set(true);
            if (upperSelected.get()) {
                if ((upperValue - getStep()) > lowerValue && upperValue > upperBottomLimit) {
                    upperValue -= getStep();
                    upperValueField.set(getValueString(upperValue));
                }
            } else {
                if (lowerValue > lowerBottomLimit) {
                    lowerValue -= getStep();
                    lowerValueField.set(getValueString(lowerValue));
                }
            }
        }
    }

    public void setEnable(boolean status) {
        messageSender.setModule(status, FunctionMode.Spo2.getName(),
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getConfig();
                    }
                });
    }

    public void setObjective() {
        if (getEnable()) {
            if (upperSelected.get()) {
                messageSender.setAlertConfig(AlertSettingMode.SPO2_OVH.getName(), upperValue, (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getSpo2Alert(shareMemory);
                    }
                });
            } else {
                messageSender.setAlertConfig(AlertSettingMode.SPO2_OVL.getName(), lowerValue, (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getSpo2Alert(shareMemory);
                    }
                });
            }
        }
    }
}
