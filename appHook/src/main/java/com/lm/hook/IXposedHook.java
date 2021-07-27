package com.lm.hook;


import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface IXposedHook {

    void init();

    void addHookTarget(String pkgName);

    void invokeHook(XC_LoadPackage.LoadPackageParam param);
}
