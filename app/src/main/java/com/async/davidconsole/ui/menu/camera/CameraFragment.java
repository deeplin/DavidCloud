package com.async.davidconsole.ui.menu.camera;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.async.common.utils.LogUtil;
import com.async.davidconsole.R;
import com.async.davidconsole.controllers.MainApplication;
import com.async.davidconsole.ui.main.IFragmentLockable;
import com.async.davidconsole.ui.sensorlist.SensorListLayout;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/2 9:37
 * email: 10525677@qq.com
 * description:
 */
public class CameraFragment extends Fragment implements IFragmentLockable {

    @Inject
    SensorListLayout sensorListLayout;

    CameraPreview cameraPreview = null;
    FrameLayout frCameraLeft;
    FrameLayout frCameraRight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        frCameraLeft = view.findViewById(R.id.frCameraLeft);
        frCameraRight = view.findViewById(R.id.frCameraRight);
        TextView noCamera = view.findViewById(R.id.tvCameraError);
        try {
            if (cameraPreview == null) {
                cameraPreview = new CameraPreview(view.getContext());
                cameraPreview.startCamera();
                frCameraRight.addView(cameraPreview);
            } else {
                cameraPreview.startCamera();
            }
            noCamera.setVisibility(View.GONE);
        } catch (Exception e) {
            LogUtil.i(this, "Open camera error.");
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        frCameraLeft.addView(sensorListLayout);
    }

    @Override
    public void onPause() {
        super.onPause();
        frCameraLeft.removeAllViews();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraPreview != null) {
            cameraPreview.stopCamera();
            frCameraRight.removeAllViews();
        }
    }
}