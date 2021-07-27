package com.lm.hook.base;

import android.content.Context;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;

public class LogHookIml extends BaseHookImpl {
    private static final String TAG = "LogHookIml";

    public LogHookIml() {
        hookEntityList.add(getMethodA());
        hookEntityList.add(getMethodF());

        hookEntityList.add(new MethodSignature(
                "com.linecorp.b612.android.base.util.c",
                "c",
                new Object[]{
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "c: " + object);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                }));

        hookEntityList.add(new MethodSignature(
                "com.tendcloud.tenddata.TCAgent",
                "onEvent",
                new Object[]{
                        Context.class,
                        String.class,
                        String.class,
                        Map.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "TCAgent.onEvent111: " + object);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                }));


        hookEntityList.add(new MethodSignature(
                "s42",
                "b",
                new Object[]{
                        String.class,
                        Object[].class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "s42.b: " + object);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                }));

        hookEntityList.add(new MethodSignature(
                "s42",
                "c",
                new Object[]{
                        String.class,
                        Object[].class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "s42.b: " + object);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                }));

        hookEntityList.add(new MethodSignature(
                "s42",
                "e",
                new Object[]{
                        String.class,
                        Object[].class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "s42.b: " + object);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                }));


    }

    private BaseHookImpl.MethodSignature getMethodA() {
        return new MethodSignature(
                "s42",
                "a",
                new Object[]{
                        String.class,
                        Object[].class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "a: " + object);
                                    if (object != null) {
                                        String msg = object.toString();
                                        if (msg.contains("onFirstRender-end")) {
                                            android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("onFirstRender-end")));
                                        } else if (msg.contains("OnlyFilter")) {
                                            android.util.Log.e(TAG, android.util.Log.getStackTraceString(new Throwable("OnlyFilter")));
                                        }
                                    }
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }


    private BaseHookImpl.MethodSignature getMethodF() {
        return new MethodSignature(
                "s42",
                "f",
                new Object[]{
                        String.class,
                        Object[].class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                for (Object object : param.args) {
                                    LogUtils.e(TAG, "s42.f: " + object);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

}
