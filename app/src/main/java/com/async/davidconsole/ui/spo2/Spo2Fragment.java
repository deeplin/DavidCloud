package com.async.davidconsole.ui.spo2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.controllers.MessageSender;
import com.async.davidconsole.ui.main.IFragmentLockable;
import com.async.davidconsole.ui.sensorlist.SensorListLayout;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/2 20:19
 * email: 10525677@qq.com
 * description:
 */
public class Spo2Fragment extends Fragment implements IFragmentLockable {

    @Inject
    SensorListLayout sensorListLayout;
    @Inject
    MessageSender messageSender;

    FrameLayout leftFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_spo2, container, false);

        leftFrame = view.findViewById(R.id.spo2LeftFrame);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        messageSender.getSpo2Config();
        leftFrame.addView(sensorListLayout);
    }

    @Override
    public void onPause() {
        super.onPause();
        leftFrame.removeAllViews();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
