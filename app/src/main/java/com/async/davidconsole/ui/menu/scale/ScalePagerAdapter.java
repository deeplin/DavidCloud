package com.async.davidconsole.ui.menu.scale;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.ui.menu.chart.SensorChartView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 22:43
 * email: 10525677@qq.com
 * description:
 */
public class ScalePagerAdapter extends PagerAdapter {

    private SensorChartView sensorChartView;

    ScalePagerAdapter(SensorChartView sensorChartView) {
        this.sensorChartView = sensorChartView;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 实例化 一个 页卡
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;

        switch (position) {
            case 0:
                view = new ScaleLayout(container.getContext(), sensorChartView);
                break;
//            case 1:
//                ScaleDataRetriever scaleDataRetriever = new ScaleDataRetriever();
//                view = new PageTurnTable(container.getContext(), scaleDataRetriever);
//                break;
            default:
                view = new View(container.getContext());
                break;
        }

        container.addView(view);
        view.setTag(position);
        return view;
    }
}
