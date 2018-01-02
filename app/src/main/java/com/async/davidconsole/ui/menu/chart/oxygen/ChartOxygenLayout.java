package com.async.davidconsole.ui.menu.chart.oxygen;

import android.content.Context;

import com.async.davidconsole.ui.menu.chart.ChartBaseLayout;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.IChartDataView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 19:23
 * email: 10525677@qq.com
 * description:
 */
public class ChartOxygenLayout extends ChartBaseLayout {
    public ChartOxygenLayout(Context context, IChartDataView chartViewModel) {
        super(context);
        ChartBaseViewModel chartBaseViewModel = new ChartOxygenViewModel(chartViewModel);
        super.initialize(chartBaseViewModel);
    }
}
