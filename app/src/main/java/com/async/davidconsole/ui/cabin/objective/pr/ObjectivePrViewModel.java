package com.async.davidconsole.ui.cabin.objective.pr;

import android.databinding.Observable;

import com.async.davidconsole.enums.AlertSettingMode;
import com.async.davidconsole.ui.cabin.objective.spo2.ObjectiveSpo2ViewModel;
import com.async.davidconsole.utils.ViewUtil;

/**
 * author: Ling Lin
 * created on: 2017/12/30 22:39
 * email: 10525677@qq.com
 * description:
 */
public class ObjectivePrViewModel extends ObjectiveSpo2ViewModel {

    @Override
    public synchronized void attach() {
        upperValueCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                upperValue = shareMemory.prUpperLimit.get();
                upperValueField.set(getValueString(upperValue));
            }
        };

        lowerValueCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                lowerValue = shareMemory.prLowerLimit.get();
                lowerValueField.set(getValueString(lowerValue));
            }
        };

        moduleSoftware.updated.addOnPropertyChangedCallback(enableCallback);
        shareMemory.prUpperLimit.addOnPropertyChangedCallback(upperValueCallback);
        shareMemory.prLowerLimit.addOnPropertyChangedCallback(lowerValueCallback);

        moduleSoftware.updated.notifyChange();
        shareMemory.prUpperLimit.notifyChange();
        shareMemory.prLowerLimit.notifyChange();
        upperSelected.notifyChange();
    }

    @Override
    public synchronized void detach() {
        shareMemory.prLowerLimit.removeOnPropertyChangedCallback(lowerValueCallback);
        shareMemory.prUpperLimit.removeOnPropertyChangedCallback(upperValueCallback);
        moduleSoftware.updated.removeOnPropertyChangedCallback(enableCallback);
    }

    @Override
    protected String getValueString(int value) {
        return ViewUtil.formatPrValue(value);
    }

    @Override
    protected void setLimit() {
        upperTopLimit = sensorRange.getPrUpperTop();
        upperBottomLimit = sensorRange.getPrUpperBottom();
        lowerTopLimit = sensorRange.getPrLowerTop();
        lowerBottomLimit = sensorRange.getPrLowerBottom();
    }

    @Override
    protected void setValue() {
        upperValue = shareMemory.prUpperLimit.get();
        lowerValue = shareMemory.prLowerLimit.get();
        upperValueField.set(getValueString(upperValue));
        lowerValueField.set(getValueString(lowerValue));
    }

    @Override
    protected int getStep() {
        return 1;
    }

    @Override
    public void setObjective() {
        if (getEnable()) {
            if (upperSelected.get()) {
                messageSender.setAlertConfig(AlertSettingMode.PR_OVH.getName(), upperValue, (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getPrAlert(shareMemory);
                    }
                });
            } else {
                messageSender.setAlertConfig(AlertSettingMode.PR_OVL.getName(), lowerValue, (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getPrAlert(shareMemory);
                    }
                });
            }
        }
    }
}
