package com.async.davidconsole.ui.menu.scale;

import android.databinding.Observable;
import android.databinding.ObservableInt;
import android.support.v4.content.ContextCompat;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.dao.WeightModel;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.dao.gen.WeightModelDao;
import com.async.davidconsole.ui.menu.chart.SensorChartView;
import com.async.davidconsole.utils.SystemConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2018/1/1 22:36
 * email: 10525677@qq.com
 * description:
 */
public class ScaleViewModel implements IViewModel {

    @Inject
    DaoControl daoControl;
    @Inject
    ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    /*x轴数据*/
    private List<Double> dataSeries;
    private SensorChartView sensorChartView;
    private List<WeightModel> weightModelList;

    private final int maxXDot;
    private final int dataColor;
    private final double axisMinY;

    private ScaleNavigator scaleNavigator;
    private Observable.OnPropertyChangedCallback scaleCallback;

    protected Disposable refreshDisposable;

    @Inject
    ScaleViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        maxXDot = 50;
        dataColor = ContextCompat.getColor(MainApplication.getInstance(), R.color.scale);
        dataSeries = new ArrayList<>();
        axisMinY = 0.0;

        scaleCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (scaleNavigator != null) {
                    int weight = ((ObservableInt) observable).get();
                    scaleNavigator.setWeightValue(weight);
                }
            }
        };
    }

    @Override
    public void attach() {
        reload();
    }

    @Override
    public void detach() {
        shareMemory.SC.removeOnPropertyChangedCallback(scaleCallback);

        if (refreshDisposable != null) {
            refreshDisposable.dispose();
            refreshDisposable = null;
        }
    }

    void setScaleNavigator(SensorChartView sensorChartView, ScaleNavigator scaleNavigator) {
        this.scaleNavigator = scaleNavigator;
        this.sensorChartView = sensorChartView;

        shareMemory.SC.addOnPropertyChangedCallback(scaleCallback);
        shareMemory.SC.notifyChange();

        initializeXAxis();
        initializeYAxis();
    }

    private void reload() {
        loadDataFromDatabase();

        fillData();
        displayLine();
        sensorChartView.rePaint();
    }

    void grossWeight() {
        if (!shareMemory.lockScreen.get()) {
            messageSender.setScale(0, null);
        }
    }

    void delete() {
        DaoSession daoSession = daoControl.getDaoSession();
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        weightModelDao.deleteAll();

        reload();
    }

    private void loadDataFromDatabase() {
        DaoSession daoSession = daoControl.getDaoSession();
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();

        //读取49天（7周）数据
        int limit = 24 * maxXDot;
        weightModelList = weightModelDao
                .queryBuilder().orderDesc(WeightModelDao.Properties.Id)
                .limit(limit).build().list();
    }

    private void initializeXAxis() {
        //读取49天（7周）数据 一天一点
        for (int index = 1; index <= maxXDot; index++) {
            if (index % 10 == 0) {
                sensorChartView.addXAxisLabel(Integer.toString(index));
            } else {
                sensorChartView.addXAxisLabel("");
            }
        }
    }

    private void initializeYAxis() {
        sensorChartView.setYAxisLabels(8000.0, 0.0, 500.0, 2.0);
    }

    private void fillData() {
        /*设置数据*/
        dataSeries.clear();
        int size = weightModelList.size();
        for (int index = size - 1; index >= 0; index -= 24) {
            double data = weightModelList.get(index).getSC();
            if (data < SystemConfig.SCALE_DISPLAY_LOWER + 500) {
                data = SystemConfig.SCALE_DISPLAY_LOWER + 500;
            } else if (data > SystemConfig.SCALE_DISPLAY_UPPER) {
                data = SystemConfig.SCALE_DISPLAY_UPPER;
            }
            dataSeries.add(data);
        }
    }

    /*填满120个数据*/
    private void fillData(List<Double> series) {
        int size = series.size();
        if (size < maxXDot) {
            for (int index = 0; index < maxXDot - size; index++) {
                series.add(axisMinY);
            }
        }
    }

    private void displayLine() {
        fillData(dataSeries);
        sensorChartView.addDataSet(dataSeries, dataColor);
    }

    public boolean isScreenLock() {
        return shareMemory.lockScreen.get();
    }
}