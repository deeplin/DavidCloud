package com.async.davidconsole.controllers;

import android.app.Application;

import com.async.common.utils.LogUtil;
import com.async.common.utils.ResourceUtil;
import com.async.davidconsole.dao.SensorRange;
import com.async.davidconsole.enums.LanguageMode;
import com.async.davidconsole.serial.SerialControl;
import com.async.davidconsole.serial.SerialMessageParser;
import com.async.davidconsole.utils.Constant;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/7/8 12:23
 * email: 10525677@qq.com
 * description: Application类
 */

@Singleton
public class MainApplication extends Application {

    private static MainApplication application;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    DaoControl daoControl;
    @Inject
    SerialControl serialControl;
    @Inject
    SerialMessageParser serialMessageHandler;

    private ApplicationComponent applicationComponent;

    public static MainApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        applicationComponent = DaggerApplicationComponent.builder().build();
        applicationComponent.inject(this);

        this.start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void start() {
        try {
            moduleHardware.load();
            daoControl.start(this);
            this.setLanguage();

            serialControl.start(Constant.SERIAL_BUFFER_SIZE, serialMessageHandler);
        } catch (Exception e) {
            LogUtil.e(this, e);
            System.exit(-1);
        }
    }

    public void stop() {
        try {
            serialControl.stop();
            daoControl.stop();
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    private void setLanguage() {
        SensorRange sensorRange = daoControl.getSensorRange();
        if (sensorRange.getLanguageIndex() == LanguageMode.English.getIndex()) {
            ResourceUtil.setLocalLanguage(this, Locale.ENGLISH);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Chinese.getIndex()) {
            ResourceUtil.setLocalLanguage(this, Locale.SIMPLIFIED_CHINESE);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.SPANISH.getIndex()) {
            Locale spanish = new Locale("es", "ES");
            ResourceUtil.setLocalLanguage(this, spanish);
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
