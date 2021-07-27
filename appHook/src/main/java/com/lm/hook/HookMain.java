package com.lm.hook;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class HookMain implements IXposedHookLoadPackage {
    private static final String TAG = "XposedHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        printLog("pkgName: "+lpparam.packageName);
        XposedHookImpl.getInstance().invokeHook(lpparam);
    }

    private void printLog(String msg) {
        XposedBridge.log(TAG + msg);
    }
}
