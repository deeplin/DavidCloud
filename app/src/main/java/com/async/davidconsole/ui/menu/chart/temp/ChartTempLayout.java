package com.async.davidconsole.ui.menu.chart.temp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.async.common.ui.IViewModel;
import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.databinding.LayoutChartTempBinding;
import com.async.davidconsole.ui.menu.chart.IChartDataView;
import com.async.davidconsole.utils.SystemConfig;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/1 20:08
 * email: 10525677@qq.com
 * description:
 */
public class ChartTempLayout extends ConstraintLayout implements ChartTempNavigator, IViewModel {

    ChartTempViewModel chartTempViewModel;
    LayoutChartTempBinding layoutChartTempBinding;
    private boolean confirmed;

    public ChartTempLayout(Context context, IChartDataView chartDataView) {
        super(context);

        this.chartTempViewModel = new ChartTempViewModel(chartDataView);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutChartTempBinding = LayoutChartTempBinding.inflate(layoutInflater, this, true);
        layoutChartTempBinding.setViewModel(chartTempViewModel);

        RxView.clicks(layoutChartTempBinding.ibChartCycleUp)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartTempViewModel.increase());

        RxView.clicks(layoutChartTempBinding.ibChartCycleDown)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartTempViewModel.decrease());

        RxView.clicks(layoutChartTempBinding.btChartTempAir)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    chartTempViewModel.airSelected.set(!chartTempViewModel.airSelected.get());
                    layoutChartTempBinding.btChartTempAir.setSelected(chartTempViewModel.airSelected.get());
                });

        RxView.clicks(layoutChartTempBinding.btChartTempSkin1)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    chartTempViewModel.skin1Selected.set(!chartTempViewModel.skin1Selected.get());
                    layoutChartTempBinding.btChartTempSkin1.setSelected(chartTempViewModel.skin1Selected.get());
                });

        RxView.clicks(layoutChartTempBinding.btChartTempSkin2)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    chartTempViewModel.skin2Selected.set(!chartTempViewModel.skin2Selected.get());
                    layoutChartTempBinding.btChartTempSkin2.setSelected(chartTempViewModel.skin2Selected.get());
                });

        RxView.clicks(layoutChartTempBinding.btChartDelete)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    if (!chartTempViewModel.isScreenLock()) {
                        if (confirmed) {
                            confirmed = false;
                            chartTempViewModel.delete();
                            layoutChartTempBinding.btChartDelete.setText(R.string.delete);
                        } else {
                            confirmed = true;
                            layoutChartTempBinding.btChartDelete.setText(R.string.ok);
                            io.reactivex.Observable.just(this).delay(2, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(obj -> {
                                        confirmed = false;
                                        layoutChartTempBinding.btChartDelete.setText(R.string.delete);
                                    });
                        }
                    }
                });
        layoutChartTempBinding.btChartTempSkin1.setSelected(chartTempViewModel.skin1Selected.get());
        layoutChartTempBinding.btChartTempSkin2.setSelected(chartTempViewModel.skin2Selected.get());
        layoutChartTempBinding.btChartTempAir.setSelected(chartTempViewModel.skin2Selected.get());
    }

    @Override
    public void setAirVisible(boolean status) {
        if (status) {
            layoutChartTempBinding.btChartTempAir.setVisibility(View.VISIBLE);
        } else {
            layoutChartTempBinding.btChartTempAir.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void attach() {
        chartTempViewModel.setNavigator(this);
        chartTempViewModel.attach();
    }

    @Override
    public void detach() {
        chartTempViewModel.detach();
        chartTempViewModel.setNavigator(null);
    }
}
