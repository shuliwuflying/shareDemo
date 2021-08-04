package com.lm.hook.camera;

import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.HookUtils;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

/**
 * camera 各阶段hook
 */
public class CameraStageHookImpl extends BaseHookImpl {
    private static final String TAG = "OpenCameraHookImpl";
    private static long sOpenStart = 0;
    private static long sPreviewStart = 0;
    private static long sStopPreviewStart = 0;
    private static long sCloseCameraStart = 0;
    private static CameraStateCallback sCameraStateCallback;

    public CameraStageHookImpl() {
        hookEntityList.add(openCameraV1Hook());
        hookEntityList.add(openCameraV2Hook());
        hookEntityList.add(closeCameraV1Hook());
        hookEntityList.add(closeCameraV2Hook());
        hookEntityList.add(startPreviewV1Hook());
        hookEntityList.add(stopPreviewV1Hook());
    }


    private MethodSignature openCameraV1Hook() {
        return new MethodSignature(Camera.class.getName(), "open",
                new Object[]{
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (param.args[0] != null) {
                                    CameraAnalysis.printCameraFace("camera_level: V1");
                                    onOpenCameraBefore(param);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onOpenCameraFinish();
                            }
                        }
                });
    }

    private MethodSignature closeCameraV1Hook() {
        return new MethodSignature(Camera.class.getName(), "release",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraFinish();
                            }
                        }
                });
    }


    private MethodSignature closeCameraV2Hook() {
        return new MethodSignature("android.hardware.camera2.impl.CameraDeviceImpl", "close",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraFinish();
                            }
                        }
                });
    }

    private MethodSignature startPreviewV1Hook() {
        return new MethodSignature(Camera.class.getName(), "startPreview",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onStartPreviewBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onStartPreviewFinish();
                            }
                        }
                });
    }

    private MethodSignature stopPreviewV1Hook() {
        return new MethodSignature(Camera.class.getName(), "stopPreview",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onStopPreviewBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onStopPreviewFinish();
                            }
                        }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MethodSignature openCameraV2Hook() {
        return new MethodSignature(CameraManager.class.getName(), "openCamera",
                new Object[]{
                        String.class,
                        CameraDevice.StateCallback.class,
                        Handler.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                CameraAnalysis.printCameraFace("camera_level: V2");
                                onOpenCameraBefore(param);

                                sCameraStateCallback = new CameraStateCallback((CameraDevice.StateCallback) param.args[1]);
                                param.args[1] = sCameraStateCallback;
                            }
                        }
                });
    }


    private class CameraStateCallback extends CameraDevice.StateCallback {
        private CameraDevice.StateCallback realCallback;

        CameraStateCallback(CameraDevice.StateCallback callback) {
            realCallback = callback;
        }

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            LogUtils.e(TAG, "camera: "+camera);
            onOpenCameraFinish();
            realCallback.onOpened(camera);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            LogUtils.e(TAG, "onDisconnected");
            realCallback.onDisconnected(camera);
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            LogUtils.e(TAG, "onError");
            realCallback.onError(camera, error);
        }
    }


    private void onOpenCameraBefore(XC_MethodHook.MethodHookParam param) {
        sOpenStart = System.currentTimeMillis();
        Integer value = Integer.parseInt(String.valueOf(param.args[0]));
        CameraAnalysis.printCameraFace("facing: " + (value == 1 ? "front" : "back"));
        HookUtils.initHandler();
    }

    private void onOpenCameraFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "open-camera-cost: " + (System.currentTimeMillis() - sOpenStart));
    }

    private void onCloseCameraBefore(XC_MethodHook.MethodHookParam param) {
        sCloseCameraStart = System.currentTimeMillis();
    }

    private void onCloseCameraFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "close-camera-cost: " + (System.currentTimeMillis() - sCloseCameraStart));
    }

    private void onStartPreviewBefore(XC_MethodHook.MethodHookParam param) {
        sPreviewStart = System.currentTimeMillis();
    }

    private void onStartPreviewFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "start-preview-cost: " + (System.currentTimeMillis() - sPreviewStart));
    }

    private void onStopPreviewBefore(XC_MethodHook.MethodHookParam param) {
        sStopPreviewStart = System.currentTimeMillis();
    }

    private void onStopPreviewFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "stop-preview-cost: " + (System.currentTimeMillis() - sStopPreviewStart));
    }

}
