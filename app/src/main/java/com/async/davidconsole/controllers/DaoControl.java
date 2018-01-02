package com.async.davidconsole.controllers;

import android.content.Context;

import com.async.common.utils.ResourceUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.dao.SensorRange;
import com.async.davidconsole.dao.WeightModel;
import com.async.davidconsole.dao.gen.AnalogCommandDao;
import com.async.davidconsole.dao.gen.DaoMaster;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.dao.gen.SensorRangeDao;
import com.async.davidconsole.dao.gen.WeightModelDao;
import com.async.davidconsole.enums.LanguageMode;
import com.async.davidconsole.utils.SystemConfig;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:09
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class DaoControl {

    private DaoMaster.DevOpenHelper devOpenHelper;
    private Database database;
    private DaoSession daoSession;

    @Inject
    public DaoControl() {
    }

    public void start(Context applicationContext) throws Exception {
        devOpenHelper = new DaoMaster.DevOpenHelper(applicationContext, "DavidDatabase");
        database = devOpenHelper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();

        createSensorRangeTable();
    }

    public void stop() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null) {
            database.close();
            database = null;
        }
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    private void createSensorRangeTable() throws Exception {
        SensorRangeDao sensorRangeDao = daoSession.getSensorRangeDao();
        SensorRange sensorRange = sensorRangeDao.queryBuilder()
                .where(SensorRangeDao.Properties.Id.eq(0L))
                .unique();
        if (sensorRange == null) {
            /*读取语言设置*/
            byte languageModeId = LanguageMode.Chinese.getIndex();
            String languageIndex = ResourceUtil.getString(R.string.language);

            LanguageMode languageMode = LanguageMode.getMode(languageIndex);
            if (languageMode != null) {
                languageModeId = languageMode.getIndex();
            }
            sensorRange = new SensorRange();
            sensorRange.setLanguageIndex(languageModeId);
            sensorRangeDao.insert(sensorRange);
        }
    }

    public SensorRange getSensorRange() {
        SensorRangeDao sensorRangeDao = daoSession.getSensorRangeDao();
        return sensorRangeDao.queryBuilder()
                .where(SensorRangeDao.Properties.Id.eq(0L))
                .unique();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /*
* 保存数据到传感器
* */
    private int analogCount = 0;
    private int scaleCount = 0;

    public void saveModel(AnalogCommand analogCommand) {
        analogCount++;
        /*每分钟记录一次*/
        if (analogCount >= SystemConfig.SAVE_ANALOG_SECOND) {
            analogCount = 0;
            AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
            long newAnalogId = analogCommandDao.insert(analogCommand);
            /*去除ID*/
            analogCommand.setId(null);

            //delete old analog records
            List<AnalogCommand> analogModelsList = analogCommandDao.queryBuilder()
                    .where(AnalogCommandDao.Properties.Id.le(newAnalogId - SystemConfig.ANALOG_SAVED_IN_DATABASE))
                    .build().list();
            for (AnalogCommand oldAnalogCommand : analogModelsList) {
                analogCommandDao.delete(oldAnalogCommand);
            }

            scaleCount++;
            /*每小时记录一次*/
            if (scaleCount >= SystemConfig.SAVE_WEIGHT_SECOND) {
                scaleCount = 0;

                WeightModelDao weightModelDao = daoSession.getWeightModelDao();
                WeightModel weightModel = new WeightModel();
                int sc = analogCommand.getSC();
                weightModel.setSC(sc);
                weightModel.setTime(System.currentTimeMillis());
                long newWeightID = weightModelDao.insert(weightModel);

                //delete old scale records
                List<WeightModel> weightModelList = weightModelDao.queryBuilder()
                        .where(WeightModelDao.Properties.Id.le(newWeightID - SystemConfig.SCALE_SAVED_IN_DATABASE))
                        .build().list();
                for (WeightModel oldScaleCommand : weightModelList) {
                    weightModelDao.delete(oldScaleCommand);
                }
            }
        }
    }
}
