package com.async.davidconsole.ui.menu.chart;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.ui.menu.chart.humidity.ChartHumidityLayout;
import com.async.davidconsole.ui.menu.chart.oxygen.ChartOxygenLayout;
import com.async.davidconsole.ui.menu.chart.pr.ChartPrLayout;
import com.async.davidconsole.ui.menu.chart.spo2.ChartSpo2Layout;
import com.async.davidconsole.ui.menu.chart.temp.ChartTempLayout;

/**
 * author: Ling Lin
 * created on: 2017/12/31 16:39
 * email: 10525677@qq.com
 * description:
 */
public class ChartPagerAdapter extends PagerAdapter {

    private IChartDataView chartDataView;

    private int count;
    private int[] tabArray;

    ChartPagerAdapter(boolean isCabin, IChartDataView chartDataView, ModuleHardware moduleHardware) {
        this.chartDataView = chartDataView;

        count = 0;
        tabArray = new int[5];

        tabArray[count] = 0;
        count++;

        if (isCabin) {
            if (moduleHardware.isHUM()) {
                tabArray[count] = 1;
                count++;
            }

            if (moduleHardware.isO2()) {
                tabArray[count] = 2;
                count++;
            }
        }

        if (moduleHardware.isSPO2()) {
            tabArray[count] = 3;
            count++;
            tabArray[count] = 4;
            count++;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        int tabId = tabArray[position];
        switch (tabId) {
            case 0:
                view = new ChartTempLayout(container.getContext(), chartDataView);
                break;
            case 1:
                view = new ChartHumidityLayout(container.getContext(), chartDataView);
                break;
            case 2:
                view = new ChartOxygenLayout(container.getContext(), chartDataView);
                break;
            case 3:
                view = new ChartSpo2Layout(container.getContext(), chartDataView);
                break;
            case 4:
                view = new ChartPrLayout(container.getContext(), chartDataView);
                break;
            default:
                view = new View(container.getContext());
                break;
        }

        container.addView(view);
        view.setTag(position);
        return view;
    }
}
