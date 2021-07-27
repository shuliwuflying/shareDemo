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
                        for (Object obj : param.args) {
                            LogUtils.i(TAG,  "onEvent1: " + obj);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

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
                        for (Object obj : param.args) {
                            LogUtils.i(TAG,  "onEvent2: " + obj);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

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
                        for (Object obj : param.args) {
                            LogUtils.i(TAG,  "onEvent3: " + obj);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        });
    }
}
