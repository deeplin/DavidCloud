package com.async.davidconsole.ui.menu.table;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.DaoControl;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.dao.WeightModel;
import com.async.davidconsole.dao.gen.DaoSession;
import com.async.davidconsole.dao.gen.WeightModelDao;

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
 * created on: 2018/1/2 10:07
 * email: 10525677@qq.com
 * description:
 */
public class ScaleDataRetriever implements IDataRetriever, Consumer {

    @Inject
    DaoControl mDaoControl;

    List<String> mHeadList;
    int mRowSize;
    int mColumnSize;

    int mRowId;
    long mCurrentId;
    boolean mLastRecord;

    SimpleDateFormat mDateFormatter;
    SimpleDateFormat mTimeFormatter;

    private volatile Consumer<List<String>> consumer;

    public ScaleDataRetriever() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        mRowSize = 12;
        mColumnSize = 3;
        mCurrentId = Long.MAX_VALUE / 2; //防止溢出;
        mLastRecord = false;
        mHeadList = new ArrayList<>();

        mDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormatter = new SimpleDateFormat("HH:mm");

        buildHead();
    }

    private void buildHead() {
        mHeadList.add(MainApplication.getInstance().getString(R.string.date));
        mHeadList.add(MainApplication.getInstance().getString(R.string.time));
        mHeadList.add(MainApplication.getInstance().getString(R.string.weight));
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
        return 2;
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

    @Override
    public List<String> getHeadList() {
        return mHeadList;
    }

    public void setConsumer(Consumer<List<String>> consumer) {
        this.consumer = consumer;
    }

    /*
    * 从数据库中读取血氧数据
    * */
    @Override
    public void accept(final Object o) throws Exception {
        if (consumer == null)
            return;

        DaoSession daoSession = mDaoControl.getDaoSession();
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        final List<WeightModel> weightModelList = weightModelDao.queryBuilder()
                .orderDesc(WeightModelDao.Properties.Id)
                .where(WeightModelDao.Properties.Id.le(mCurrentId))
                .limit(mRowSize).list();

        if (weightModelList.size() <= 0) {
            /*没有数据*/
            mCurrentId += mRowSize;
            return;
        }

        mLastRecord = false;
        mRowId = 1;

        Observable.create((ObservableOnSubscribe<List<WeightModel>>) e -> {
            e.onNext(weightModelList);
            e.onComplete();
        }).flatMap(weightModelList1 -> Observable.fromIterable(weightModelList1)).map(weightModel -> {
            List<String> record = new ArrayList<>();
            record.add(String.valueOf(mRowId));
            if (mRowId == 1) {
                mCurrentId = weightModel.getId();
            }
            mRowId++;
            record.add(mDateFormatter.format(weightModel.getTime()));
            record.add(mTimeFormatter.format(weightModel.getTime()));
            record.add(String.valueOf(weightModel.getSC()));
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
