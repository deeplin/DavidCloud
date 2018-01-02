package com.async.davidconsole.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/12/31 22:26
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class TimingData {

    public static final String APGAR = "APGAR";
    public static final String CPR = "CPR";

    private String mTitleString;
    private String mTextString;

    private boolean mApgarStarted;
    private boolean mCprStarted;

    private StopWatch mStopWatch;

    @Inject
    TimingData() {
        mStopWatch = new StopWatch();
        stop();
    }

    public void startApgar() {
        mTitleString = APGAR;
        mCprStarted = false;
        mApgarStarted = true;
        mStopWatch.start();
    }

    public void startCpr() {
        mTitleString = CPR;
        mApgarStarted = false;
        mCprStarted = true;
        mStopWatch.start();
    }

    public void setConsumer(Consumer<String> consumer) {
        mStopWatch.setConsumer(consumer);
    }

    public void stop() {
        mStopWatch.stop();
        mApgarStarted = false;
        mCprStarted = false;
        mTitleString = "";
        mTextString = "--:--";
    }

    public void reset() {
        mStopWatch.reset();
    }

    public boolean isApgarStarted() {
        return mApgarStarted;
    }

    public boolean isCprStarted() {
        return mCprStarted;
    }

    String getTitleString() {
        return mTitleString;
    }

    String getTextString() {
        return mTextString;
    }

    void addStep(String textString) {
        mTextString = textString;
    }

    public int getCount() {
        return mStopWatch.getCount();
    }
}
