package com.async.davidconsole.ui.cabin.objective.spo2;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import com.async.common.ui.ITabConstraintLayout;
import com.async.davidconsole.databinding.LayoutObjectiveSpo2Binding;
import com.async.davidconsole.utils.SystemConfig;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:58
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveSpo2Layout extends ConstraintLayout implements ObjectiveSpo2Navigator, ITabConstraintLayout {
    ObjectiveSpo2ViewModel objectiveViewModel;

    LayoutObjectiveSpo2Binding layoutObjectiveBinding;
    volatile Disposable increaseDisposable;
    volatile Disposable decreaseDisposable;

    public ObjectiveSpo2Layout(Context context, ObjectiveSpo2ViewModel objectiveViewModel) {
        super(context);
        this.objectiveViewModel = objectiveViewModel;

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutObjectiveBinding = LayoutObjectiveSpo2Binding.inflate(layoutInflater, this, true);
        layoutObjectiveBinding.setViewModel(objectiveViewModel);

        RxView.clicks(layoutObjectiveBinding.ibObjectiveOn)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setEnable(true);
                });

        RxView.clicks(layoutObjectiveBinding.ibObjectiveOff)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setEnable(false);
                });

        RxView.clicks(layoutObjectiveBinding.ibObjectiveUpper)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.upperSelected.set(true);
                });

        RxView.clicks(layoutObjectiveBinding.ibObjectiveLower)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.upperSelected.set(false);
                });

        RxView.clicks(layoutObjectiveBinding.ibObjectiveUpperValue)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.upperSelected.set(true);
                });

        RxView.clicks(layoutObjectiveBinding.ibObjectiveLowerValue)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.upperSelected.set(false);
                });

        RxView.touches(layoutObjectiveBinding.ibObjectiveUp, motionEvent -> motionEvent.getAction() != MotionEvent.ACTION_MOVE)
                .subscribe((MotionEvent motionEvent) -> {
                    synchronized (this) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            stopDisposable();
                            objectiveViewModel.increaseValue();
                            increaseDisposable = io.reactivex.Observable
                                    .interval(1000, SystemConfig.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                                    .subscribe((Long aLong) -> objectiveViewModel.increaseValue());
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            stopDisposable();
                        }
                    }
                });

        RxView.touches(layoutObjectiveBinding.ibObjectiveDown, motionEvent -> motionEvent.getAction() != MotionEvent.ACTION_MOVE)
                .subscribe((MotionEvent motionEvent) -> {
                    synchronized (this) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            stopDisposable();
                            objectiveViewModel.decreaseValue();
                            decreaseDisposable = io.reactivex.Observable
                                    .interval(1000, SystemConfig.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                                    .subscribe((Long aLong) -> objectiveViewModel.decreaseValue());
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            stopDisposable();
                        }
                    }
                });

        RxView.clicks(layoutObjectiveBinding.btObjectiveOK)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setObjective();
                });
    }

    public synchronized void stopDisposable() {
        if (decreaseDisposable != null) {
            decreaseDisposable.dispose();
            decreaseDisposable = null;
        }
        if (increaseDisposable != null) {
            increaseDisposable.dispose();
            increaseDisposable = null;
        }
    }

    @Override
    public void attach() {
        objectiveViewModel.setObjectiveNavigator(this);
        objectiveViewModel.attach();
    }

    @Override
    public void detach() {
        stopDisposable();
        objectiveViewModel.detach();
        objectiveViewModel.setObjectiveNavigator(null);
    }

    @Override
    public void enable(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                            layoutObjectiveBinding.ibObjectiveOn.setSelected(status);
                            layoutObjectiveBinding.ibObjectiveOff.setSelected(!status);
                        }
                );
    }

    @Override
    public void selectUpperValue(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                            layoutObjectiveBinding.ibObjectiveUpper.setSelected(status);
                            layoutObjectiveBinding.ibObjectiveLower.setSelected(!status);
                        }
                );
    }
}
