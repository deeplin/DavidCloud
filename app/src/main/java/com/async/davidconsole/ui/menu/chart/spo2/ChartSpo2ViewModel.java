package com.async.davidconsole.ui.menu.chart.spo2;

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
 * created on: 2018/1/1 19:39
 * email: 10525677@qq.com
 * description:
 */
public class ChartSpo2ViewModel extends ChartBaseViewModel {

    ChartSpo2ViewModel(IChartDataView chartViewModel) {
        super(chartViewModel);
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.spo2);
    }

    @Override
    public Double accept(AnalogCommand analogCommand) {
        int value = analogCommand.getSP();
        if (value < SystemConfig.SPO2_DISPLAY_LOWER) {
            value = SystemConfig.SPO2_DISPLAY_LOWER;
        } else if (value > SystemConfig.SPO2_DISPLAY_UPPER) {
            value = SystemConfig.SPO2_DISPLAY_UPPER;
        }
        return (value / 10.0);
    }

    @Override
    public Double accept() {
        chartDataView.initializeXAxis(getStep());
        chartDataView.initializeYAxis(100.0, 0.0, 10.0, 2.0);
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}