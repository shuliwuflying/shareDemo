package com.lm.hook.base;

import com.lm.hook.utils.LogUtils;

import java.lang.reflect.Proxy;

import de.robv.android.xposed.XC_MethodHook;

public class ClassHookImpl extends BaseHookImpl{
    private static final String TAG = "ClassHookImpl";

    public ClassHookImpl() {
//        hookEntityList.add(getSuperClassHook());
//        hookEntityList.add(isAssignableFromHook());
//        hookEntityList.add(getPackageManageHook());
    }

    private MethodSignature getSuperClassHook() {
        final String targetClz = "java.lang.Class";
        final String method = "getSuperclass";
        Object[] parmas = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Object result = param.getResult();
                        LogUtils.e(TAG, "initWith afterHookedMethod result: "+result);
                        if (result == Proxy.class) {
                            LogUtils.e(TAG, "initWith afterHookedMethod result == Proxy");
                            param.setResult(Object.class);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, parmas);
    }

    private MethodSignature isAssignableFromHook() {
        final String targetClz = "java.lang.Class";
        final String method = "isAssignableFrom";
        Object[] parmas = new Object[] {
                Class.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Object result = param.getResult();
                        LogUtils.e(TAG, "isAssignableFrom afterHookedMethod result: "+result+"  params: "+param.args[0]);
                        if (param.args[0] == Proxy.class) {
                            LogUtils.e(TAG, "isAssignableFrom afterHookedMethod params == Proxy");
                            param.setResult(false);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, parmas);
    }

    private MethodSignature getPackageManageHook() {
        final String targetClz = "android.app.ActivityThread";
        final String method = "getPackageManager";
        Object[] parmas = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "before getPackageManageHook");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after getPackageManageHook");
                    }
                }
        };
        return new MethodSignature(targetClz, method, parmas);
    }
}
