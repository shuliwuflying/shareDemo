package com.android.libcamera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.SurfaceHolder;

import java.util.List;


public class CameraHelper {

    private static final String TAG = "CameraHelper";
    private static Camera camera;
    private static Handler sHandler;

    public static void init() {
        HandlerThread handlerThread = new HandlerThread("cameraV1");
        handlerThread.start();
        sHandler = new Handler(handlerThread.getLooper());
    }

    public static void openCamera(SurfaceHolder holder) {
        android.util.Log.e(TAG,"openCamera");
        int numberOfCameras = Camera.getNumberOfCameras();
        int cameraId = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        android.util.Log.e(TAG,"cameraId: "+cameraId);
        try {
            camera=Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        android.util.Log.e(TAG,"camera111111: "+camera);
    }


    public static void startPreview(final SurfaceHolder holder,final int width,final int height) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                openCamera(holder);
                android.util.Log.e(TAG,"startPreview: "+holder.getSurface());
                if (holder.getSurface() == null) {
                    return;
                }
                camera.stopPreview();

                Camera.Parameters parameters= camera.getParameters();
                parameters.setPictureFormat(ImageFormat.JPEG);//设置图片的格式

                List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
                List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
                getOptimalVideoSize(mSupportedVideoSizes,
                        mSupportedPreviewSizes, height, width);
                Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                        mSupportedPreviewSizes, height, width);
                parameters.setPreviewSize(optimalSize.width, optimalSize.height); // 设置预览图像大小

                parameters.set("orientation", "portrait");
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                int buffSize = optimalSize.width * optimalSize.height *3/2;
                try {
                    //一定要将属性值返回去，否则设置无效
                    camera.setDisplayOrientation(90);
                    camera.setPreviewDisplay(holder);
                    camera.setParameters(parameters);// 设置相机参数
                    byte[][] buffer = new byte[3][buffSize];
                    for (int i=0;i<3;i++) {
                        camera.addCallbackBuffer(buffer[i]);
                    }
                    camera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] bytes, Camera camera) {
                            android.util.Log.e(TAG,"onPreviewFrame");
                            camera.addCallbackBuffer(bytes);
                        }
                    });
                    camera.startPreview();
                    camera.startFaceDetection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public static Camera.Size getOptimalVideoSize(List<Camera.Size> supportedVideoSizes,
                                                  List<Camera.Size> previewSizes, int w, int h) {
        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;

        // Supported video sizes list might be null, it means that we are allowed to use the preview
        // sizes
        List<Camera.Size> videoSizes;
        if (supportedVideoSizes != null) {
            videoSizes = supportedVideoSizes;
        } else {
            videoSizes = previewSizes;
        }
        Camera.Size optimalSize = null;

        // Start with max value and refine as we iterate over available video sizes. This is the
        // minimum difference between view and camera height.
        double minDiff = Double.MAX_VALUE;

        // Target view height
        int targetHeight = h;

        // Try to find a video size that matches aspect ratio and the target view size.
        // Iterate over all available sizes and pick the largest size that can fit in the view and
        // still maintain the aspect ratio.
        for (Camera.Size size : videoSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find video size that matches the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : videoSizes) {
                if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


}
