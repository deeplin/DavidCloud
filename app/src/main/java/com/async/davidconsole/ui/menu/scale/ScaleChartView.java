package com.async.davidconsole.ui.menu.scale;

import android.content.Context;
import android.util.AttributeSet;

import com.async.davidconsole.ui.menu.chart.SensorChartView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 23:34
 * email: 10525677@qq.com
 * description:
 */
public class ScaleChartView extends SensorChartView {

    public ScaleChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected int getLeftOffset(){
        return 80;
    }
}
