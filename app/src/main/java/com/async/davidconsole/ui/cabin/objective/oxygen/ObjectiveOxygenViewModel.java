package com.async.davidconsole.ui.cabin.objective.oxygen;

import com.async.davidconsole.enums.FunctionMode;
import com.async.davidconsole.ui.cabin.objective.humidity.ObjectiveHumidityViewModel;
import com.async.davidconsole.utils.ViewUtil;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:37
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveOxygenViewModel extends ObjectiveHumidityViewModel {
    protected boolean getEnable() {
        return moduleSoftware.isO2();
    }

    protected int getValue() {
        return shareMemory.oxygenObjective.get();
    }

    protected void setLimit() {
        upperLimit = sensorRange.getOxygenUpper();
        lowerLimit = sensorRange.getOxygenLower();
    }

    protected String getFunctionName() {
        return FunctionMode.Oxygen.getName();
    }

    protected String getValueString() {
        return ViewUtil.formatOxygenValue(value);
    }
}
