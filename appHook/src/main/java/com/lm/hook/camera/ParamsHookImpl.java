package com.lm.hook.camera;

import android.hardware.Camera;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;

public class ParamsHookImpl extends BaseHookImpl {
    private static final String TAG = "ParamsHookImpl";
    private static final List<String> filterParamList = new ArrayList<>();
    private static final List<String> keepParamList = new ArrayList<>();
    private static final Map<String, String> lastValueMap = new HashMap<>();
    public static int previewWidth = 0;
    public static int previewHeight =0;


    public ParamsHookImpl() {
        initFilterList();
        initKeepList();
        hookEntityList.add(getDefaultBufferSizeMethod());
        hookEntityList.add(getPreviewSize());
        hookEntityList.add(setParametersHook());
        hookEntityList.add(parametersPutHook());
        hookEntityList.add(setFlashMode());
        hookEntityList.add(setPictureSizeForCamera2());
        hookEntityList.add(captureRequestHook());
    }

    private void initFilterList() {
        filterParamList.add("preview-size");
        filterParamList.add("antibanding");
        filterParamList.add("picture-size");
        filterParamList.add("rotation");
    }

    private void initKeepList() {
        keepParamList.add("aeMode");
        keepParamList.add("afMode");
        keepParamList.add("aeExposure");
        keepParamList.add("flash.mode");
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
                                LogUtils.e(TAG, "setDefaultBufferSize");
                                previewWidth = Integer.parseInt(param.args[0].toString());
                                previewHeight = Integer.parseInt(param.args[1].toString());
                                CameraAnalysis.printPreviewSize(String.format("preview-size:%d,%d",previewWidth, previewHeight));
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
                                previewWidth = Integer.parseInt(param.args[1].toString());
                                previewHeight = Integer.parseInt(param.args[0].toString());
                                LogUtils.e(TAG, "getPreviewSize previewWidth: "+previewWidth +"  previewHeight: "+previewHeight);
                                CameraAnalysis.printPreviewSize(String.format("preview-size:%d,%d",previewWidth, previewHeight));
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
                                for (int i = 0; i < param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length - 1) {
                                        sb.append(": ");
                                    }
                                }
                                if (filterParamList.contains(param.args[0])) {
                                    CameraAnalysis.print(sb.toString());
                                } else {
                                    CameraAnalysis.printPreviewSize(sb.toString());
                                }

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
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                for (int i = 0; i < 2; i++) {
                                    int value = (Integer) param.args[i];
                                    if (value < previewHeight) {
                                        return;
                                    }
                                }
                                LogUtils.e(TAG, "ImageReader width: "+param.args[1]+"   height"+param.args[0]);
                                LogUtils.e(TAG, "ImageReader: "+ param.getResult());
                                LogUtils.recordLog(TAG, String.format("picture-size: %s,%s",param.args[1],param.args[0]));
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
                                for (int i = 0; i < param.args.length; i++) {
                                    sb.append(param.args[i]);
                                    if (i != param.args.length - 1) {
                                        sb.append(",");
                                    }
                                }
                                LogUtils.recordLog(TAG, "flash-mode: " + sb.toString());
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
                                boolean isKeepValue = false;
                                String key = null;
                                for(String value: keepParamList) {
                                    if (param.args[0].toString().contains(value)) {
                                        sb.append(value);
                                        key = value;
                                        isKeepValue = true;
                                        break;
                                    }
                                }
                                if (!isKeepValue) {
                                    sb.append(param.args[0]);
                                }
                                sb.append(":");
                                sb.append(param.args[1]);

                                if (!isKeepValue) {
                                    CameraAnalysis.print(sb.toString());
                                } else {
                                    if (!param.args[1].toString().equals(lastValueMap.get(key))) {
                                        lastValueMap.put(key, param.args[1].toString());
                                        CameraAnalysis.printPreviewSize(sb.toString());
                                    }
                                }
                            }
                        }
                });
    }


}
