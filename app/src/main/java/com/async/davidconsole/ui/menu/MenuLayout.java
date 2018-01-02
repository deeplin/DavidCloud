package com.async.davidconsole.ui.menu;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.controllers.ModuleSoftware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.databinding.LayoutMenuBinding;
import com.async.davidconsole.utils.FragmentPage;
import com.async.davidconsole.utils.SystemConfig;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/26 19:13
 * email: 10525677@qq.com
 * description:
 */
public class MenuLayout extends ConstraintLayout implements MenuNavigator {

    @Inject
    MenuViewModel menuViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;

    LayoutMenuBinding layoutMenuBinding;

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        menuViewModel.setMenuNavigator(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutMenuBinding = LayoutMenuBinding.inflate(layoutInflater, this, true);
        layoutMenuBinding.setViewModel(menuViewModel);

        RxView.clicks(layoutMenuBinding.btMenuChart)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> openFragment(layoutMenuBinding.btMenuChart, FragmentPage.CHART_FRAGMENT));

        RxView.clicks(layoutMenuBinding.btMenuSpo2)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isSPO2() && moduleSoftware.isSPO2()) {
                        openFragment(layoutMenuBinding.btMenuSpo2, FragmentPage.SPO2_FRAGMENT);
                    }
                });


        RxView.clicks(layoutMenuBinding.btMenuScale)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isSCALE() && moduleSoftware.isSCALE()) {
                        openFragment(layoutMenuBinding.btMenuScale, FragmentPage.SCALE_FRAGMENT);
                    }
                });

        RxView.clicks(layoutMenuBinding.btMenuCamera)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isCameraInstalled()) {
                        openFragment(layoutMenuBinding.btMenuCamera, FragmentPage.CAMERA_FRAGMENT);
                    }
                });

        RxView.clicks(layoutMenuBinding.btMenuSetting)
                .throttleFirst(SystemConfig.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> openFragment(layoutMenuBinding.btMenuSetting, FragmentPage.SETTING_FRAGMENT));
    }

    /*
    * 打开目标碎片
    * */
    private synchronized void openFragment(ImageButton button, byte toFragmentId) {
        unSelect();
        button.setSelected(true);
        shareMemory.currentFragmentID.set(toFragmentId);
    }

    /*
    * 清楚按钮状态
    * */
    @Override
    public void clearButtonBorder() {
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> unSelect());
    }

    private void unSelect() {
        layoutMenuBinding.btMenuChart.setSelected(false);
        layoutMenuBinding.btMenuSpo2.setSelected(false);
        layoutMenuBinding.btMenuScale.setSelected(false);
        layoutMenuBinding.btMenuCamera.setSelected(false);
        layoutMenuBinding.btMenuSetting.setSelected(false);
    }

    /*
    * 锁屏
    * */
    @Override
    public void lockScreen(boolean status) {
        int visibility;
        if (status) {
            visibility = View.INVISIBLE;
        } else {
            visibility = View.VISIBLE;
        }
        Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> {
                    layoutMenuBinding.btMenuChart.setVisibility(visibility);
                    layoutMenuBinding.btMenuSpo2.setVisibility(visibility);
                    layoutMenuBinding.btMenuScale.setVisibility(visibility);
                    layoutMenuBinding.btMenuCamera.setVisibility(visibility);
                    layoutMenuBinding.btMenuSetting.setVisibility(visibility);
                });
    }
}
