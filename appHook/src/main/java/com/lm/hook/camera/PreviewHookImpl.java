package com.lm.hook.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;

public class PreviewHookImpl extends BaseHookImpl {
    private static final String TAG = "PreviewHookImpl";
    private static final List<String> filterParamList = new ArrayList<>();
    private static final List<String> keepParamList = new ArrayList<>();
    private static final Map<String, String> lastValueMap = new HashMap<>();
    public static int previewWidth = 0;
    public static int previewHeight =0;
    private CameraStageHookImpl mCameraStageBase;
    private SurfaceTexture mPreviewSurfaceTexture;
    private volatile boolean isRecord = false;
    public static String sPictureHdSize = "";
    public static String sPictureSize = "";

    public PreviewHookImpl(CameraStageHookImpl hook) {
        mCameraStageBase = hook;
        initFilterList();
        initKeepList();
        hookForCameraV1();
        hookForCameraV2();
    }

    private void hookForCameraV1() {
        hookEntityList.add(getParametersHook());
        hookEntityList.add(setPreviewSize());
        hookEntityList.add(setPictureSize());
        if (!"V1950A".equals(Build.MODEL)) {
            hookEntityList.add(parametersPutHook());
        }
        hookEntityList.add(getSurfaceTextureHook());
    }

    private void hookForCameraV2() {
        hookEntityList.add(getDefaultBufferSizeMethod());
        hookEntityList.add(setPictureSizeForCamera2());
        if (!"V1950A".equals(Build.MODEL)) {
            hookEntityList.add(captureRequestHook());
        }
        hookEntityList.add(getParametersHook2());
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
                                LogUtils.e(TAG, "setDefaultBufferSize: "+param.thisObject);
                                mPreviewSurfaceTexture = (SurfaceTexture) param.thisObject;
                                previewWidth = Integer.parseInt(param.args[0].toString());
                                previewHeight = Integer.parseInt(param.args[1].toString());
                                CameraAnalysis.printPreviewSize(String.format("preview-size-%s: %d,%d",CameraStageHookImpl.sCameraFacing, previewWidth, previewHeight));
                            }
                        }
                });
    }

    private MethodSignature setPreviewSize() {
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
                                sPictureSize = String.format("picture-size: %d,%d",previewWidth, previewHeight);
                                CameraAnalysis.printPreviewSize(String.format("preview-size-%s: %d,%d",CameraStageHookImpl.sCameraFacing, previewWidth, previewHeight));
                            }
                        }
                });
    }

    private MethodSignature setPictureSize() {
        return new MethodSignature(Camera.Parameters.class.getName(),
                "setPictureSize",
                new Object[]{
                        int.class,
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                String height = param.args[0].toString();
                                String width = param.args[1].toString();
                                LogUtils.e(TAG, "pictureWidth: "+width +"  pictureHeight: "+height);
                                sPictureHdSize = String.format("picture-size-hd: %s,%s",width, height);
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
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "beforeHookedMethod put1111");
                                try {
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
                                    LogUtils.e(TAG, "beforeHookedMethod put2222");
                                } catch (Exception e) {
                                    LogUtils.e(TAG, "parametersPutHook e: "+e);
                                }

                            }
                        }
                });
    }

    private MethodSignature getParametersHook() {
        return new MethodSignature(Camera.class.getName(),
                "setParameters",
                new Object[]{
                        Camera.Parameters.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (!isRecord) {
                                    for (Object object : param.args) {
                                        if (object instanceof Camera.Parameters) {
                                            String value = ((Camera.Parameters) object).flatten();
                                            LogUtils.recordLog (TAG, "allParameters: "+value);
                                        }
                                    }
                                    isRecord = true;
                                }
                            }
                        }
                });
    }

    private MethodSignature getParametersHook2() {
        return new MethodSignature(CameraManager.class.getName(),
                "getCameraCharacteristics",
                new Object[]{
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) {
                                final Object retValue = param.getResult();
                                LogUtils.recordLog(TAG, "picture-format: jpeg");
                                LogUtils.e("sliver", "getParametersHook2 afterHookedMethod isRecord: "+isRecord);
                                if (retValue != null && !isRecord) {
                                   new Thread(new Runnable() {
                                       @Override
                                       public void run() {
                                           isRecord = true;
                                           CameraCharacteristics characteristics = ((CameraCharacteristics)retValue);
                                           List<CameraCharacteristics.Key<?>> list = characteristics.getKeys();
                                           StringBuilder sb = new StringBuilder();
                                           for(CameraCharacteristics.Key<?> key: list) {
                                               Object object= characteristics.get(key);
                                               sb.append(key.getName());
                                               sb.append("=");
                                               if(object.getClass().isArray()) {
                                                   sb.append("[");
                                                  int size = Array.getLength(object);
                                                  for(int i =0;i<size;i++) {
                                                      Object value = Array.get(object, i);
                                                      sb.append(value.toString());
                                                      if(i < size - 1) {
                                                          sb.append(",");
                                                      }
                                                  }
                                                  sb.append("]");
                                               } else {
                                                   sb.append(object.toString());
                                               }
                                               sb.append(";");
                                           }
                                           LogUtils.recordLog("sliver", "allParameters: "+sb.toString());
                                       }
                                   }).start();
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
                                sPictureHdSize = String.format("picture-size-hd: %s,%s",param.args[1],param.args[0]);
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
                                sb.append(": ");
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

    public MethodSignature getSurfaceTextureHook() {
        return new MethodSignature(
                "android.graphics.SurfaceTexture",
                "setOnFrameAvailableListener",
                new Object[]{
                        SurfaceTexture.OnFrameAvailableListener.class,
                        Handler.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "SurfaceTexture#OnFrameAvailableListener before: " + param.thisObject);
                                FrameMonitor monitor = new FrameMonitor();
                                monitor.startTime = 0;
                                monitor.count = 0;
                                param.args[0] = new FrameAvailableListener((SurfaceTexture.OnFrameAvailableListener) param.args[0], monitor.viewId);
                            }
                        }
                });
    }

    private class FrameAvailableListener implements SurfaceTexture.OnFrameAvailableListener {

        SurfaceTexture.OnFrameAvailableListener mListener;
        String tag = "";
        private long startTime = 0;
        private int count = 0;
        private FrameMonitor monitor;


        FrameAvailableListener(SurfaceTexture.OnFrameAvailableListener listener, String tag) {
            mListener = listener;
            this.tag = tag;
            monitor = new FrameMonitor();
        }


        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            if (mCameraStageBase!= null && mCameraStageBase.isStartPreview()) {
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                if(count == 0 && mCameraStageBase != null) {
                    LogUtils.e(TAG,"recordFirstFrameReceive thread: "+Thread.currentThread());
                    mCameraStageBase.recordFirstFrameReceive();
                }

                if (monitor.count == 0) {
                    monitor.startTime = System.currentTimeMillis();
                }
                monitor.count++;
                count++;
                long timeDuration = System.currentTimeMillis() - monitor.startTime;
                if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 5 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
                    LogUtils.e(TAG,"onFrameAvailable thread: "+Thread.currentThread());
                    CameraAnalysis.printPreviewFps(String.valueOf(monitor.count));
                    CameraAnalysis.isPreviewParamsSet = true;
                    monitor.count = 0;
                    monitor.startTime = System.currentTimeMillis();
                }
            }
            if (mListener != null) {
                mListener.onFrameAvailable(surfaceTexture);
            }
        }
    }

    private static class FrameMonitor {
        int count = 0;
        long startTime = 0;
        SurfaceTexture.OnFrameAvailableListener listener;
        String viewId = "";
    }

}
