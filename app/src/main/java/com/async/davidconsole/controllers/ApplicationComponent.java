package com.async.davidconsole.controllers;

import com.async.davidconsole.serial.SerialMessageParser;
import com.async.davidconsole.ui.cabin.home.HomeFragment;
import com.async.davidconsole.ui.cabin.home.HomeViewModel;
import com.async.davidconsole.ui.cabin.objective.ObjectiveFragment;
import com.async.davidconsole.ui.cabin.objective.ObjectivePagerAdapter;
import com.async.davidconsole.ui.cabin.objective.humidity.ObjectiveHumidityViewModel;
import com.async.davidconsole.ui.cabin.objective.spo2.ObjectiveSpo2ViewModel;
import com.async.davidconsole.ui.cabin.objective.temp.ObjectiveTempLayout;
import com.async.davidconsole.ui.cabin.objective.temp.ObjectiveTempViewModel;
import com.async.davidconsole.ui.main.MainActivity;
import com.async.davidconsole.ui.main.MainViewModel;
import com.async.davidconsole.ui.menu.MenuLayout;
import com.async.davidconsole.ui.menu.camera.CameraFragment;
import com.async.davidconsole.ui.menu.chart.ChartBaseViewModel;
import com.async.davidconsole.ui.menu.chart.ChartFragment;
import com.async.davidconsole.ui.menu.scale.ScaleFragment;
import com.async.davidconsole.ui.menu.scale.ScaleViewModel;
import com.async.davidconsole.ui.menu.table.ScaleDataRetriever;
import com.async.davidconsole.ui.menu.table.Spo2DataRetriever;
import com.async.davidconsole.ui.sensorlist.SensorListLayout;
import com.async.davidconsole.ui.sensorlist.SensorListViewModel;
import com.async.davidconsole.ui.side.SideLayout;
import com.async.davidconsole.ui.side.SideViewModel;
import com.async.davidconsole.ui.spo2.Spo2Fragment;
import com.async.davidconsole.ui.top.TopLayout;
import com.async.davidconsole.ui.top.TopViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author: Ling Lin
 * created on: 2017/7/8 12:24
 * email: 10525677@qq.com
 * description: Dagger component类
 */

@Singleton
@Component
public interface ApplicationComponent {

    void inject(MainApplication mainApplication);

    void inject(MainActivity mainActivity);

    void inject(TopLayout alertLayout);

    void inject(AutomationControl automationControl);

    void inject(SideLayout sideLayout);

    void inject(MainViewModel mainViewModel);

    void inject(MenuLayout menuLayout);

    void inject(SideViewModel sideViewModel);

    void inject(HomeFragment homeFragment);

    void inject(HomeViewModel homeViewModel);

    void inject(SerialMessageParser serialMessageParser);

    void inject(ObjectiveFragment objectiveFragment);

    void inject(ObjectiveTempLayout objectiveSkinLayout);

    void inject(ObjectiveTempViewModel objectiveViewModel);

    void inject(ObjectiveHumidityViewModel objectiveHumidityViewModel);

    void inject(ObjectiveSpo2ViewModel objectiveSpo2ViewModel);

    void inject(ObjectivePagerAdapter objectivePagerAdapter);

    void inject(ChartFragment chartFragment);

    void inject(SensorListLayout sensorListLayout);

    void inject(SensorListViewModel sensorListViewModel);

    void inject(ChartBaseViewModel chartBaseViewModel);

    void inject(ScaleFragment scaleFragment);

    void inject(ScaleViewModel scaleViewModel);

    void inject(CameraFragment cameraFragment);

    //    void inject(SettingFragment settingFragment);
//
//    void inject(UserCalibrationLayout userCalibration);
//
//    void inject(LoginLayout loginLayout);
//
//    void inject(ChartBaseOtherViewModel chartBaseOtherViewModel);
//
//    void inject(AdminFragment adminFragment);
//
//    void inject(WarmerHomeFragment stageHomeFragment);
//
//    void inject(WarmerViewModel stageViewModel);
//
//    void inject(HeatingTimingView heatingTimingView);
//
//    void inject(WarmerObjectiveFragment stageObjectiveFragment);
//
//    void inject(StageOtherPagerAdapter stageOtherPagerAdapter);
//
//    void inject(WarmerObjectiveSkinLayout stageObjectiveSkinLayout);
//
//    void inject(WarmerObjectivePreWarmViewModel stageObjectivePreWarmViewModel);
//
//    void inject(WarmerObjectivePreWarmLayout stageObjectivePreWarmLayout);
//
//    void inject(WarmerObjectiveManualViewModel stageObjectiveManualViewModel);
//
//    void inject(WarmerObjectiveManualLayout stageObjectiveManualLayout);
//
//    void inject(StageObjectiveTimingLayout stageObjectiveTimingLayout);
//
//    void inject(ScaleLayout scaleLayout);
//
    void inject(ScaleDataRetriever scaleDataRetriever);

    void inject(Spo2Fragment spo2Fragment);

//    void inject(Spo2ViewModel spo2ViewModel);
//
//    void inject(Spo2ViewLayout spo2ViewLayout);
//
//    void inject(Spo2SettingSensLayout spo2SettingSensLayout);
//
//    void inject(Spo2SettingAverageTimeLayout spo2SettingAverageTimeLayout);
//
//    void inject(Spo2SettingFastSATLayout spo2SettingFastSATLayout);

    void inject(Spo2DataRetriever spo2DataRetriever);

//    void inject(Spo2ChartSurfaceView spo2ChartSurfaceView);
//
//    void inject(AdminCalibrationViewModel adminCalibrationViewModel);
//
//    void inject(AdminSensorRangeLayout adminSensorRangeLayout);
//
//    void inject(AdminLanguageLayout adminLanguageLayout);
//
    void inject(TopViewModel alertViewModel);

    //
//    void inject(AdminOffsetWarningViewModel adminOffsetWarningViewModel);
//
//    void inject(PrintViewModel printViewModel);
//
    void inject(MessageSender messageSender);

//    void inject(CloudMessageParser cloudMessageParser);
//
//    void inject(LocationControl locationControl);
}