package com.async.davidconsole.ui.cabin.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.databinding.FragmentHomeBinding;
import com.async.davidconsole.enums.CtrlMode;
import com.async.davidconsole.enums.FunctionMode;
import com.async.davidconsole.utils.FragmentPage;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:54
 * email: 10525677@qq.com
 * description:
 */
public class HomeFragment extends Fragment implements HomeNavigator {

    @Inject
    HomeViewModel homeViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;

    FragmentHomeBinding fragmentHomeBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        fragmentHomeBinding.setViewModel(homeViewModel);

        View view = fragmentHomeBinding.getRoot();
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                    double x = event.getX();
                    double y = event.getY();
                    int functionTab = CtrlMode.None.getIndex();
                    if (x < 30) {
                    } else if (x < 580) {
                        if (y < 10) {
                        } else if (y < 190) {
                            functionTab = CtrlMode.Air.getIndex();
                        } else if (y < 230) {
                        } else if (y < 490) {
                            functionTab = CtrlMode.Skin.getIndex();
                        }
                    } else if (x < 630) {
                    } else if (x < 890) {
                        if (y < 10) {
                        } else if (y < 150) {
                            if (moduleHardware.isHUM())
                                functionTab = FunctionMode.Humidity.getIndex();
                        } else if (y < 180) {
                        } else if (y < 300) {
                            if (moduleHardware.isO2())
                                functionTab = FunctionMode.Oxygen.getIndex();
                        } else if (y < 330) {
                        } else if (y < 460) {
                            if (moduleHardware.isSPO2())
                                functionTab = FunctionMode.Spo2.getIndex();
                        } else if (y < 480) {
                        } else if (y < 600) {
                            if (moduleHardware.isSPO2())
                                functionTab = FunctionMode.Pr.getIndex();
                        }
                    }
                    if (!shareMemory.lockScreen.get() && functionTab != CtrlMode.None.getIndex()) {
                        shareMemory.setFunctionTag(functionTab);
                        shareMemory.currentFragmentID.set(FragmentPage.OBJECTIVE_FRAGMENT);
                    }
                    break;
                }
            }
            return true;
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.setHomeNavigator(this);
        homeViewModel.attach();

        shareMemory.A2.notifyChange();
        shareMemory.ctrlMode.notifyChange();
        shareMemory.airObjective.notifyChange();
        shareMemory.S1B.notifyChange();
        shareMemory.S2.notifyChange();
        shareMemory.skinObjective.notifyChange();
        shareMemory.H1.notifyChange();
        shareMemory.humidityObjective.notifyChange();
        shareMemory.O2.notifyChange();
        shareMemory.oxygenObjective.notifyChange();
        shareMemory.SPO2.notifyChange();
        shareMemory.spo2LowerLimit.notifyChange();
        shareMemory.spo2UpperLimit.notifyChange();
        shareMemory.PR.notifyChange();
        shareMemory.prLowerLimit.notifyChange();
        shareMemory.prUpperLimit.notifyChange();
        shareMemory.inc.notifyChange();
    }

    @Override
    public void onPause() {
        super.onPause();
        homeViewModel.detach();
        homeViewModel.setHomeNavigator(null);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setHeatStep(int step) {
        ViewGroup.LayoutParams params = fragmentHomeBinding.ivHomeHeatingHide.getLayoutParams();

        LogUtil.e(this, "HEAT SET");
        Observable.just(step)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((num) -> {
                    params.width = (int) ((100 - num) * 1.74);

                    fragmentHomeBinding.ivHomeHeatingHide.setVisibility(View.GONE);
                    fragmentHomeBinding.ivHomeHeatingHide.setVisibility(View.VISIBLE);
                    LogUtil.e(this, "HEAT SET2");
                });
    }

    @Override
    public void spo2ShowBorder(boolean status) {
        if (status) {
            fragmentHomeBinding.ivHomeSpo2Hide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
        } else {
            fragmentHomeBinding.ivHomeSpo2Hide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_spo2_hide));
        }
    }

    @Override
    public void oxygenShowBorder(boolean status) {
        if (status) {
            fragmentHomeBinding.ivHomeOxygenHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
        } else {
            fragmentHomeBinding.ivHomeOxygenHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_oxygen_hide));
        }
    }

    @Override
    public void humidityShowBorder(boolean status) {
        if (status) {
            fragmentHomeBinding.ivHomeHumidityHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.color.border));
        } else {
            fragmentHomeBinding.ivHomeHumidityHide
                    .setBackground(ContextCompat.getDrawable(this.getActivity(), R.mipmap.home_humidity_hide));
        }
    }
}
