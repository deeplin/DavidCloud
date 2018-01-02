package com.async.davidconsole.controllers;

import android.util.Log;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.dao.gen.AnalogCommandDao;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.utils.SystemConfig;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/25 13:14
 * email: 10525677@qq.com
 * description:
 */
public class DaoControlUnitTest {

    DaoControl daoControl;

    private int ANALOG_COUNT = 100;

    private int analogData = 0;
    @Test
    public void analogCommandTest() throws Exception {
        daoControl = new DaoControl();
        daoControl.start(MainApplication.getInstance());

        removeAnalogCommand(SystemConfig.ANALOG_SAVED_IN_DATABASE);
        addAnalogCommand();
        checkAnalogCommand();

        daoControl.stop();
    }

    @Test
    public void repeatAnalogCommandTest() throws Exception {
        daoControl = new DaoControl();
        daoControl.start(MainApplication.getInstance());

        removeAnalogCommand(SystemConfig.ANALOG_SAVED_IN_DATABASE);

        Observable<Long> observable = Observable.interval(1, 1, TimeUnit.SECONDS);
        observable.observeOn(Schedulers.io()).subscribe((aLong) ->{
            addOneAnalogCommand();
            checkOneAnalogCommand();
        });
//        for (int index = 0; index < ANALOG_COUNT; index++) {
//            addOneAnalogCommand();
//            checkOneAnalogCommand();
//        }
        Thread.sleep(10* 1000);
    }

    private void addAnalogCommand() {
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
        /*每分钟记录一次*/
        for (int index = 0; index < ANALOG_COUNT; index++) {
            int data = index % 100;
            AnalogCommand analogCommand = new AnalogCommand();
            analogCommand.setO1(data);
            analogCommand.setO2(data);
            analogCommand.setO3(data);
            analogCommandDao.insert(analogCommand);
            /*去除ID*/
            analogCommand.setId(null);
        }
    }

    private void addOneAnalogCommand(){
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();

        analogData ++;
        int data = analogData;
        AnalogCommand analogCommand = new AnalogCommand();
        analogCommand.setO1(data);
        analogCommand.setO2(data);
        analogCommand.setO3(data);
        analogCommandDao.insert(analogCommand);
    }

    private void checkAnalogCommand() {
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();

        List<AnalogCommand> analogCommandsList = analogCommandDao.queryBuilder()
                .build().list();

        for (int index = 0; index < ANALOG_COUNT; index++) {
            AnalogCommand analogCommand = analogCommandsList.get(index);
            int o1 = analogCommand.getO1();
            int o2 = analogCommand.getO2();
            int o3 = analogCommand.getO3();
            Assert.assertEquals(o1, index % 100);
            Assert.assertEquals(o2, index % 100);
            Assert.assertEquals(o3, index % 100);

            LogUtil.i(this, "test ok");
        }
    }

    private void checkOneAnalogCommand() {
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();

//        List<AnalogCommand> analogCommandList = analogCommandDao.
//                queryBuilder().orderDesc(AnalogCommandDao.Properties.Id).limit(1)
//                .build().list();

        AnalogCommand analogCommand = analogCommandDao.
                queryBuilder().orderDesc(AnalogCommandDao.Properties.Id).limit(1)
                .build().unique();

        int data = analogData;
        int o1 = analogCommand.getO1();
        int o2 = analogCommand.getO2();
        int o3 = analogCommand.getO3();
        Assert.assertEquals(o1, data);
        Assert.assertEquals(o2, data);
        Assert.assertEquals(o3, data);

        LogUtil.i(this,"test ok");
    }

    private void removeAnalogCommand(int newId) {
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();

        List<AnalogCommand> analogCommandsList = analogCommandDao.queryBuilder()
                .where(AnalogCommandDao.Properties.Id.le(newId))
                .build().list();
        for (AnalogCommand oldAnalogCommand : analogCommandsList) {
            analogCommandDao.delete(oldAnalogCommand);
        }
    }
}
