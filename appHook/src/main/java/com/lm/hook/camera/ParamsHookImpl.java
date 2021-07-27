package com.lm.hook.camera;

import android.hardware.Camera;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

public class ParamsHookImpl extends BaseHookImpl {
    private static final String TAG = "ParamsHookImpl";


    public ParamsHookImpl() {
        hookEntityList.add(getDefaultBufferSizeMethod());
        hookEntityList.add(getPreviewSize());
        hookEntityList.add(setParametersHook());
        hookEntityList.add(parametersPutHook());
//        hookEntityList.add(setPictureSize());
        hookEntityList.add(setFlashMode());
        hookEntityList.add(setPictureSizeForCamera2());
        hookEntityList.add(captureRequestHook());
    }

    private MethodSignature getDefaultBufferSizeMethod() {
        return new MethodSignature("android.graphics.SurfaceTexture",
                "setDefaultBufferSize",
                new Object[]{
                        int.class,
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                StringBuilder sb = new StringBuilder();
                                for(int i =0;i< param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length -1) {
                                        sb.append(",");
                                    }
                                }
                                CameraAnalysis.printPreviewSize("CameraV2 setPreviewSize: "+sb.toString());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private MethodSignature getPreviewSize() {
        return new MethodSignature(Camera.Parameters.class.getName(),
                "setPreviewSize",
                new Object[]{
                        int.class,
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                StringBuilder sb = new StringBuilder();
                                for(int i =0;i< param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length -1) {
                                        sb.append(",");
                                    }
                                }
                                CameraAnalysis.printPreviewSize("CameraV1 setPreviewSize: "+sb.toString());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private MethodSignature parametersPutHook() {
        return new MethodSignature(Camera.Parameters.class.getName(),
                "put",
                new Object[]{
                        String.class,
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                StringBuilder sb = new StringBuilder();
                                for(int i =0;i< param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length -1) {
                                        sb.append(": ");
                                    }
                                }
                                CameraAnalysis.printPreviewSize(sb.toString());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private MethodSignature setParametersHook() {
        return new MethodSignature(Camera.class.getName(),
                "setParameters",
                new Object[]{
                        Camera.Parameters.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    if (object instanceof Camera.Parameters) {
                                        //CameraAnalysis.print("setParameters params: "+((Camera.Parameters) object).flatten());
                                    }
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private MethodSignature setPictureSizeForCamera2() {
        final String targetClass = ImageReader.class.getName();
        final String method = "newInstance";
        return new MethodSignature(targetClass,
                method,
                new Object[]{
                        int.class,
                        int.class,
                        int.class,
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                StringBuilder sb = new StringBuilder();
                                for(int i =0;i< 2; i++) {
                                    int value = (Integer)param.args[i];
                                    if (value < 500) {
                                        return;
                                    }
                                    sb.append(param.args[i]);
                                    if (i != param.args.length -1) {
                                        sb.append(",");
                                    }
                                }
                                CameraAnalysis.print("setPictureSize: "+sb.toString());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });

    }

    private MethodSignature setFlashMode() {
        final String targetClass = Camera.Parameters.class.getName();
        return new MethodSignature(targetClass,
                "setFlashMode",
                new Object[]{
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                StringBuilder sb = new StringBuilder();
                                for(int i =0;i< param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length -1) {
                                        sb.append(",");
                                    }
                                }
                                CameraAnalysis.print("setFlashMode: "+sb.toString());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }


    private MethodSignature captureRequestHook() {
        final String targetClass = CaptureRequest.Builder.class.getName();
        final String method = "set";
        return new MethodSignature(targetClass,
                method,
                new Object[]{
                        CaptureRequest.Key.class,
                        Object.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                StringBuilder sb = new StringBuilder();
                                for(int i =0;i< param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length -1) {
                                        sb.append(",");
                                    }
                                }
                                CameraAnalysis.printCameraCharacters("CameraV2 set: "+sb.toString());
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }


}
