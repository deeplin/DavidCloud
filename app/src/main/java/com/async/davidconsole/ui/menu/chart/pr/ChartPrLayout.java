package com.async.davidconsole.ui.menu.chart.pr;

import android.content.Context;

import com.async.davidconsole.ui.menu.chart.ChartBaseLayout;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.IChartDataView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 19:47
 * email: 10525677@qq.com
 * description:
 */
public class ChartPrLayout extends ChartBaseLayout {
    public ChartPrLayout(Context context, IChartDataView chartViewModel) {
        super(context);
        ChartBaseViewModel chartBaseViewModel = new ChartPrViewModel(chartViewModel);
        super.initialize(chartBaseViewModel);
    }
}
