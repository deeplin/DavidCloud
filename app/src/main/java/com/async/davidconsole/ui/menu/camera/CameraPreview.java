package com.async.davidconsole.ui.menu.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.async.common.utils.LogUtil;

import java.io.IOException;
import java.util.List;

/**
 * author: Ling Lin
 * created on: 2018/1/2 9:37
 * email: 10525677@qq.com
 * description:
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CameraPreview(Context context) {
        super(context);
    }

    public void startCamera() throws RuntimeException {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
        mCamera = Camera.open(CAMERA_ID);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            LogUtil.e(this, e);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCamera.stopPreview();

            Camera.Parameters cameraParams = mCamera.getParameters();

            //Setup resolution
            List<Camera.Size> mPictureSizeList = cameraParams.getSupportedPictureSizes();
            int resolutionId = mPictureSizeList.size() - 1;
            if (mPictureSizeList.size() > 2) {
                resolutionId = 1;
            }
            Camera.Size pictureSize = mPictureSizeList.get(resolutionId);
            LogUtil.i(this, "Picture Actual Size - w: " + pictureSize.width + ", h: " + pictureSize.height);

            cameraParams.set("orientation", "landscape");
            cameraParams.setPictureSize(pictureSize.width, pictureSize.height);
            cameraParams.setPictureFormat(PixelFormat.JPEG);
            cameraParams.set("jpeg-quality", 80);
            cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.autoFocus(null); // 设置自动对焦mCamera.setDisplayOrientation(0);
            mCamera.setParameters(cameraParams);

            mCamera.startPreview();
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            stopCamera();
        } catch (Exception e) {
            LogUtil.e(this, e);
        }
    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
