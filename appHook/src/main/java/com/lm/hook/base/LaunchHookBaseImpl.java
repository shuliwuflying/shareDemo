package com.lm.hook.base;

import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class LaunchHookBaseImpl extends BaseHookImpl {

    private long sStartTs = 0;
    private boolean isRecord = false;
    protected long sAppStartTs = 0;
    protected boolean sIsRecordAppStart = false;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(launchStartHook());
    }

    /**
     * Application.onCreate()
     * @return
     */
    private  MethodSignature launchStartHook() {
        final String targetClz = getApplicationClass();
        final String method = "onCreate";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        sAppStartTs = System.currentTimeMillis();
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    public void setFirstReceiveFrame() {
        if(!isRecord) {
            LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "launch-cost:" + (System.currentTimeMillis() - sAppStartTs));
            isRecord = true;
        }
    }


    protected abstract String getApplicationClass();
}
