package com.async.davidconsole.ui.sensorlist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.async.common.ui.AutoAttachConstraintLayout;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.databinding.LayoutSensorListBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:32
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class SensorListLayout extends AutoAttachConstraintLayout implements SensorListNavigator {

    @Inject
    SensorListViewModel sensorListViewModel;

    LayoutSensorListBinding layoutSensorListBinding;

    @Inject
    public SensorListLayout() {
        super(MainApplication.getInstance());

        MainApplication.getInstance().getApplicationComponent().inject(this);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutSensorListBinding = LayoutSensorListBinding.inflate(layoutInflater, this, true);
        layoutSensorListBinding.setViewModel(sensorListViewModel);
    }

    @Override
    public void attach() {
        sensorListViewModel.setSensorListNavigator(this);
        sensorListViewModel.attach();
    }

    @Override
    public void detach() {
        sensorListViewModel.detach();
        sensorListViewModel.setSensorListNavigator(null);
    }

    @Override
    public void setBackground(boolean isCabin) {
        io.reactivex.Observable.just(isCabin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        this.setBackgroundResource(R.mipmap.sensor_list_background);
                        layoutSensorListBinding.tvSensorListOxygenObjective.setVisibility(View.VISIBLE);
                        layoutSensorListBinding.tvSensorListHumidityObjective.setVisibility(View.VISIBLE);
                    } else {
                        this.setBackgroundResource(R.mipmap.heating_sensor_list_background);
                        layoutSensorListBinding.tvSensorListOxygenObjective.setVisibility(View.GONE);
                        layoutSensorListBinding.tvSensorListHumidityObjective.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void spo2ShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        layoutSensorListBinding.ivSensorListSpo2Hide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.color.border));
                    } else {
                        layoutSensorListBinding.ivSensorListSpo2Hide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.mipmap.sensor_list_spo2_hide));
                    }
                });
    }

    @Override
    public void oxygenShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        layoutSensorListBinding.ivSensorListOxygenHide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.color.border));
                    } else {
                        layoutSensorListBinding.ivSensorListOxygenHide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.mipmap.sensor_list_o2_hide));
                    }
                });
    }

    @Override
    public void scaleShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        layoutSensorListBinding.ivSensorListOxygenHide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.color.border));
                    } else {
                        layoutSensorListBinding.ivSensorListOxygenHide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.mipmap.sensor_list_scale_hide));
                    }
                });
    }

    @Override
    public void humidityShowBorder(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        layoutSensorListBinding.ivSensorListHumidityHide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.color.border));
                    } else {
                        layoutSensorListBinding.ivSensorListHumidityHide
                                .setBackground(ContextCompat.getDrawable(layoutSensorListBinding.getRoot().getContext(), R.mipmap.sensor_list_humidity_hide));
                    }
                });
    }

    @Override
    public void displayOxygenValue(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> layoutSensorListBinding.tvSensorListOxygen.setText(text));
    }

    @Override
    public void displayHumidityValue(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> layoutSensorListBinding.tvSensorListHumidity.setText(text));
    }

    @Override
    public void setTimingValue(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> layoutSensorListBinding.tvSensorListTiming.setText(text));
    }

    @Override
    public void displayTemp1Value(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> layoutSensorListBinding.tvSensorListTemp1.setText(text));
    }

    @Override
    public void displayTemp1Objective(String value) {
        int visibility;
        String text;
        if (value != null) {
            visibility = View.VISIBLE;
            text = value;
        } else {
            visibility = View.INVISIBLE;
            text = "";
        }
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> {
                    layoutSensorListBinding.ivSensorListTemp1Set.setVisibility(visibility);
                    layoutSensorListBinding.tvSensorListTemp1Objective.setText(text);
                });
    }

    @Override
    public void displayTemp2Value(String value) {
        io.reactivex.Observable.just(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((text) -> layoutSensorListBinding.tvSensorListTemp2.setText(text));
    }

    @Override
    public void displayTemp2Objective(String value) {
        int visibility;
        String text;
        if (value != null) {
            visibility = View.VISIBLE;
            text = value;
        } else {
            visibility = View.INVISIBLE;
            text = "";
        }
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> {
                    layoutSensorListBinding.ivSensorListTemp2Set.setVisibility(visibility);
                    layoutSensorListBinding.tvSensorListTemp2Objective.setText(text);
                });
    }
}
