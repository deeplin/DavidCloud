package com.async.davidconsole.ui.menu.chart;

import com.async.common.actions.Func;
import com.async.common.actions.Func2;
import com.async.davidconsole.dao.AnalogCommand;

import java.util.List;

/**
 * author: Ling Lin
 * created on: 2017/12/31 16:40
 * email: 10525677@qq.com
 * description:
 */
public interface IChartDataView {

    void loadDataFromDatabase();

    void fillData(int cycleTime, Func2<AnalogCommand, Double> function);

    void displayBackground(Func<Double> action);

    void initializeXAxis(int step);

    void initializeYAxis(double max, double min, double step, double detailStep);

    void addLine(int color);

    void addLine(int color, List<Double> series);

    void removeLine(int color);

    void rePaint();

    void removeAllLine();
}
