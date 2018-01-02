package com.async.davidconsole.ui.menu.chart;

import com.async.common.actions.Func;
import com.async.common.actions.Func2;
import com.async.common.utils.LogUtil;
import com.async.common.utils.TimeUtil;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.dao.gen.AnalogCommandDao;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.utils.SystemConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:45
 * email: 10525677@qq.com
 * description:
 */
public class ChartDataView implements IChartDataView {

    DaoControl daoControl;

    private final List<Double> dataSeries;
    private final SensorChartView sensorChartView;
    private List<AnalogCommand> analogCommandList;

    public ChartDataView(SensorChartView sensorChartView, DaoControl daoControl) {
        this.sensorChartView = sensorChartView;
        this.daoControl = daoControl;
        dataSeries = new ArrayList<>();
    }

    public void loadDataFromDatabase() {
        DaoSession daoSession = daoControl.getDaoSession();
        daoSession.clear();
        AnalogCommandDao analogModelDao = daoSession.getAnalogCommandDao();

        int limit = 120 * ChartBaseViewModel.CYCLE_VALUE_TIME[ChartBaseViewModel.CYCLE_VALUE_TIME.length - 1];
        analogCommandList = analogModelDao
                .queryBuilder().orderDesc(AnalogCommandDao.Properties.Id)
                .limit(limit).build().list();
    }

    public void initializeXAxis(int step) {
        sensorChartView.resetXAxix();
        for (int index = 1; index <= 120; index++) {
            if (index % (2 * SystemConfig.AXIS_X_DOT_PER_STEP) == 0) {
                sensorChartView.addXAxisLabel(Integer.toString(index / SystemConfig.AXIS_X_DOT_PER_STEP));
            } else {
                sensorChartView.addXAxisLabel("");
            }
        }
    }

//    public void initializeXAxis(int step) {
//        long lastTime = -1;
//        if (analogCommandList != null && analogCommandList.size() > 0) {
//            AnalogCommand analogCommand = analogCommandList.get(0);
//            lastTime = analogCommand.getTime();
//        }
//        sensorChartView.resetXAxix();
//        int count = 2;
//        for (int index = 1; index <= 120; index++) {
//            if (index % (4 * SystemConfig.AXIS_X_DOT_PER_STEP) == 0) {
//                if (lastTime > 0) {
//                    String timeString = TimeUtil.longtoDateString(lastTime - count * step, TimeUtil.AxisTime);
//                    count--;
//                    sensorChartView.addXAxisLabel(timeString);
//                } else {
//                    sensorChartView.addXAxisLabel("");
//                }
//            } else {
//                sensorChartView.addXAxisLabel("");
//            }
//        }
//    }

    @Override
    public void initializeYAxis(double max, double min, double step, double detailStep) {
        sensorChartView.setYAxisLabels(max, min, step, detailStep);
    }

    public void removeAllLine() {
        sensorChartView.clearAllDataSet();
    }

    @Override
    public void addLine(int color) {
        sensorChartView.addDataSet(dataSeries, color);
    }

    @Override
    public void addLine(int color, List<Double> series) {
        sensorChartView.addDataSet(series, color);
    }

    @Override
    public void removeLine(int color) {
        sensorChartView.clearDataSet(color);
    }

    @Override
    public void displayBackground(Func<Double> func) {
        Double objective = func.accept();
        sensorChartView.setObjectiveLines(objective);
    }

    @Override
    public void fillData(int cycleTime,
                         Func2<AnalogCommand, Double> function) {
        /*设置数据*/
        dataSeries.clear();
        int startIndex;

        if (analogCommandList.size() < cycleTime * 120) {
            startIndex = analogCommandList.size();
        } else {
            startIndex = cycleTime * 120;
        }
        for (int index = startIndex - 1, count = 0; index >= 0 && count < 120; index -= cycleTime, count++) {
            double data = function.accept(analogCommandList.get(index));
            dataSeries.add(data);
        }
    }

    @Override
    public void rePaint() {
        sensorChartView.rePaint();
    }
}