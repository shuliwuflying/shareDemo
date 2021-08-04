package com.lm.hook.kw;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

class KWLogHookImpl extends BaseHookImpl {
    private static final String TAG = "KWLogHookImpl";

    public KWLogHookImpl() {
        hookEntityList.add(getLogHookForTwoParams());
        hookEntityList.add(getLogHookForThreeParams());

//        hookEntityList.add(getCameraLogHook("v"));
//        hookEntityList.add(getCameraLogHook("d"));
//        hookEntityList.add(getCameraLogHook("i"));
//        hookEntityList.add(getCameraLogHook("w"));
//        hookEntityList.add(getCameraLogHook("e"));
//
//        hookEntityList.add(getCameraReportHook("a"));
//        hookEntityList.add(getCameraReportHook("b"));
//        hookEntityList.add(getCameraReportHook("c"));
//        hookEntityList.add(getCameraReportHook("d"));
//
//        hookEntityList.add(getWriteLogHook());
    }


    private static MethodSignature getLogHookForTwoParams() {
        final String targetClass = "com.kwai.camerasdk.log.Log";
        final String method = "getMsg";
        final Object[] methodParams = new Object[] {
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam para) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(Object obj: para.args) {
                            sb.append(obj);
                            sb.append(",");
                        }
                        LogUtils.e(TAG, sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClass, method, methodParams);
    }

    private static MethodSignature getLogHookForThreeParams() {
        final String targetClass = "com.kwai.camerasdk.log.Log";
        final String method = "getMsg";
        final Object[] methodParams = new Object[] {
                String.class,
                String.class,
                Throwable.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam para) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(Object obj: para.args) {
                            sb.append(obj);
                            sb.append(",");
                        }
                        LogUtils.e(TAG, sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClass, method, methodParams);
    }

    private static MethodSignature getCameraLogHook(final String methodName) {
        final String targetClz = "com.kwai.video.kscamerakit.log.KSCameraKitLog";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "method: "+methodName+ "    tag: "+param.args[0] +"   msg: "+param.args[1]);
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private static MethodSignature getCameraReportHook(final String methodName) {
        final String targetClz = "com.kwai.report.a.b";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (!param.args[1].toString().contains("YCNNPluginGetModel")) {
                            LogUtils.e(TAG, "method: "+methodName+ "    tag: "+param.args[0] +"   msg: "+param.args[1]);
                        }

                        if (param.args[0].toString().contains("ColdStartReportHelper")) {
                            LogUtils.e(TAG, android.util.Log.getStackTraceString(new Throwable("ColdStartReportHelper")));
                        }

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private static MethodSignature getWriteLogHook() {
        final String targetClz = "com.kwai.video.ksuploaderkit.logreporter.LogReporter";
        final String method = "writeLogger";
        final Object[] params = new Object[] {
                String.class,
                int.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "writeLogger: "+param.args[0]+" , "+param.args[1]+" , "+ param.args[2]);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }
}
