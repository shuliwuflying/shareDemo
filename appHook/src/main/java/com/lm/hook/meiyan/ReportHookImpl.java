package com.lm.hook.meiyan;

import android.content.Context;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;

class ReportHookImpl extends BaseHookImpl {
    private static final String TAG = "ReportHookImpl";
    private static final String TARGET_CLZ = "com.umeng.analytics.MobclickAgent";
    private static final String METHOD_NAME = "onEvent";

    public ReportHookImpl() {
        hookEntityList.add(onEventHook1());
        hookEntityList.add(onEventHook2());
        hookEntityList.add(onEventHook3());
    }

    private MethodSignature onEventHook1() {
        return new MethodSignature(TARGET_CLZ, METHOD_NAME, new Object[] {
                Context.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG,"onEventHook1 beforeHookedMethod111");
                        for (Object obj : param.args) {
                            LogUtils.e(TAG,  "onEvent1: " + obj);
                        }
                        LogUtils.e(TAG,"onEventHook1 beforeHookedMethod222");
                    }
                }
        });
    }

    private MethodSignature onEventHook2() {
        return new MethodSignature(TARGET_CLZ, METHOD_NAME, new Object[] {
                Context.class,
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG,"onEventHook2 beforeHookedMethod111");
                        for (Object obj : param.args) {
                            LogUtils.e(TAG,  "onEvent2: " + obj);
                        }
                        LogUtils.e(TAG,"onEventHook2 beforeHookedMethod222");
                    }
                }
        });
    }

    private MethodSignature onEventHook3() {
        return new MethodSignature(TARGET_CLZ, METHOD_NAME, new Object[] {
                Context.class,
                String.class,
                Map.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG,"onEventHook3 beforeHookedMethod111");
                        for (Object obj : param.args) {
                            LogUtils.e(TAG,  "onEvent3: " + obj);
                        }
                        LogUtils.e(TAG,"onEventHook3 beforeHookedMethod222");
                    }
                }
        });
    }
}
