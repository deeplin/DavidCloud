package com.async.davidconsole.ui.menu.chart.humidity;

import android.support.v4.content.ContextCompat;

import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.IChartDataView;
import com.async.davidconsole.utils.Constant;
import com.async.davidconsole.utils.SystemConfig;

/**
 * author: Ling Lin
 * created on: 2018/1/1 11:29
 * email: 10525677@qq.com
 * description:
 */
public class ChartHumidityViewModel extends ChartBaseViewModel {

    ChartHumidityViewModel(IChartDataView chartViewModel) {
        super(chartViewModel);
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.humidity);
    }

    @Override
    public Double accept(AnalogCommand analogCommand) {
        int value = analogCommand.getH1();
        if (value < SystemConfig.HUMIDITY_DISPLAY_LOWER + 10) {
            value = SystemConfig.HUMIDITY_DISPLAY_LOWER + 10;
        } else if (value > SystemConfig.HUMIDITY_DISPLAY_UPPER) {
            value = SystemConfig.HUMIDITY_DISPLAY_UPPER;
        }
        return (value / 10.0);
    }

    @Override
    public Double accept() {
        chartDataView.initializeXAxis(getStep());
        chartDataView.initializeYAxis(100.0, 0.0, 10.0, 2.0);
        if (moduleSoftware.isHUM()) {
            return shareMemory.humidityObjective.get() / 10.0;
        }
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}
