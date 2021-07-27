package com.lm.hook.base;


import com.lm.hook.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.assetAsByteArray;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class BaseHookImpl {
    protected List<MethodSignature> hookEntityList = new ArrayList<>();
    protected XC_LoadPackage.LoadPackageParam hookParam;

    public void init(XC_LoadPackage.LoadPackageParam param) {
        try {
            this.hookParam = param;
            hook(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void hook(XC_LoadPackage.LoadPackageParam param) {
        for (MethodSignature method : hookEntityList) {
            method.doHook(param);
        }
        hookImpl();
    }

    protected void hookImpl() {

    }

    protected void hookMethod(MethodSignature methodSignature) {
        methodSignature.doHook(hookParam);
    }

    public static class MethodSignature {
        public String clzName;
        public String methodName;
        public Object[] params;

        public MethodSignature(String classTarget, String methodTarget, Object[] params) {
            this.clzName = classTarget;
            this.methodName = methodTarget;
            this.params = params;
        }

        public void doHook(XC_LoadPackage.LoadPackageParam param) {
            LogUtils.e("HookImpl", "method: " + methodName);
            try {
                if(methodName.equals("<init>")) {
                     findAndHookConstructor(clzName, param.classLoader, params);
                } else {
                    findAndHookMethod(clzName, param.classLoader, methodName, params);
                }
                LogUtils.e("HookImpl", "method: " + methodName +" success");
            } catch (Exception e) {
                LogUtils.e("HookImpl", "method: " + methodName + " failed2222  cause: " + e.getCause());
            }
        }
    }
}
