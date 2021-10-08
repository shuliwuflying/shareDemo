package com.lm.hook.kw;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import org.json.JSONObject;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class KwReportHook extends BaseHookImpl {
    private static final String TAG = "KwReportHook";
    private static boolean isFlag = true;

    public void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        try {
            LogUtils.e(TAG,"KwReportHook prepare");
            hookEntityList.add(getReportHook());
            hookEntityList.add(getIsChannelHook());
            hookEntityList.add(setStatsHolderHook("onDebugInfo"));

//            hookEntityList.add(setStatsHolderHook("onReportJsonStats"));
//            hookEntityList.add(setStatsHolderHook("onReportLiveJsonStats"));
//
        } catch (ClassNotFoundException e) {
            LogUtils.e(TAG,"ClassNotFoundException "+e.getMessage());
            e.printStackTrace();
        }
    }

    private MethodSignature getReportHook() throws ClassNotFoundException {
        final String targetClz = "com.kuaishou.android.security.ku.perf.KSecurityPerfReport";
        final String method = "a";
        final Class<?> tagClz = hookParam.classLoader.loadClass("com.kuaishou.android.security.ku.perf.KSecurityPerfReport$TAG");
        final Class<?> paramsClz = hookParam.classLoader.loadClass("com.kuaishou.android.security.adapter.common.InitCommonParams");
        Object[] methodParams = new Object[] {
                tagClz,
                paramsClz,
                String.class,
                int.class,
                JSONObject.class,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(Object obj: param.args) {
                            sb.append(obj);
                            sb.append(",");
                        }
                        LogUtils.e(TAG, "getReportHook");
                        LogUtils.e(TAG, sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    }
                }

        };
        return new MethodSignature(targetClz, method, methodParams);
    }


    private MethodSignature getSendLogHook() {
        final String targetClz = "com.kwai.video.kscamerakit.KSCameraKitLogReporter";
        final String methodName = "sendLog";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "sendLog: "+param.args[0]+" ,"+param.args[1]+" ,"+param.args[2]);
                        android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("sendLog")));
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getIsChannelHook() {
        final String targetClz = "com.kwai.m2u.manager.channel.ReleaseChannelManager";
        final String methodName = "isChannel";
        final Object[] params = new Object[] {
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "isChannel111: "+param.args[0]);
                        if("testlog".equalsIgnoreCase(param.args[0].toString())){
                            param.setResult(true);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature setStatsHolderHook(final String method) {
        final String targetClz = "com.kwai.camerasdk.stats.StatsHolder$1";
        final Object[] params = new Object[] {
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, method+": "+param.args[0]);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }
}
