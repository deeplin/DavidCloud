package com.async.davidconsole.ui.menu.chart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.async.common.ui.ITabConstraintLayout;
import com.async.common.ui.IViewModel;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.databinding.FragmentChartBinding;
import com.async.davidconsole.enums.SystemMode;
import com.async.davidconsole.ui.main.IFragmentLockable;
import com.async.davidconsole.ui.sensorlist.SensorListLayout;
import com.async.davidconsole.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import devlight.io.library.ntb.NavigationTabBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:34
 * email: 10525677@qq.com
 * description:
 */
public class ChartFragment extends Fragment implements IFragmentLockable {

    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SensorListLayout sensorListLayout;
    @Inject
    DaoControl daoControl;

    FragmentChartBinding fragmentChartBinding;
    IChartDataView chartViewModel;
    protected Disposable refreshDisposable;

    private int oldPosition = Constant.SENSOR_NA_VALUE;
    private IViewModel oldViewModel = null;
    private boolean firstTag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        fragmentChartBinding = FragmentChartBinding.inflate(inflater, container, false);
        chartViewModel = new ChartDataView(fragmentChartBinding.sensorChartView, daoControl);

        View view = fragmentChartBinding.getRoot();
        buildTabPage(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentChartBinding.flChartLeft.addView(sensorListLayout);

        io.reactivex.Observable<Long> observable = io.reactivex.Observable.interval(10, 10, TimeUnit.SECONDS);
        refreshDisposable = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((aLong) -> {
                    if (oldViewModel != null) {
                        oldViewModel.attach();
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (refreshDisposable != null) {
            refreshDisposable.dispose();
            refreshDisposable = null;
        }
        fragmentChartBinding.flChartLeft.removeAllViews();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        int childCount = fragmentChartBinding.vpChart.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View view = fragmentChartBinding.vpChart.getChildAt(index);
            if (view != null) {
                if (view instanceof IViewModel) {
                    if ((int) view.getTag() == oldPosition) {
                        IViewModel viewModel = (IViewModel) view;
                        viewModel.detach();
                    }
                }
            }
        }
    }

    protected void buildTabPage(View view) {
        SystemMode systemMode = shareMemory.systemMode.get();
        boolean isCabin = systemMode.equals(SystemMode.Cabin);

        ChartPagerAdapter chartPageAdapter = new ChartPagerAdapter(isCabin, chartViewModel, moduleHardware);

        ViewGroup.LayoutParams layoutParams = fragmentChartBinding.tabChart.getLayoutParams();
        layoutParams.width = 120 * chartPageAdapter.getCount();

        fragmentChartBinding.vpChart.setAdapter(chartPageAdapter);
        final List<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(view.getContext(), R.mipmap.celsius),
                        ContextCompat.getColor(view.getContext(), R.color.button))
                        .build()
        );
        if (isCabin) {
            if (moduleHardware.isHUM())
                models.add(
                        new NavigationTabBar.Model.Builder(
                                ContextCompat.getDrawable(view.getContext(), R.mipmap.humidity),
                                ContextCompat.getColor(view.getContext(), R.color.button))
                                .build()
                );
            if (moduleHardware.isO2())
                models.add(
                        new NavigationTabBar.Model.Builder(
                                ContextCompat.getDrawable(view.getContext(), R.mipmap.o2),
                                ContextCompat.getColor(view.getContext(), R.color.button))
                                .build()
                );
        }
        if (moduleHardware.isSPO2()) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(view.getContext(), R.mipmap.spo2),
                            ContextCompat.getColor(view.getContext(), R.color.button))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(view.getContext(), R.mipmap.pr),
                            ContextCompat.getColor(view.getContext(), R.color.button))
                            .build()
            );
        }

        fragmentChartBinding.tabChart.setModels(models);
        fragmentChartBinding.tabChart.setBgColor(ContextCompat.getColor(view.getContext(), R.color.background));
        fragmentChartBinding.tabChart.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if(firstTag){
                    firstTag = false;
                    oldPosition = position;
                    oldViewModel = (IViewModel)fragmentChartBinding.vpChart.getChildAt(position);
                    oldViewModel.attach();
                }
            }

            @Override
            public void onPageSelected(final int position) {
                int childCount = fragmentChartBinding.vpChart.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View view = fragmentChartBinding.vpChart.getChildAt(index);
                    if (view != null) {
                        int tag = (int) view.getTag();
                        if (tag == oldPosition && view instanceof IViewModel) {
                            IViewModel viewModel = (IViewModel) view;
                            viewModel.detach();
                        }
                        if (tag == position && view instanceof IViewModel) {
                            IViewModel viewModel = (IViewModel) view;
                            viewModel.attach();
                            oldViewModel = viewModel;
                            oldPosition = position;
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        });
        fragmentChartBinding.tabChart.setViewPager(fragmentChartBinding.vpChart, 0);
        /*设置缓存数*/
        fragmentChartBinding.vpChart.setOffscreenPageLimit(1);
    }
}
