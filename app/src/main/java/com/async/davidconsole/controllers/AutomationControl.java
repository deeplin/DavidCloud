package com.async.davidconsole.controllers;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.async.common.serial.BaseSerialMessage;
import com.async.common.ui.IViewModel;
import com.async.common.utils.LogUtil;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.dao.WeightModel;
import com.async.davidconsole.dao.gen.AnalogCommandDao;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.dao.gen.WeightModelDao;
import com.async.davidconsole.enums.AlertPriorityMode;
import com.async.davidconsole.serial.SerialControl;
import com.async.davidconsole.serial.command.StatusCommand;
import com.async.davidconsole.ui.main.MainViewModel;
import com.async.davidconsole.ui.side.SideViewModel;
import com.async.davidconsole.utils.Constant;
import com.async.davidconsole.utils.SystemConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2017/7/15 13:35
 * email: 10525677@qq.com
 * description: 自动控制类
 */

@Singleton
public class AutomationControl implements IViewModel, BiConsumer<Boolean, BaseSerialMessage> {

    @Inject
    SideViewModel sideViewModel;
    @Inject
    MainViewModel mainViewModel;
    @Inject
    MessageSender messageSender;
    @Inject
    SerialControl serialControl;
    @Inject
    ShareMemory shareMemory;
    @Inject
    AlertControl alertControl;
    @Inject
    DaoControl daoControl;

    private Disposable uiDisposable;
    private Disposable ioDisposable;

    @Inject
    public AutomationControl() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        uiDisposable = null;
        ioDisposable = null;
    }

    @Override
    public void attach() {
        sendSystemConfig();
        /*设置报警*/
        shareMemory.alertID.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                String alertID = ((ObservableField<String>) observable).get();
                if (alertID.equals(Constant.SENSOR_NA)) {
                    alertControl.clearRemoteAlert();
                } else {
                    alertControl.setAlert(AlertPriorityMode.Sys_New_Alert, alertID);
                }
            }
        });

        /*刷新屏幕信息*/
        Observable<Long> observable = Observable.interval(1, 1, TimeUnit.SECONDS);
        if (uiDisposable == null) {
            uiDisposable = observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((aLong) -> {
                        mainViewModel.checkScreenLock();
                        sideViewModel.displayCurrentTime();
                    });
        }

        AnalogCommand analogCommand = new AnalogCommand();
        analogCommand.setOnCompleted(this);
        serialControl.addRepeatSession(analogCommand);
        StatusCommand statusCommand = new StatusCommand();
        statusCommand.setOnCompleted(shareMemory);
        serialControl.addRepeatSession(statusCommand);

        /*读取传感器*/
        //todo
//        if (ioDisposable == null) {
//            ioDisposable = observable
//                    .observeOn(Schedulers.io())
//                    .subscribe((aLong) -> serialControl.refresh(),
//                            (Throwable error) -> LogUtil.e(this, error));
//        }
    }

    private void sendSystemConfig() {
        /*读取配置文件*/
        messageSender.getConfig();
        messageSender.getConfig();

        /*配置37度灯*/
        shareMemory.above37.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                messageSender.setLED37(status, null);
            }
        });
        shareMemory.above37.notifyChange();
    }

    @Override
    public void detach() {
        if (ioDisposable != null) {
            ioDisposable.dispose();
            ioDisposable = null;
        }
        if (uiDisposable != null) {
            uiDisposable.dispose();
            uiDisposable = null;
        }
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) throws Exception {
        if (aBoolean) {
            if (baseSerialMessage instanceof AnalogCommand) {
                AnalogCommand analogCommand = (AnalogCommand) baseSerialMessage;
                analogCommand.setTime(System.currentTimeMillis());
                daoControl.saveModel(analogCommand);
                shareMemory.accept(true, baseSerialMessage);
            }
        }
    }
}
