package com.async.davidconsole.ui.menu.table;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.dao.AnalogCommand;
import com.async.davidconsole.dao.gen.AnalogCommandDao;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.enums.FunctionMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/2 10:09
 * email: 10525677@qq.com
 * description:
 */
public class Spo2DataRetriever implements IDataRetriever, Consumer {

    @Inject
    DaoControl mDaoControl;

    List<String> mHeadList;
    int mRowId;

    int mRowSize;
    int mColumnSize;
    long mCurrentId;
    boolean mLastRecord;

    private volatile Consumer<List<String>> consumer;

    public Spo2DataRetriever() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        mRowSize = 6;
        mColumnSize = 3;
        mCurrentId = Long.MAX_VALUE / 2; //防止溢出;
        mLastRecord = false;

        mHeadList = new ArrayList<>();

        buildHead();
    }

    private void buildHead() {
        mHeadList.add(MainApplication.getInstance().getString(R.string.time));
        mHeadList.add(FunctionMode.Spo2.getName());
        mHeadList.add(FunctionMode.Pr.getName());
    }

    @Override
    public int getRowSize() {
        return mRowSize;
    }

    @Override
    public int getColumnSize() {
        return mColumnSize;
    }

    @Override
    public int getColoredValue() {
        return 1;
    }

    @Override
    public void goPrevious() {
        mCurrentId += mRowSize;
        Observable.just(this).observeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void goNext() {
        if (!mLastRecord) {
            mCurrentId -= mRowSize;
            Observable.just(this).observeOn(Schedulers.io()).subscribe(this);
        }
    }

    public List<String> getHeadList() {
        return mHeadList;
    }

    @Override
    public void setConsumer(Consumer<List<String>> consumer) {
        this.consumer = consumer;
    }

    /*
    * 从数据库中读取血氧数据
    * */
    @Override
    public void accept(Object o) throws Exception {
        if (consumer == null)
            return;

        final DaoSession daoSession = mDaoControl.getDaoSession();
        AnalogCommandDao analogModelDao = daoSession.getAnalogCommandDao();
        final List<AnalogCommand> analogCommandList = analogModelDao.queryBuilder()
                .orderDesc(AnalogCommandDao.Properties.Id)
                .where(AnalogCommandDao.Properties.Id.le(mCurrentId))
                .limit(mRowSize).list();

        if (analogCommandList == null || analogCommandList.size() <= 0) {
            /*没有数据*/
            mCurrentId += mRowSize;
            return;
        }

        mLastRecord = false;
        mRowId = 1;
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Observable.create((ObservableOnSubscribe<List<AnalogCommand>>) e -> {
            e.onNext(analogCommandList);
            e.onComplete();
        }).flatMap(commandList -> Observable.fromIterable(commandList))
                .map(analogModel -> {
                    List<String> record = new ArrayList<>();
                    record.add(String.valueOf(mRowId));
                    if (mRowId == 1) {
                        mCurrentId = analogModel.getId();
                    }
                    mRowId++;
                    record.add(dateFormatter.format(analogModel.getTime()));
                    record.add(String.valueOf(analogModel.getSP()));
                    record.add(String.valueOf(analogModel.getPR()));
                    return record;
                }).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> recordList) {
                Observable.just(recordList)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(this, e);
            }

            @Override
            public void onComplete() {
                /*填写空白行*/
                if (mRowId < mRowSize + 1) {
                    mLastRecord = true;
                    for (; mRowId <= mRowSize; mRowId++) {
                        List<String> record = new ArrayList<>();
                        record.add(String.valueOf(mRowId));
                        record.add("--");
                        record.add("--");
                        record.add("--");
                        Observable.just(record)
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
                    }
                }
            }
        });
    }
}
