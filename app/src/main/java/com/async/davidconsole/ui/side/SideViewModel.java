package com.async.davidconsole.ui.side;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.async.common.ui.IViewModel;
import com.async.common.utils.TimeUtil;
import com.async.davidconsole.controllers.AlertControl;
import com.async.davidconsole.controllers.MainApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class SideViewModel implements IViewModel{

    @Inject
    AlertControl alertControl;

    public ObservableField<String> date = new ObservableField<>("0000-00-00");
    public ObservableField<String> time = new ObservableField<>("--:-- am");
    public ObservableField<String> weekDay = new ObservableField<>("----");
    public ObservableBoolean screenLock = new ObservableBoolean(false);

    private Observable.OnPropertyChangedCallback alertCallback;

    private SideNavigator sideNavigator;

    @Inject
    public SideViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        alertCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                sideNavigator.setAlertImage(alertControl.isAlert());
            }
        };
    }

    @Override
    public void attach() {
        alertControl.alertStringField.addOnPropertyChangedCallback(alertCallback);
    }

    @Override
    public void detach() {
        alertControl.alertStringField.removeOnPropertyChangedCallback(alertCallback);
    }

    public void setSideNavigator(SideNavigator sideNavigator) {
        this.sideNavigator = sideNavigator;
    }

    public void displayCurrentTime() {
        this.date.set(TimeUtil.getCurrentDate(TimeUtil.Date));
        this.time.set(TimeUtil.getCurrentEnglishDate(TimeUtil.Minute));
        this.weekDay.set(TimeUtil.getCurrentEnglishDate(TimeUtil.WeekDay));
    }

    public void setScreenLock(boolean status) {
        screenLock.set(status);
        sideNavigator.lockScreen(status);
    }
}
