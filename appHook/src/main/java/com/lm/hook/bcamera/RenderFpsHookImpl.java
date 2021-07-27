package com.lm.hook.bcamera;

import android.hardware.Camera;
import android.util.Log;

import com.lm.hook.base.LaunchHookImpl;
import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import javax.microedition.khronos.opengles.GL10;

import de.robv.android.xposed.XC_MethodHook;


/**
 * B612 帧率(preview, render)hook
 */
class RenderFpsHookImpl extends BaseHookImpl {
    private static final String TAG = "RenderFpsHookImpl";
    private static boolean sIsFirstFrame = false;
    private static long sDrawFrameCount = 0;
    private static long sRecordLastFpsTs = 0L;

    public RenderFpsHookImpl() {
        hookEntityList.add(getRendFps());
        hookEntityList.add(getPreviewFpsMethod());
    }

    /**
     * 渲染帧率
     * @return
     */
    private MethodSignature getRendFps() {
        final String targetClz = "oh2";
        final String method = "Q2";

        return new MethodSignature(
                targetClz,
                method,
                new Object[]{
                        GL10.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if (!sIsFirstFrame) {
                                    sIsFirstFrame = true;
                                    LaunchHookImpl.recordFirstDrawFrame();
                                    Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("onDrawFrame")));
                                    sRecordLastFpsTs = System.currentTimeMillis();
                                }
                                sDrawFrameCount++;
                                long timeDuration = System.currentTimeMillis() - sRecordLastFpsTs;
                                if (Math.abs(timeDuration - 1000) < 15 || timeDuration >= 1000) {
                                    LogUtils.e(TAG, "onDrawFrame fsp: " + sDrawFrameCount);
                                    sRecordLastFpsTs = System.currentTimeMillis();
                                    sDrawFrameCount = 0;
                                }
                            }
                        }
                });
    }

    /**
     * 预览帧率
     * @return
     */
    private MethodSignature getPreviewFpsMethod() {
        return new MethodSignature("n83$h", "onPreviewFrame",
                new Object[]{
                        byte[].class,
                        Camera.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "n83$h onPreviewFrame");

                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

}
