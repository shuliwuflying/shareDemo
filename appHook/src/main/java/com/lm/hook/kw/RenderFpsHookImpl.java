package com.lm.hook.kw;


import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**TAG
 *美颜渲染帧率
 */
class RenderFpsHookImpl extends BaseHookImpl {
    private static final String TAG = "RenderFpsHookImpl";
    private static final String FPS_START = "Render Thread Stats:";
    private static final String FPS_END = "Drop";
    private static final int COUNT_SIZE = 5;
    private static int count = 0;
    private static boolean sIsFirstFrame = false;
    private static int sDrawFrameCount = 0;
    private static long sRecordLastFpsTs = 0L;
    private static float sFpsCount = 0;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        try {
//            hookEntityList.add(getNativeDrawFrameHook("com.kwai.camerasdk.render.NativeRenderThread"));
//            hookEntityList.add(getDrawLastFrameHook("com.kwai.camerasdk.render.NativeRenderThread"));
//
//            hookEntityList.add(getNativeDrawFrameHook("com.kwai.camerasdk.render.view2.NativeRenderThread2"));
//            hookEntityList.add(getDrawLastFrameHook("com.kwai.camerasdk.render.view2.NativeRenderThread2"));
//
//            hookEntityList.add(getGlNativeDrawFrameHook("com.kwai.camerasdk.render.GlDrawer"));
//
//            hookEntityList.add(getProcessReceiveFrameHook("nativeProcessPublishFrame"));
//            hookEntityList.add(getProcessReceiveFrameHook("nativeProcessReceiveFrame"));
//
//            hookEntityList.add(setRenderThreadListenerHook());
//            hookEntityList.add(onFrameAvailableHook());
//
//            hookEntityList.add(onFirstFrameHook());
//            hookEntityList.add(onFirstFrameHook1());
            hookEntityList.add(setStatsHolderHook());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MethodSignature getNativeDrawFrameHook(String className) throws ClassNotFoundException {
        final String targetClz = className;
        final String methodName = "nativeDrawFrame";
        final Class<?> paramClz = hookParam.classLoader.loadClass("com.kwai.camerasdk.video.VideoFrame");
        final Object[] params = new Object[] {
                long.class,
                paramClz,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "nativeDrawFrame111 after");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "nativeDrawFrame111 after");

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getGlNativeDrawFrameHook(String className) throws ClassNotFoundException {
        final String targetClz = className;
        final String methodName = "nativeDrawFrame";
        final Class<?> paramClz = hookParam.classLoader.loadClass("com.kwai.camerasdk.video.VideoFrame");
        final Object[] params = new Object[] {
                long.class,
                paramClz,
                int.class,
                int.class,
                int.class,
                int.class,
                boolean.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "com.kwai.camerasdk.render.GlDrawer after");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getDrawLastFrameHook(String className) {
        final String targetClz = className;
        final String methodName = "drawLastFrame";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "getDrawLastFrameHook111 after");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }


    private static  MethodSignature getProcessReceiveFrameHook(final String method) {
        final String targeClz = "com.kwai.camerasdk.monitor.FrameMonitor";
        final Object[] params = new Object[] {
                long.class,
                int.class,
                long.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, method+" after");
                    }
                }
        };
        return new MethodSignature(targeClz, method, params);
    }


    private MethodSignature setRenderThreadListenerHook() throws ClassNotFoundException {
        final String targetClz = "com.kwai.camerasdk.render.NativeRenderThread";
        final String method = "setRenderThreadListener";
        final Class<?> paramsClz = hookParam.classLoader.loadClass("com.kwai.camerasdk.render.RenderThreadListener");
        final Object[] params = new Object[] {
            paramsClz,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.e(TAG, method+" hook after");
                }
            }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature onFrameAvailableHook() throws ClassNotFoundException {
        final String targetClz = "com.kwai.camerasdk.render.NativeRenderThread";
        final String method = "onFrameAvailable";
        final Class<?> paramClz = hookParam.classLoader.loadClass("com.kwai.camerasdk.video.VideoFrame");
        final Object[] params = new Object[] {
                paramClz,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, method+" hook after");
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature onFirstFrameHook() {
        final String targetClz = "com.kwai.m2u.main.controller.components.CBottomButtonController";
        final String method = "onFistFrameRenderSuccess";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, method+" hook after");
                        android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("onFistFrameRenderSuccess")));
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature onFirstFrameHook1() {
        final String targetClz = "com.kwai.contorller.a.c";
        final String method = "a";
        final Object[] params = new Object[] {
                Runnable.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, method+" hook after");
                        android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("onFistFrameRenderSuccess")));
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }



    private MethodSignature setStatsHolderHook() {
        final String targetClz = "com.kwai.camerasdk.stats.StatsHolder$1";
        final String methodName = "onDebugInfo";
        final Object[] params = new Object[] {
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String value = param.args[0].toString();
                        int startIndex = value.indexOf(FPS_START);
                        int endIndex = value.indexOf(FPS_END,startIndex);
                        if (startIndex > 0 && endIndex >0) {
                           String fpsValue = value.substring(startIndex+FPS_START.length()+7, endIndex).trim();
                           count ++;
                           sFpsCount += Float.valueOf(fpsValue);
                           if (count >= COUNT_SIZE) {
                               CameraAnalysis.printRenderFps(String.valueOf(sFpsCount/count));
                               sFpsCount = 0;
                               count = 0;
                           }
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

}
