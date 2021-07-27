package com.lm.hook.camera;

import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.HookUtils;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

public class CameraHookImpl extends BaseHookImpl {
    private static final String TAG = "OpenCameraHookImpl";

    public CameraHookImpl() {
        hookEntityList.add(getOpenCameraV1Method());
        hookEntityList.add(getOpenCameraV2Method());
        hookEntityList.add(getHdCaptureForCameraV1());
        hookEntityList.add(getHdCaptureForCameraV2());
    }


    private MethodSignature getOpenCameraV1Method() {
        return new MethodSignature(Camera.class.getName(), "open",
                new Object[]{
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (param.args[0] != null) {
                                    Integer value = Integer.parseInt(String.valueOf(param.args[0]));
                                    CameraAnalysis.printCameraFace("camera level: V1");
                                    CameraAnalysis.printCameraFace("facing: " + (value == 1 ? "front" : "back"));
                                    HookUtils.initHandler();
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MethodSignature getOpenCameraV2Method() {
        return new MethodSignature(CameraManager.class.getName(), "openCamera",
                new Object[]{
                        String.class,
                        CameraDevice.StateCallback.class,
                        Handler.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (param.args[0] != null) {
                                    Integer value = Integer.parseInt(String.valueOf(param.args[0]));
                                    CameraAnalysis.printCameraFace("camera level: V2");
                                    CameraAnalysis.printCameraFace("facing: " + (value == 1 ? "front" : "back"));
                                    HookUtils.initHandler();
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private MethodSignature getHdCaptureForCameraV1() {
        return new MethodSignature(Camera.class.getName(), "takePicture",
                new Object[]{
                        Camera.ShutterCallback.class,
                        Camera.PictureCallback.class,
                        Camera.PictureCallback.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                CameraAnalysis.isHdCapture = true;
                                LogUtils.recordLog(TAG, "Hd capture true");
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
                                if (param.args[0] != null && param.args[0] instanceof Integer) {
                                    Integer value = Integer.parseInt(String.valueOf(param.args[0]));
                                    if (value == CameraDevice.TEMPLATE_STILL_CAPTURE) {
                                        CameraAnalysis.isHdCapture = true;
                                        LogUtils.recordLog(TAG, "Hd capture true");
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


}
