package com.lm.hook.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

public class HdCaptureHookImpl extends BaseHookImpl {
    private static final String TAG = "HdCaptureHookImpl";
    private static long sCaptureStart = 0;

    public HdCaptureHookImpl() {
        hookEntityList.add(getHdCaptureForCameraV1());
        hookEntityList.add(getHdCaptureForCameraV2());
    }

    private MethodSignature getHdCaptureForCameraV1() {
        return new MethodSignature(Camera.class.getName(), "takePicture",
                new Object[]{
                        Camera.ShutterCallback.class,
                        Camera.PictureCallback.class,
                        Camera.PictureCallback.class,
                        Camera.PictureCallback.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "takePicture");
                                sCaptureStart = System.currentTimeMillis();
                                CameraAnalysis.isHdCapture = true;
                                LogUtils.recordLog(TAG, "hd-capture: true");
                                Camera.PictureCallback callback = (Camera.PictureCallback) param.args[3];
                                PictureCallback pictureCallback = new PictureCallback(callback);
                                param.args[3] = pictureCallback;
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private MethodSignature getHdCaptureForCameraV2() {
        return new MethodSignature("android.hardware.camera2.impl.CameraDeviceImpl", "createCaptureRequest",
                new Object[]{
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "createCaptureRequest");
                                if (param.args[0] != null && param.args[0] instanceof Integer) {
                                    Integer value = Integer.parseInt(String.valueOf(param.args[0]));
                                    if (value == CameraDevice.TEMPLATE_STILL_CAPTURE) {
                                        CameraAnalysis.isHdCapture = true;
                                        sCaptureStart = System.currentTimeMillis();
                                        LogUtils.recordLog(TAG, "hd-capture: true");
                                    } else if (value == CameraDevice.TEMPLATE_PREVIEW) {
                                        CameraAnalysis.isPreviewParamsSet = false;
                                    }
                                }

                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    class PictureCallback implements Camera.PictureCallback {
        private Camera.PictureCallback pictureCallback;

        public PictureCallback(Camera.PictureCallback callback) {
            this.pictureCallback = callback;
        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureCallback.onPictureTaken(data, camera);
            LogUtils.recordLog(TAG,"capture-cost: "+(System.currentTimeMillis() - sCaptureStart));
        }
    }
}
