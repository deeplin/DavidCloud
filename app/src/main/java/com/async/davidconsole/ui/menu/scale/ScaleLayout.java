package com.async.davidconsole.ui.menu.scale;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;

import com.async.common.ui.IViewModel;
import com.async.common.utils.ResourceUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.databinding.LayoutScaleBinding;
import com.async.davidconsole.ui.menu.chart.SensorChartView;
import com.async.davidconsole.utils.SystemConfig;
import com.async.davidconsole.utils.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/1 23:06
 * email: 10525677@qq.com
 * description:
 */
public class ScaleLayout extends ConstraintLayout implements ScaleNavigator, IViewModel {

    ScaleViewModel scaleViewModel;
    LayoutScaleBinding layoutScaleBinding;
    private boolean confirmed;

    public ScaleLayout(Context context, SensorChartView sensorChartView) {
        super(context);
        scaleViewModel = new ScaleViewModel();
        confirmed = false;

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutScaleBinding = LayoutScaleBinding.inflate(layoutInflater, this, true);
        scaleViewModel.setScaleNavigator(sensorChartView, this);

        RxView.clicks(layoutScaleBinding.btScaleGrossWeight)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (!scaleViewModel.isScreenLock())
                        scaleViewModel.grossWeight();
                });

        RxView.clicks(layoutScaleBinding.btChartDelete)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    if (!scaleViewModel.isScreenLock()) {
                        if (confirmed) {
                            confirmed = false;
                            scaleViewModel.delete();
                            layoutScaleBinding.btChartDelete.setText(R.string.delete);
                        } else {
                            confirmed = true;
                            layoutScaleBinding.btChartDelete.setText(R.string.ok);
                            io.reactivex.Observable.just(this).delay(2, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(obj -> {
                                        confirmed = false;
                                        layoutScaleBinding.btChartDelete.setText(R.string.delete);
                                    });
                        }
                    }
                });
    }

    @Override
    public synchronized void attach() {
        scaleViewModel.attach();
    }

    @Override
    public synchronized void detach() {
        scaleViewModel.detach();
    }

    @Override
    public void setWeightValue(int value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((num) -> layoutScaleBinding.tvScaleWeight.setText(
                        String.format("%s   %s", ResourceUtil.getString(R.string.current_weight),
                                ViewUtil.formatScaleValue(num))));
    }
}
