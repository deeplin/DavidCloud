package com.async.davidconsole.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.AutomationControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.databinding.ActivityMainBinding;
import com.async.davidconsole.ui.cabin.home.HomeFragment;
import com.async.davidconsole.ui.cabin.objective.ObjectiveFragment;
import com.async.davidconsole.ui.menu.MenuViewModel;
import com.async.davidconsole.ui.menu.camera.CameraFragment;
import com.async.davidconsole.ui.menu.chart.ChartFragment;
import com.async.davidconsole.ui.menu.scale.ScaleFragment;
import com.async.davidconsole.ui.spo2.Spo2Fragment;
import com.async.davidconsole.utils.FragmentPage;
import com.async.davidconsole.utils.ViewUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class MainActivity extends Activity implements MainNavigator {

    @Inject
    MainViewModel mainViewModel;
    @Inject
    MenuViewModel menuViewModel;
    @Inject
    AutomationControl automationControl;

    ActivityMainBinding activityMainBinding;

    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication.getInstance().getApplicationComponent().inject(this);
        mainViewModel.setMainNavigator(this);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(mainViewModel);
        currentFragment = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.attach();
        automationControl.attach();
    }

    @Override
    public void onPause() {
        super.onPause();
        automationControl.detach();
        mainViewModel.detach();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            MainApplication.getInstance().stop();
            super.onDestroy();
            Thread.sleep(500);
        } catch (Exception e) {
            LogUtil.e(this, e);
        } finally {
            System.exit(0);
        }
    }

    /* 鼠标电击，自动调用*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mainViewModel.initializeTimeOut();
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void changeFragment(byte position) {
        Observable.just(position)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::rotate);
    }

    /*
    * 碎片换页
    * */
    private void rotate(byte position) {
        FragmentManager fragmentManager = this.getFragmentManager();
        Fragment toFragment = fragmentManager.findFragmentByTag(String.valueOf(position));
        if (toFragment == null) {
            switch (position) {
                case FragmentPage.HOME_FRAGMENT: {
                    menuViewModel.clearButtonBorder();
                    toFragment = new HomeFragment();
                }
                break;
                case FragmentPage.OBJECTIVE_FRAGMENT: {
                    menuViewModel.clearButtonBorder();
                    toFragment = new ObjectiveFragment();
                }
                break;
                case FragmentPage.CHART_FRAGMENT: {
                    toFragment = new ChartFragment();
                }
                break;
                case FragmentPage.SPO2_FRAGMENT: {
                    toFragment = new Spo2Fragment();
                }
                break;
                case FragmentPage.SCALE_FRAGMENT: {
                    toFragment = new ScaleFragment();
                }
                break;
                case FragmentPage.CAMERA_FRAGMENT: {
                    toFragment = new CameraFragment();
                }
                break;
//                case FragmentPage.SETTING_FRAGMENT: {
//                    toFragment = new SettingFragment();
//                }
//                break;
//                case FragmentPage.ADMIN_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new AdminFragment();
//                }
//                break;
//                case FragmentPage.WARMER_HOME_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new WarmerHomeFragment();
//                }
//                break;
//                case FragmentPage.WARMER_OBJECTIVE_FRAGMENT: {
//                    menuViewModel.clearButtonBorder();
//                    toFragment = new WarmerObjectiveFragment();
//                }
//                break;
            }
            ViewUtil.changeFragment(fragmentManager, currentFragment, toFragment, position, R.id.mainHomeLayout);
            currentFragment = toFragment;
        }
    }

    @Override
    public boolean isLockableFragment() {
        return currentFragment instanceof IFragmentLockable;
    }

    //影藏右上角右下角圆角
    public void setScreenLock(boolean status) {
        if (status) {
            activityMainBinding.ivHomeTopCornerHide.setVisibility(View.VISIBLE);
            activityMainBinding.ivHomeBottomCornerHide.setVisibility(View.VISIBLE);
        } else {
            activityMainBinding.ivHomeTopCornerHide.setVisibility(View.GONE);
            activityMainBinding.ivHomeBottomCornerHide.setVisibility(View.GONE);
        }
    }
}
