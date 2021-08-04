package com.lm.hook.kw;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class LaunchHookImpl extends BaseHookImpl {
    private static long sStartTs = 0;
    private static boolean isRecord = false;

    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(launchStartHook());
    }

    /**
     * Application.onCreate()
     * @return
     */
    private static MethodSignature launchStartHook() {
        final String targetClz = "com.kwai.m2u.CameraApplication";
        final String method = "onCreate";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        sStartTs = System.currentTimeMillis();
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    public static void setFirstReceiveFrame() {
        if(!isRecord) {
            LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "launch-cost:" + (System.currentTimeMillis() - sStartTs));
            isRecord = true;
        }
    }
}
