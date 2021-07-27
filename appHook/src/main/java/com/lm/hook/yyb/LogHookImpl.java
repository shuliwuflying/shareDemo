package com.lm.hook.yyb;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class LogHookImpl extends BaseHookImpl {
    private static final String TAG = "LogHookImpl";

    public void init(XC_LoadPackage.LoadPackageParam param) {
        this.hookParam = param;
        hookEntityList.add(getLogHook(hookParam, "d"));
        hookEntityList.add(getLogHook(hookParam, "e"));
        hookEntityList.add(getLogHook(hookParam, "w"));
        super.init(param);

    }

    private static MethodSignature getLogHook(XC_LoadPackage.LoadPackageParam hookParam, final String methodName) {
        final String targetClz = "com.tencent.pangu.dyelog.filelog.logmanager.DFLog";
        try {
            final Class<?> clz = hookParam.classLoader.loadClass("com.tencent.pangu.dyelog.filelog.logmanager.ExtraMessageType");
            Class<?> paramsClz = java.lang.reflect.Array.newInstance(clz, 1).getClass();
            return new MethodSignature(
                    targetClz,
                    methodName,
                    new Object[]{
                            String.class,
                            String.class,
                            paramsClz,
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < param.args.length; i++) {
                                        if (param.args[i] == null) {
                                            continue;
                                        }
                                        sb.append(param.args[i]);
                                        if (i != param.args.length - 1) {
                                            sb.append(",");
                                        }
                                    }
                                    String value = sb.toString();
                                    LogUtils.i(TAG, "Debug_" + methodName + ": " + value);
                                    android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("DFLog")));
                                }

                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                                }
                            }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
