package com.android.libcamera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = "CameraSurfaceView";

    public CameraSurfaceView(Context context) {
        this(context,null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        android.util.Log.e(TAG,"surfaceCreated");
        CameraHelper.init();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        android.util.Log.e(TAG,"surfaceChanged: i: "+i+"  i1: "+i1+"  i2: "+i2);
        CameraHelper.startPreview(surfaceHolder,i1,i2);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        CameraHelper.releaseCamera();
    }
}
