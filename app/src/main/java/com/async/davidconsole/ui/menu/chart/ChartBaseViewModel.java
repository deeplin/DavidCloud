package com.async.davidconsole.ui.menu.chart;

import android.databinding.Observable;
import android.databinding.ObservableInt;

import com.async.common.actions.Func;
import com.async.common.actions.Func2;
import com.async.common.ui.IViewModel;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.ModuleSoftware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.dao.gen.AnalogCommandDao;
import com.async.davidconsole.dao.gen.DaoSession;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:36
 * email: 10525677@qq.com
 * description:
 */
public abstract class ChartBaseViewModel implements Func<Double>,
        Func2<AnalogCommand, Double>, IViewModel {

    @Inject
    protected ShareMemory shareMemory;
    @Inject
    protected ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;

    private static final int[] CYCLE_VALUE_ARRAY = {4, 8, 12, 24, 48};
    static final int[] CYCLE_VALUE_TIME = {2, 4, 6, 12, 24};
    public ObservableInt cycleValue = new ObservableInt();
    public ObservableInt cycleIndex = new ObservableInt(0);

    protected IChartDataView chartDataView;

    public ChartBaseViewModel(IChartDataView chartDataView) {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        this.chartDataView = chartDataView;
        cycleIndex.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                attach();
            }
        });
    }

    public abstract int getColor();

    public synchronized void increase() {
        if (!isScreenLock()) {
            if (cycleIndex.get() < CYCLE_VALUE_ARRAY.length - 1) {
                cycleIndex.set(cycleIndex.get() + 1);
            }
        }
    }

    public synchronized void decrease() {
        if (!isScreenLock()) {
            if (cycleIndex.get() > 0) {
                cycleIndex.set(cycleIndex.get() - 1);
            }
        }
    }

    public void delete() {
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
        analogCommandDao.deleteAll();

        attach();
    }

    public boolean isScreenLock() {
        return shareMemory.lockScreen.get();
    }

    //x轴时间间隔
    protected int getStep() {
        int step = CYCLE_VALUE_TIME[cycleIndex.get()] * 60 * 1000;
        return step * 40;
    }

    @Override
    public synchronized void attach() {
        cycleValue.set(CYCLE_VALUE_ARRAY[cycleIndex.get()]);

        chartDataView.loadDataFromDatabase();
        chartDataView.fillData(CYCLE_VALUE_TIME[cycleIndex.get()], this);
        chartDataView.displayBackground(this);
        addLine();

        chartDataView.rePaint();
    }

    @Override
    public synchronized void detach() {
        chartDataView.removeAllLine();
    }

    protected void addLine() {
        chartDataView.addLine(getColor());
    }
}
