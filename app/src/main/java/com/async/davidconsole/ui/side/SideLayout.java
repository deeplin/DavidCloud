package com.async.davidconsole.ui.side;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.async.common.serial.BaseSerialMessage;
import com.async.common.ui.AutoAttachConstraintLayout;
import com.async.common.utils.ReflectionUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.AlertControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.databinding.LayoutSideBinding;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.ui.main.MainViewModel;
import com.async.davidconsole.utils.FragmentPage;
import com.async.davidconsole.utils.SystemConfig;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
public class SideLayout extends AutoAttachConstraintLayout implements SideNavigator, BiConsumer<Boolean, BaseSerialMessage> {

    @Inject
    SideViewModel sideViewModel;
    @Inject
    MainViewModel mainViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    AlertControl alertControl;
    @Inject
    MessageSender messageSender;

    LayoutSideBinding layoutSideBinding;

    public SideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @Override
    public void attach() {
        sideViewModel.attach();
    }

    @Override
    public void detach() {
        sideViewModel.detach();
    }

    private void initialize() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        sideViewModel.setSideNavigator(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutSideBinding = LayoutSideBinding.inflate(layoutInflater, this, true);
        layoutSideBinding.setViewModel(sideViewModel);

        RxView.clicks(layoutSideBinding.btSideHome)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    SystemMode systemMode = shareMemory.systemMode.get();
                    if (systemMode.equals(SystemMode.Cabin)) {
                        shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    } else if (systemMode.equals(SystemMode.Warmer)) {
                        shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    }
                });

        RxView.clicks(layoutSideBinding.btSideLockScreen)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> mainViewModel.lockClick());

        RxView.clicks(layoutSideBinding.btSideStopAlarm)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object Void) -> {
                    String alertID = alertControl.getAlertID();
                    if (alertID != null) {
                        boolean longMute;
                        if (alertID.equals("SEN.O2DIF")
                                || alertID.equals("SEN.O2_1")
                                || alertID.equals("SEN.O2_2")
                                || alertID.equals("O2.DEVH")
                                || alertID.equals("O2.DEVL"))
                            longMute = false;
                        else
                            longMute = true;
                        messageSender.setMute(alertID, longMute, this);
                    }
                });
    }

    /*显示警告*/
    public void setAlertImage(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        layoutSideBinding.btSideLockScreen.setImageResource(R.mipmap.stop_alert);
                    } else {
                        if (shareMemory.lockScreen.get()) {
                            layoutSideBinding.btSideLockScreen.setImageResource(R.mipmap.screen_lock);
                        } else {
                            layoutSideBinding.btSideLockScreen.setImageResource(R.mipmap.screen_unlock);
                        }
                        layoutSideBinding.btSideStopAlarm.setImageResource(R.mipmap.alarm_started);
                    }
                });
    }

    /* 锁屏 */
    @Override
    public void lockScreen(boolean status) {
        io.reactivex.Observable.just(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((st) -> {
                    if (st) {
                        layoutSideBinding.btSideLockScreen.setSelected(true);
                        layoutSideBinding.btSideLockScreen.setImageResource(R.mipmap.screen_lock);
                        layoutSideBinding.btSideStopAlarm.setSelected(true);
                    } else {
                        layoutSideBinding.btSideLockScreen.setSelected(false);
                        layoutSideBinding.btSideLockScreen.setImageResource(R.mipmap.screen_unlock);
                        layoutSideBinding.btSideStopAlarm.setSelected(false);
                    }
                });
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) throws Exception {
        /*静音成功*/
        io.reactivex.Observable.just(R.mipmap.alarm_stopped)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((id) -> layoutSideBinding.btSideStopAlarm.setImageResource(id));
    }
}