package com.async.davidconsole.ui.menu.chart;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;

import com.async.common.ui.IViewModel;
import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.databinding.LayoutChartBaseBinding;
import com.async.davidconsole.utils.SystemConfig;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/1 11:27
 * email: 10525677@qq.com
 * description:
 */

public abstract class ChartBaseLayout extends ConstraintLayout implements IViewModel{

    protected LayoutChartBaseBinding layoutChartBaseBinding;
    ChartBaseViewModel chartBaseViewModel;

    private boolean confirmed;

    public ChartBaseLayout(Context context) {
        super(context);
        confirmed = false;
    }

    protected void initialize(ChartBaseViewModel chartBaseViewModel) {
        this.chartBaseViewModel = chartBaseViewModel;

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutChartBaseBinding = LayoutChartBaseBinding.inflate(layoutInflater, this, true);
        layoutChartBaseBinding.setViewModel(chartBaseViewModel);

        RxView.clicks(layoutChartBaseBinding.ibChartCycleUp)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartBaseViewModel.increase());

        RxView.clicks(layoutChartBaseBinding.ibChartCycleDown)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartBaseViewModel.decrease());

        RxView.clicks(layoutChartBaseBinding.btChartDelete)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    if (!chartBaseViewModel.isScreenLock()) {
                        if (confirmed) {
                            confirmed = false;
                            chartBaseViewModel.delete();
                            layoutChartBaseBinding.btChartDelete.setText(R.string.delete);
                        } else {
                            confirmed = true;
                            layoutChartBaseBinding.btChartDelete.setText(R.string.ok);
                            io.reactivex.Observable.just(this).delay(2, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(obj -> {
                                        confirmed = false;
                                        layoutChartBaseBinding.btChartDelete.setText(R.string.delete);
                                    });
                        }
                    }
                });
    }

    @Override
    public void attach() {
        chartBaseViewModel.attach();
    }

    @Override
    public void detach() {
        chartBaseViewModel.detach();
    }
}
