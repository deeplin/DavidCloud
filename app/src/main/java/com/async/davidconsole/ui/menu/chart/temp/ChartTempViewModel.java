package com.async.davidconsole.ui.menu.chart.temp;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.support.v4.content.ContextCompat;

import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.enums.CtrlMode;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.IChartDataView;
import com.async.davidconsole.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Ling Lin
 * created on: 2018/1/1 20:08
 * email: 10525677@qq.com
 * description:
 */
public class ChartTempViewModel extends ChartBaseViewModel {

    public ObservableBoolean skin1Selected = new ObservableBoolean(true);
    public ObservableBoolean skin2Selected = new ObservableBoolean(true);
    public ObservableBoolean airSelected = new ObservableBoolean(true);

    private List<Double> airSeries;
    private List<Double> skin2Series;

    private final int airColor;
    private final int skin2Color;

    ChartTempViewModel(IChartDataView chartViewModel) {
        super(chartViewModel);

        airSeries = new ArrayList<>();
        airColor = ContextCompat.getColor(MainApplication.getInstance(), R.color.c_air);
        skin2Series = new ArrayList<>();
        skin2Color = ContextCompat.getColor(MainApplication.getInstance(), R.color.skin2);
    }

    public void setNavigator(ChartTempNavigator chartTempNavigator) {
        if (chartTempNavigator != null) {
            if (shareMemory.systemMode.get().equals(SystemMode.Warmer)) {
                chartTempNavigator.setAirVisible(false);
                airSelected.set(false);
            } else {
                chartTempNavigator.setAirVisible(true);
                airSelected.set(true);
            }

            airSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    attach();
                }
            });

            skin1Selected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    attach();
                }
            });

            skin2Selected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    attach();
                }
            });
        }
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.skin1);
    }

    @Override
    public synchronized void attach() {
        airSeries.clear();
        skin2Series.clear();
        super.attach();
    }

    @Override
    public synchronized void detach() {
        super.detach();
    }

    @Override
    public Double accept(AnalogCommand analogCommand) {
        int value = analogCommand.getS1B();
        value = filterTemp(value, 202, 447);

        int air = analogCommand.getA2();
        air = filterTemp(air, 205, 450);
        airSeries.add(air / 10.0);

        int skin2 = analogCommand.getS2();
        skin2 = filterTemp(skin2, 208, 453);
        skin2Series.add(skin2 / 10.0);

        return (value / 10.0);
    }

    private int filterTemp(int value, int lowValue, int highValue) {
        if (value < lowValue) {
            value = lowValue;
        } else if (value > highValue) {
            value = highValue;
        }
        return value;
    }

    @Override
    protected void addLine() {
        if (skin1Selected.get()) {
            super.addLine();
        } else {
            chartDataView.removeLine(getColor());
        }

        if (airSelected.get()) {
            chartDataView.addLine(airColor, airSeries);
        } else {
            chartDataView.removeLine(airColor);
        }
        if (skin2Selected.get()) {
            chartDataView.addLine(skin2Color, skin2Series);
        } else {
            chartDataView.removeLine(skin2Color);
        }
    }

    @Override
    public Double accept() {
        chartDataView.initializeXAxis(getStep());
        chartDataView.initializeYAxis(45.0, 20.0, 2.5, 2.0);
        if (shareMemory.ctrlMode.get().equals(CtrlMode.Skin)) {
            return shareMemory.skinObjective.get() / 10.0;
        } else if (shareMemory.ctrlMode.get().equals(CtrlMode.Air)) {
            return shareMemory.airObjective.get() / 10.0;
        } else {
            return Constant.SENSOR_NA_VALUE * 1.0;
        }
    }
}
