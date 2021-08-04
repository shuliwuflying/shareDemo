package com.lm.hook.kw;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class NormalCaptureHookImpl extends BaseHookImpl {
    private final static String TAG = "NormalCaptureHookImpl";
    private final static String TARGET_CLZ = "com.kwai.camerasdk.mediarecorder.MediaRecorderImpl";
    private static long sCaptureStartTs = 0;
    private static long sHookEndTs = 0;

    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        try {
            hookEntityList.add(getNormalPictureHook());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * protected boolean capturePreview(CapturePreviewListener paramCapturePreviewListener,
     * int paramInt1, int paramInt2, DisplayLayout paramDisplayLayout, CaptureImageMode paramCaptureImageMode,
     * final boolean paramBoolean)
     * @throws ClassNotFoundException
     */
    private MethodSignature getNormalPictureHook() throws ClassNotFoundException {
        final String methodName = "capturePreview";
        final Class<?> paramClz1 = hookParam.classLoader.loadClass("com.kwai.camerasdk.videoCapture.CapturePreviewListener");
        final Class<?> paramClz2 = hookParam.classLoader.loadClass("com.kwai.camerasdk.models.DisplayLayout");
        final Class<?> paramClz3 = hookParam.classLoader.loadClass("com.kwai.camerasdk.models.CaptureImageMode");

        final Object[] params = new Object[] {
                paramClz1,
                int.class,
                int.class,
                paramClz2,
                paramClz3,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                        sCaptureStartTs = System.currentTimeMillis();
                        if (Integer.parseInt(param.args[1].toString()) > 0) {
                            final Object origin = param.args[0];
                            Class<?>[] interfaceList = origin.getClass().getInterfaces();
                            InvocationHandler handler = new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    Object ret = method.invoke(origin, args);
                                    long end = System.currentTimeMillis();
                                    if (end > sHookEndTs) {
                                        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG,"hd-capture:false");
                                        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "picture-size:"+param.args[1]+","+param.args[2]);
                                        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG,"capture-cost:"+(end - sCaptureStartTs));
                                    }
                                    return ret;
                                }
                            };
                            Object proxyInstance = Proxy.newProxyInstance(hookParam.classLoader, interfaceList, handler);
                            param.args[0] = proxyInstance;
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        sHookEndTs = System.currentTimeMillis();
                    }
                }
        };
        return new MethodSignature(TARGET_CLZ, methodName, params);
    }

}
