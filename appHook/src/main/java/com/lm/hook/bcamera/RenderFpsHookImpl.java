package com.lm.hook.bcamera;


import android.graphics.SurfaceTexture;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.base.LaunchHookBaseImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * B612 帧率(preview, render)hook
 */
class RenderFpsHookImpl extends BaseHookImpl {
    private static final String TAG = "RenderFpsHookImpl";
    private static boolean sIsFirstFrame = false;
    private static long sDrawFrameCount = 0;
    private static long sDrawFrameCount2 = 0;
    private static long sRecordLastFpsTs = 0L;

    private static long sRecordLastFpsTs2 = 0L;
    private String lastValue = "";

    private LaunchHookBaseImpl mLaunchHookBase;
    private HashMap<String, Integer> stackMap = new HashMap<>();

    public RenderFpsHookImpl(LaunchHookBaseImpl launchHookBase) {
        mLaunchHookBase = launchHookBase;
    }

    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(getUpdateImaTxt());
//        hookEntityList.add(getTransformMatrixHook());
    }

//    /**
//     * 渲染帧率
//     *
//     * @return
//     */
//    private MethodSignature getRendFps() {
//        final String targetClz = "oh2";
//        final String method = "Q2";
//
//        return new MethodSignature(
//                targetClz,
//                method,
//                new Object[]{
//                        GL10.class,
//                        new XC_MethodHook() {
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                if (!sIsFirstFrame) {
//                                    sIsFirstFrame = true;
//                                    mLaunchHookBase.setFirstReceiveFrame();
//                                    Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("onDrawFrame")));
//                                    sRecordLastFpsTs2 = System.currentTimeMillis();
//                                }
//                                sDrawFrameCount2++;
//                                long timeDuration = System.currentTimeMillis() - sRecordLastFpsTs2;
//                                if (Math.abs(timeDuration - 1000) < 15 || timeDuration >= 1000) {
//                                    LogUtils.recordLog(TAG, "onDrawFrame222 fsp: " + sDrawFrameCount2);
//                                    sRecordLastFpsTs2 = System.currentTimeMillis();
//                                    sDrawFrameCount2 = 0;
//                                }
//                            }
//                        }
//                });
//    }

    private MethodSignature getUpdateImaTxt() {
        final String targetClz = SurfaceTexture.class.getName();
        final String methodName = "updateTexImage";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        LogUtils.e(TAG, "updateTexImage  thread: "+Thread.currentThread().getName());
                        printRenderFps();
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getTransformMatrixHook() {
        final String targetClz = SurfaceTexture.class.getName();
        final String methodName = "getTransformMatrix";
        final Object[] params = new Object[] {
                float[].class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        printRenderFps2();
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }



    private void printRenderFps() {
        if (!sIsFirstFrame) {
            sIsFirstFrame = true;
            sRecordLastFpsTs = System.currentTimeMillis();
        }
        sDrawFrameCount++;
        long timeDuration = System.currentTimeMillis() - sRecordLastFpsTs;
        if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 5 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
            CameraAnalysis.printRenderFps(String.valueOf(sDrawFrameCount));
            sRecordLastFpsTs = System.currentTimeMillis();
            sDrawFrameCount = 0;
        }
    }

    private void printRenderFps2() {
        if (!sIsFirstFrame) {
            sIsFirstFrame = true;
            sRecordLastFpsTs = System.currentTimeMillis();
        }
        sDrawFrameCount2++;
        long timeDuration = System.currentTimeMillis() - sRecordLastFpsTs2;
        LogUtils.i(TAG, "getTransformMatrix  thread: "+Thread.currentThread().getId());
        if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 5 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
            LogUtils.e(TAG, "getTransformMatrix  thread: "+Thread.currentThread().getId());
            CameraAnalysis.printRenderFps(String.valueOf(sDrawFrameCount2));
            sRecordLastFpsTs2 = System.currentTimeMillis();
            sDrawFrameCount2 = 0;
        }
    }


}
