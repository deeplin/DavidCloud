package com.async.davidconsole.ui.menu.chart.humidity;

import android.content.Context;

import com.async.davidconsole.ui.menu.chart.ChartBaseLayout;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.IChartDataView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 11:29
 * email: 10525677@qq.com
 * description:
 */
public class ChartHumidityLayout extends ChartBaseLayout {
    public ChartHumidityLayout(Context context, IChartDataView chartViewModel) {
        super(context);
        ChartBaseViewModel chartBaseViewModel = new ChartHumidityViewModel(chartViewModel);
        super.initialize(chartBaseViewModel);
    }
}
