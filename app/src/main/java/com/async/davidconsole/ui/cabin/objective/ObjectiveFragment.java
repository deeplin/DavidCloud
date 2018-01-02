package com.async.davidconsole.ui.cabin.objective;

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
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.ModuleHardware;
import com.async.davidconsole.controllers.ShareMemory;
import com.async.davidconsole.databinding.FragmentObjectiveBinding;
import com.async.davidconsole.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import devlight.io.library.ntb.NavigationTabBar;

/**
 * author: Ling Lin
 * created on: 2017/12/29 9:58
 * email: 10525677@qq.com
 * description:
 */
/*
* 目标设置碎片
* */
public class ObjectiveFragment extends Fragment {

    @Inject
    ShareMemory shareMemory;
    @Inject
    ObjectivePagerAdapter pagerAdapter;
    @Inject
    ModuleHardware hardwareConfig;

    FragmentObjectiveBinding fragmentObjectiveBinding;

    private int oldPosition = Constant.SENSOR_NA_VALUE;
    private IViewModel oldViewModel = null;
    private boolean firstTag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        fragmentObjectiveBinding = FragmentObjectiveBinding.inflate(inflater, container, false);

        View view = fragmentObjectiveBinding.getRoot();
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        int childCount = fragmentObjectiveBinding.vpObjective.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View view = fragmentObjectiveBinding.vpObjective.getChildAt(index);
            if (view != null) {
                if (view instanceof ITabConstraintLayout) {
                    if ((int) view.getTag() == oldPosition) {
                        ITabConstraintLayout tabLayout = (ITabConstraintLayout) view;
                        tabLayout.stopDisposable();
                        tabLayout.detach();
                    }
                }
            }
        }
    }

    private void buildTabPage(View view) {
        ViewGroup.LayoutParams layoutParams = fragmentObjectiveBinding.tabObjective.getLayoutParams();
        layoutParams.width = 150 * pagerAdapter.getCount();

        fragmentObjectiveBinding.vpObjective.setAdapter(pagerAdapter);
        final List<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(view.getContext(), R.mipmap.celsius),
                        ContextCompat.getColor(view.getContext(), R.color.button))
                        .build());

        if (hardwareConfig.isHUM()) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(view.getContext(), R.mipmap.humidity),
                            ContextCompat.getColor(view.getContext(), R.color.button))
                            .build()
            );
        }

        if (hardwareConfig.isO2()) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            ContextCompat.getDrawable(view.getContext(), R.mipmap.o2),
                            ContextCompat.getColor(view.getContext(), R.color.button))
                            .build()
            );
        }

        if (hardwareConfig.isSPO2()) {
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

        fragmentObjectiveBinding.tabObjective.setModels(models);
        fragmentObjectiveBinding.tabObjective.setBgColor(ContextCompat.getColor(view.getContext(), R.color.background));


        fragmentObjectiveBinding.tabObjective.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (firstTag) {
                    firstTag = false;
                    oldPosition = position;
                    oldViewModel = (IViewModel) fragmentObjectiveBinding.vpObjective.getChildAt(position);
                    oldViewModel.attach();
                }
            }

            @Override
            public void onPageSelected(final int position) {
                int childCount = fragmentObjectiveBinding.vpObjective.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View view = fragmentObjectiveBinding.vpObjective.getChildAt(index);
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
                if (state == 1) {
                    int childCount = fragmentObjectiveBinding.vpObjective.getChildCount();
                    for (int index = 0; index < childCount; index++) {
                        View view = fragmentObjectiveBinding.vpObjective.getChildAt(index);
                        if (view != null) {
                            if (view instanceof ITabConstraintLayout) {
                                ((ITabConstraintLayout) view).stopDisposable();
                            }
                        }
                    }
                }
            }
        });

        oldPosition = 0;
        int tagId = shareMemory.getFunctionTag();
        if (tagId >= 10) {
            /*根据按键设置页面*/
            int tabId = pagerAdapter.getPosition(tagId % 10);
            if (tabId > 0)
                oldPosition = tabId;
        }

        fragmentObjectiveBinding.tabObjective.setViewPager(fragmentObjectiveBinding.vpObjective, oldPosition);
        fragmentObjectiveBinding.vpObjective.setOffscreenPageLimit(1);
    }
}
