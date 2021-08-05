package com.lm.hook.meiyan;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

public class MeiYanLaunchHookImpl extends BaseHookImpl {
    private static final String TAG = "MeiYanLaunchHookImpl";
    private static long sStartTime = 0;

    public MeiYanLaunchHookImpl() {
        hookEntityList.add(applicationOnCreateHook());
    }

    private MethodSignature applicationOnCreateHook() {
        final String targetClz = "com.tencent.tinker.loader.app.TinkerApplication";
        final String method = "onCreate";
        final Object[] params = new Object[] {
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    sStartTime = System.currentTimeMillis();
                }
            }
        };
        return new MethodSignature(targetClz, method, params);
    }

    public static void recordFirstFrame() {
        LogUtils.recordLog(TAG, "launch_cost: "+(System.currentTimeMillis() - sStartTime));
    }

}
