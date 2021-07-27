package com.lm.hook.base;

import android.content.Context;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

/**
 * GDT 插件下发crash hook
 */
public class AdHookImpl extends BaseHookImpl {
    private static final String TAG = "AdHookImpl";

    public AdHookImpl() {
        hookEntityList.add(new MethodSignature(
                "com.qq.e.comm.managers.GDTADManager",
                "isInitialized",
                new Object[]{new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "isInitialized");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "isInitialized afterHookedMethod");
                        param.setResult(false);
                    }
                }
                }));

        hookEntityList.add(new MethodSignature("com.qq.e.comm.managers.GDTADManager",
                "initWith", new Object[]{
                Context.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "initWith beforeHookedMethod");
                        param.setResult(false);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "initWith afterHookedMethod");
                        param.setResult(false);
                    }
                }
        }));

    }

}
