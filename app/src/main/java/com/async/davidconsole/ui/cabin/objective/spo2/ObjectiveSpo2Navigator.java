package com.async.davidconsole.ui.cabin.objective.spo2;

import com.async.davidconsole.ui.cabin.objective.humidity.ObjectiveHumidityNavigator;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:59
 * email: 10525677@qq.com
 * description:
 */
public interface ObjectiveSpo2Navigator extends ObjectiveHumidityNavigator {
    void selectUpperValue(boolean status);
}