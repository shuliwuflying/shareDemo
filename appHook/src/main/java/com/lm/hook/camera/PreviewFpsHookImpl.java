package com.lm.hook.camera;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class PreviewFpsHookImpl extends BaseHookImpl {
    private static final String TAG = "FpsHookImpl";
    private static List<FrameMonitor> monitorList = new ArrayList<>();
    private static List<TextureView> textureViewList = new ArrayList<>();
    private static boolean sIsFirstFrame = false;
    private static long sDrawFrameCount = 0;
    private static long sRecordLastFpsTs = 0L;

    public PreviewFpsHookImpl() {
        hookEntityList.add(new MethodSignature(
                "android.view.TextureView",
                "onAttachedToWindow",
                new Object[]{new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object object = param.thisObject;
                        LogUtils.e(TAG, "onAttachedToWindow: " + object);

                        if (object instanceof TextureView) {
                            TextureView view = (TextureView) object;
                            textureViewList.add(view);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
                }));

        hookEntityList.add(new MethodSignature(
                "android.graphics.SurfaceTexture",
                "setOnFrameAvailableListener",
                new Object[]{
                        SurfaceTexture.OnFrameAvailableListener.class,
                        Handler.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                FrameMonitor monitor = new FrameMonitor();
                                monitor.startTime = 0;
                                monitor.count = 0;
                                monitorList.add(monitor);
                                TextureView view = getTextureView((SurfaceTexture) param.thisObject);
                                if (view != null) {
                                    monitor.viewId = getViewTag(view);
                                }
                                param.args[0] = new FrameAvailableListener((SurfaceTexture.OnFrameAvailableListener) param.args[0], monitor.viewId);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "SurfaceTexture#OnFrameAvailableListener after");
                            }
                        }
                }));

    }

    private static class FrameAvailableListener implements SurfaceTexture.OnFrameAvailableListener {

        SurfaceTexture.OnFrameAvailableListener mListener;
        String tag = "";
        private long startTime = 0;
        private int count = 0;


        FrameAvailableListener(SurfaceTexture.OnFrameAvailableListener listener, String tag) {
            mListener = listener;
            this.tag = tag;
        }


        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            FrameMonitor monitor = getFrameMonitor(tag);
            if (monitor == null) {
                LogUtils.e(TAG, "monitor == null");
                mListener.onFrameAvailable(surfaceTexture);
                return;
            }
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            if(count == 0) {
                CameraStageHookImpl.recordFirstFrameReceive();
            }

            if (monitor.count == 0) {
                monitor.startTime = System.currentTimeMillis();
            }
            monitor.count++;
            count++;
            long timeDuration = System.currentTimeMillis() - monitor.startTime;
            if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 15 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
                LogUtils.e(TAG,"onFrameAvailable thread: "+Thread.currentThread());
                CameraAnalysis.printPreviewFps(String.format("%.2f",monitor.count/5.0));
                CameraAnalysis.isPreviewParamsSet = true;
                monitor.count = 0;
                monitor.startTime = System.currentTimeMillis();
            }
            mListener.onFrameAvailable(surfaceTexture);
        }
    }

    private static FrameMonitor getFrameMonitor(String tag) {
        for (FrameMonitor monitor : monitorList) {
            if (tag.equals(monitor.viewId)) {
                return monitor;
            }
        }
        return null;
    }

    private static TextureView getTextureView(SurfaceTexture texture) {
        for (TextureView textureView : textureViewList) {
            if (textureView.getSurfaceTexture() == texture) {
                return textureView;
            }
        }
        return null;
    }


    private static class FrameMonitor {
        int count = 0;
        long startTime = 0;
        SurfaceTexture.OnFrameAvailableListener listener;
        String viewId = "";
    }

    /**
     * 获取View 的Tag 值
     *
     * @param view
     * @return example: com.bilibili.videoeditor.sdk.BLiveWindow{e4b9d2b V.ED..... ........ 0,122-1080,729 #7f0901fe app:id/live_window_preview}
     */
    private static String getViewTag(View view) {

        String value = view.toString();
        int indexSplit1 = value.indexOf("{");
        int indexSplit2 = value.indexOf("}");
        if (indexSplit1 <= 0 || indexSplit2 <= 0) {
            return Long.toHexString(view.getId());
        }
        StringBuilder sb = new StringBuilder();
        String prefixString = value.substring(0, indexSplit1);
        String targetString = value.substring(indexSplit1 + 1, indexSplit2);
        String className = prefixString.substring((prefixString.lastIndexOf(".")) + 1);
        LogUtils.e(TAG, "className: " + className);
        sb.append(className);
        String matchTargetString = ".. ";
        int rectFirstIndex = targetString.lastIndexOf(matchTargetString);
        int rectLastIndex = targetString.indexOf("#");
        if (rectFirstIndex != -1 && rectLastIndex != -1) {
            String rect = targetString.substring(rectFirstIndex + matchTargetString.length(), rectLastIndex);
            LogUtils.e(TAG, "rect: " + rect);
            if (!TextUtils.isEmpty(rect)) {
                sb.append(" - ");
                sb.append(rect);
            }
        }

        int idFirstIndex = targetString.lastIndexOf("/");
        String idValue = targetString.substring(idFirstIndex + 1);
        LogUtils.e(TAG, "idValue: " + idValue);
        if (!TextUtils.isEmpty(idValue)) {
            sb.append("- ");
            sb.append(idValue);
        }
        return sb.toString();
    }


}
