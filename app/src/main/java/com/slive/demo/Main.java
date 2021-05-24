package com.slive.demo;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookLoadPackage {
    private static final String TAG = "HookTest  ";
    private static List<String> pkgList = new ArrayList<>();
    private static final String BEAUTY_ME = "com.gorgeous.lite";
    private static final String KUAI_YING = "com.kwai.videoeditor";

    static {
        pkgList.add("com.lemon.faceu");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        printLog("handleLoadPackage: " + lpparam.packageName);
        hookBeautyMe(lpparam);
        hookKuaiYing(lpparam);
        hookTest(lpparam);
    }

    private void hookBeautyMe(XC_LoadPackage.LoadPackageParam param) {
        if (BEAUTY_ME.equals(param.packageName)) {
            printLog("hookBeautyMe begin");
            findAndHookMethod("com.light.beauty.mainpage.MainActivity", param.classLoader, "onCreate",
                    Bundle.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            // this will be called before the clock was updated by the original method
                        }
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            printLog("hookBeautyMe afterHookedMethod" + param.thisObject);
                            Toast.makeText((Context)param.thisObject, "you are the Best", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private void hookKuaiYing(XC_LoadPackage.LoadPackageParam param) {
        if (KUAI_YING.equals(param.packageName)) {
            printLog("hookKuaiYing begin");
            findAndHookMethod("android.view.TextureView", param.classLoader, "updateLayer",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            // this will be called before the clock was updated by the original method
                        }
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            printLog("hookKuaiYing afterHookedMethod" + param.thisObject);
                        }
                    }
            );
        }
    }

    private void hookTest(XC_LoadPackage.LoadPackageParam param) {
        findAndHookMethod("com.slive.demo.MVVMActivity", param.classLoader, "getTextViewTitle", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                printLog("afterHookedMethod" + param.thisObject);
                param.setResult("bbbbbbbb");
            }
        });
    }

    private void printLog(String msg) {
        XposedBridge.log(TAG + msg);
    }

}
