package com.async.davidconsole.ui.menu.scale;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.async.common.ui.IViewModel;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.databinding.FragmentScaleBinding;
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
 * created on: 2018/1/1 22:36
 * email: 10525677@qq.com
 * description:
 */
public class ScaleFragment extends Fragment implements IFragmentLockable {

    @Inject
    SensorListLayout sensorListLayout;

    FragmentScaleBinding fragmentScaleBinding;

    protected Disposable refreshDisposable;

    private int oldPosition = Constant.SENSOR_NA_VALUE;
    private IViewModel oldViewModel = null;
    private boolean firstTag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        fragmentScaleBinding = FragmentScaleBinding.inflate(inflater, container, false);

        View view = fragmentScaleBinding.getRoot();
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
        fragmentScaleBinding.flScaleLeft.addView(sensorListLayout);

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
        fragmentScaleBinding.flScaleLeft.removeAllViews();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        int childCount = fragmentScaleBinding.vpScale.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View view = fragmentScaleBinding.vpScale.getChildAt(index);
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

    private void buildTabPage(View view) {
        ScalePagerAdapter scalePageAdapter = new ScalePagerAdapter(fragmentScaleBinding.sensorChartView);
        fragmentScaleBinding.vpScale.setAdapter(scalePageAdapter);

        final List<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(view.getContext(), R.mipmap.chart_small),
                        ContextCompat.getColor(view.getContext(), R.color.button))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(view.getContext(), R.mipmap.data_list),
                        ContextCompat.getColor(view.getContext(), R.color.button))
                        .build()
        );

        fragmentScaleBinding.tabScale.setModels(models);
        fragmentScaleBinding.tabScale.setBgColor(ContextCompat.getColor(view.getContext(), R.color.background));

        fragmentScaleBinding.tabScale.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (firstTag) {
                    firstTag = false;
                    oldPosition = position;
                    oldViewModel = (IViewModel) fragmentScaleBinding.vpScale.getChildAt(position);
                    oldViewModel.attach();
                }
            }

            @Override
            public void onPageSelected(final int position) {
                int childCount = fragmentScaleBinding.vpScale.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View view = fragmentScaleBinding.vpScale.getChildAt(index);
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

        oldPosition = 0;
        fragmentScaleBinding.tabScale.setViewPager(fragmentScaleBinding.vpScale, oldPosition);
    }
}
