package com.lm.hook.meiyan;


import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;


import de.robv.android.xposed.XC_MethodHook;

/**TAG
 *美颜渲染帧率
 */
class RenderFpsHookImpl extends BaseHookImpl {
    private static final String TAG = "RenderFpsHookImpl";
    private static final String RENDER_THREAD_NAME = "MTRenderEglEngine";
    private static boolean sIsFirstFrame = false;
    private static int sDrawFrameCount = 0;
    private static long sRecordLastFpsTs = 0L;
    private static boolean isLog = false;
    private static HandlerThread sRenderThread;
    private static Looper sRenderLooper = null;
    private static Handler sRenderHandler = null;
    private static int sCount1 = 0;
    private static int sCount2 = 0;
    private static int sCount3 = 0;


    public RenderFpsHookImpl() {
        hookEntityList.add(getScissorHook());
        hookEntityList.add(getUpdateImaTxt());
    }

    private MethodSignature getDispatchHook(String clazz) {
        final String methodName = "dispatchMessage";
        final Object[] params = new Object[] {
                Message.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.thisObject == sRenderHandler) {
                            sCount2++;
                            printRenderFps();
                        }
                    }
                }
        };
        return new MethodSignature(clazz, methodName, params);
    }

    private MethodSignature getGlViewPortHook() {
        final String targetClz = GLES20.class.getName();
        final String methodName = "glViewport";
        final Object[] params = new Object[] {
                int.class,
                int.class,
                int.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sCount1++;
                        if (sCount1 % 500 ==1 || sCount1 %500 ==2 || sCount1 %500 == 3 || sCount1 %500 == 4 || sCount1 %500 == 5) {
                            LogUtils.e(TAG, "glViewport11111 width: "+param.args[2]+"   height: "+param.args[3]);
                            LogUtils.e(TAG, android.util.Log.getStackTraceString(new Throwable(new Throwable("getGlViewPortHook"))));
                        }

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getUpdateImaTxt() {
        final String targetClz = SurfaceTexture.class.getName();
        final String methodName = "updateTexImage";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sCount1++;
                        if (sCount1 % 500 ==1) {
                            LogUtils.e(TAG, "updateTexImage  thread: "+Thread.currentThread());
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getCreateGLContext() {
        final String targetClz = "com.meitu.core.mbccore.MTProcessor";
        final String methodName = "createGLContext";
        final Object[] params = new Object[] {
                int.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "createGLContext  thread: "+Thread.currentThread());
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }


    private MethodSignature getScissorHook() {
        final String targetClz = GLES20.class.getName();
        final String methodName = "glScissor";
        final Object[] params = new Object[] {
                int.class,
                int.class,
                int.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        printRenderFps();
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
        if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 15 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
            CameraAnalysis.printRenderFps(String.format("%.2f",sDrawFrameCount*1.0/ConstantUtils.TIME_STAMP_COUNT));
            sRecordLastFpsTs = System.currentTimeMillis();
            sDrawFrameCount = 0;
        }
    }



    private MethodSignature getHandlerThreadHook() {
        final String targetClz = HandlerThread.class.getName();
        final Object[] params = new Object[] {
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (RENDER_THREAD_NAME.equals(param.args[0])) {
                            sRenderThread = (HandlerThread) param.thisObject;
                            Log.e(TAG, "sRenderThread: "+sRenderThread);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, "<init>", params);
    }

    //    private MethodSignature getRendFps() {
//        final String targetClz = "com.meitu.myxj.beauty_new.gl.f";
//        return new MethodSignature(targetClz, "onDrawFrame",
//                new Object[] {
//                        GL10.class,
//                        new XC_MethodHook() {
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                LogUtils.e(TAG, "onDrawFrame");
//                                if (!isLog) {
//                                    android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("onDrawFrame")));
//                                    isLog = true;
//                                }
//                            }
//
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                if (!sIsFirstFrame) {
//                                    sIsFirstFrame = true;
//                                    LaunchHookImpl.recordFirstDrawFrame();
//                                    sRecordLastFpsTs = System.currentTimeMillis();
//                                }
//                                sDrawFrameCount++;
//                                long timeDuration = System.currentTimeMillis() - sRecordLastFpsTs;
//                                if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 15 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
//                                    CameraAnalysis.printRenderFps(String.format("%.2f",sDrawFrameCount*1.0/ConstantUtils.TIME_STAMP_COUNT));
//                                    sRecordLastFpsTs = System.currentTimeMillis();
//                                    sDrawFrameCount = 0;
//                                }
//                            }
//                        }
//                });
//    }

}
