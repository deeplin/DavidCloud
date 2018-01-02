package com.async.davidconsole.ui.menu.chart.spo2;

import android.content.Context;

import com.async.davidconsole.ui.menu.chart.ChartBaseLayout;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.IChartDataView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 19:38
 * email: 10525677@qq.com
 * description:
 */
public class ChartSpo2Layout extends ChartBaseLayout {
    public ChartSpo2Layout(Context context, IChartDataView chartViewModel) {
        super(context);
        ChartBaseViewModel chartBaseViewModel = new ChartSpo2ViewModel(chartViewModel);
        super.initialize(chartBaseViewModel);
    }
}
