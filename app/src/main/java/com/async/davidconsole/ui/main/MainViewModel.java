package com.async.davidconsole.ui.main;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableByte;
import android.databinding.ObservableField;
import android.view.View;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.controllers.AlertControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.ui.cabin.home.HomeViewModel;
import com.async.davidconsole.ui.menu.MenuViewModel;
import com.async.davidconsole.ui.side.SideViewModel;
import com.async.davidconsole.utils.FragmentPage;
import com.async.davidconsole.utils.SystemConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:48
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class MainViewModel implements IViewModel {

    @Inject
    SideViewModel sideViewModel;
    @Inject
    MenuViewModel menuViewModel;
    @Inject
    HomeViewModel homeViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    AlertControl alertControl;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean lockScreen;
    public ObservableField<SystemMode> systemMode;
    private ObservableByte currentFragmentID;
    private MainNavigator mainNavigator;

    /*
    * 0: 屏幕解锁，开始计时
    * Constant.SCREEN_LOCK_SECOND: 时间到，自动锁屏
    * */
    private int lockTimeOut = 0;

    private Observable.OnPropertyChangedCallback systemModeCallback;
    private Observable.OnPropertyChangedCallback lockScreenCallback;
    private Observable.OnPropertyChangedCallback currentFragmentIDCallback;

    @Inject
    public MainViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        systemMode = shareMemory.systemMode;
        lockScreen = shareMemory.lockScreen;
        currentFragmentID = shareMemory.currentFragmentID;

        systemModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SystemMode system = systemMode.get();
                if (system.equals(SystemMode.Cabin)) {
                    currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    shareMemory.lockScreen.set(false);
                } else if (system.equals(SystemMode.Warmer)) {
                    currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    shareMemory.lockScreen.set(false);
                } else if (system.equals(SystemMode.Transit)) {
                    currentFragmentID.set(FragmentPage.MENU_NONE);
                    shareMemory.lockScreen.set(true);
                }
                initializeTimeOut();
            }
        };

        lockScreenCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //Status 是否锁屏
                boolean status = ((ObservableBoolean) observable).get();
                sideViewModel.setScreenLock(status);
                menuViewModel.setScreenLock(status);
                mainNavigator.setScreenLock(status);

                if (!mainNavigator.isLockableFragment()) {
                    SystemMode system = systemMode.get();
                    if (system.equals(SystemMode.Cabin)) {
                        currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    } else if (system.equals(SystemMode.Warmer)) {
                        currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    }
                }

                if (!status) {
                    //取消报警
                    alertControl.clearAlert();
                    //重新计数
                    initializeTimeOut();
                }

                //改变standby模式
                messageSender.setStandBy(status);

                //刷新系统状态
                messageSender.getCtrlGet();
            }
        };

        currentFragmentIDCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                byte position = ((ObservableByte) observable).get();
                mainNavigator.changeFragment(position);
            }
        };
    }

    @Override
    public void attach() {
        systemMode.addOnPropertyChangedCallback(systemModeCallback);
        lockScreen.addOnPropertyChangedCallback(lockScreenCallback);
        currentFragmentID.addOnPropertyChangedCallback(currentFragmentIDCallback);
    }

    @Override
    public void detach() {
        currentFragmentID.removeOnPropertyChangedCallback(currentFragmentIDCallback);
        lockScreen.removeOnPropertyChangedCallback(lockScreenCallback);
        systemMode.removeOnPropertyChangedCallback(systemModeCallback);
    }

    public void setMainNavigator(MainNavigator mainNavigator) {
        this.mainNavigator = mainNavigator;
    }

    public synchronized void lockClick() {
        if (!lockScreen.get())
            messageSender.resumeMuteAll();
        lockScreen.set(!lockScreen.get());
    }

    /* 检测是否锁屏*/
    public synchronized void checkScreenLock() {
        /*锁屏不检测*/
        if (lockScreen.get()) {
            return;
        }
        /*刷新屏幕时间*/
        lockTimeOut++;
        if (lockTimeOut >= SystemConfig.SCREEN_LOCK_SECOND) {
            lockScreen.set(true);
        }
    }

    void initializeTimeOut() {
        lockTimeOut = 0;
    }
}
