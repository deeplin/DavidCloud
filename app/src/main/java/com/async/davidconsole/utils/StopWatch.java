package com.async.davidconsole.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/12/31 22:27
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class StopWatch implements Consumer {

    Time mTime;
    int mCount;
    Disposable mDisposable;
    SimpleDateFormat mSimpleDateFormat;
    Consumer<String> mConsumer;

    @Inject
    public StopWatch() {
        mTime = new Time(0);
        mCount = -1;
        mSimpleDateFormat = new SimpleDateFormat("mm:ss");
    }

    public synchronized void start() {
        if (mDisposable == null) {
            reset();
            Observable observable = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread());
            mDisposable = observable.subscribe(this);
        }
    }

    public synchronized void stop() {
        mConsumer = null;
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public void reset() {
        mTime.setTime(0);
        mCount = -1;
    }

    public synchronized void setConsumer(Consumer<String> consumer) {
        mConsumer = consumer;
    }

    @Override
    public synchronized void accept(@NonNull Object o) throws Exception {
        mCount++;
        String value = mSimpleDateFormat.format(mTime);
        if (mConsumer != null)
            mConsumer.accept(value);
        mTime.setTime(mTime.getTime() + 1000);
    }

    public int getCount() {
        return mCount;
    }
}
