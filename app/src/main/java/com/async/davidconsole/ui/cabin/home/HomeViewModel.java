package com.async.davidconsole.ui.cabin.home;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.controllers.ModuleSoftware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.enums.CtrlMode;
import com.async.davidconsole.utils.ViewUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:54
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class HomeViewModel implements IViewModel {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    public ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean spo2Visible = new ObservableBoolean(true);
    public ObservableBoolean oxygenVisible = new ObservableBoolean(true);
    public ObservableBoolean humidityVisible = new ObservableBoolean(true);

    private HomeNavigator homeNavigator;
    private Observable.OnPropertyChangedCallback heatCallback;
    private Observable.OnPropertyChangedCallback settingUpdateCallback;

    @Inject
    public HomeViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        heatCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (homeNavigator != null) {
                    int heat = ((ObservableInt) observable).get();
                    homeNavigator.setHeatStep(heat);
                }
            }
        };
        settingUpdateCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setEnabledSensor();
            }
        };
    }

    @Override
    public void attach() {
        moduleHardware.updated.addOnPropertyChangedCallback(settingUpdateCallback);
        moduleSoftware.updated.addOnPropertyChangedCallback(settingUpdateCallback);
        moduleHardware.updated.notifyChange();

        shareMemory.inc.addOnPropertyChangedCallback(heatCallback);

        messageSender.getCtrlGet();
        messageSender.getSpo2Alert(shareMemory);
        messageSender.getPrAlert(shareMemory);

        shareMemory.inc.notifyChange();

        if (shareMemory.ctrlMode.get().equals(CtrlMode.Prewarm)) {
            shareMemory.ctrlMode.set(CtrlMode.Air);
        }
        shareMemory.ctrlMode.notifyChange();
    }

    @Override
    public void detach() {
        shareMemory.inc.removeOnPropertyChangedCallback(heatCallback);
        moduleSoftware.updated.removeOnPropertyChangedCallback(settingUpdateCallback);
        moduleHardware.updated.addOnPropertyChangedCallback(settingUpdateCallback);
    }

    void setHomeNavigator(HomeNavigator homeNavigator) {
        this.homeNavigator = homeNavigator;
    }

    /*
    * 检测设备安装
    * */
    private void setEnabledSensor() {
        if (moduleHardware != null && moduleSoftware != null && homeNavigator != null) {
            ViewUtil.displaySensor(moduleHardware.isSPO2(), moduleSoftware.isSPO2(),
                    spo2Visible, homeNavigator::spo2ShowBorder);
            ViewUtil.displaySensor(moduleHardware.isO2(), moduleSoftware.isO2(),
                    oxygenVisible, homeNavigator::oxygenShowBorder);
            ViewUtil.displaySensor(moduleHardware.isHUM(), moduleSoftware.isHUM(),
                    humidityVisible, homeNavigator::humidityShowBorder);
        }
    }
}
