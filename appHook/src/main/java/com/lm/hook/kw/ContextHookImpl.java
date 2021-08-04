package com.lm.hook.kw;


import android.content.ContentResolver;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

class ContextHookImpl extends BaseHookImpl {
    private static final String TAG = "ContextHookImpl";

    public ContextHookImpl() {
//        hookEntityList.add(hookContentResolverRegister());
        hookEntityList.add(getContentResolver());
    }


    private static MethodSignature getContentResolver() {
        final String targetClz = ContentResolver.class.getName();
        final String methodName = "registerContentObserver";
        final Object[] params = new Object[] {
                Uri.class,
                boolean.class,
                ContentObserver.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[0].toString().contains("KWAI_PROVIDER_AUTHORITY.com.kwai.m2u")) {
                            param.setResult(null);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "registerContentObserver after uri: "+param.args[0]);
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

//    private static MethodSignature hookContentResolverRegister() {
//        final String targetClz = "com.kwai.m2u.preference.h";
//        final String methodName = "a";
//        final Object[] params = new Object[] {
//                Object[].class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        LogUtils.e(TAG, "registerContentObserver before");
//                        param.setResult(null);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        LogUtils.e(TAG, "registerContentObserver after");
//                    }
//                }
//        };
//        return new MethodSignature(targetClz, methodName, params);
//    }
}
