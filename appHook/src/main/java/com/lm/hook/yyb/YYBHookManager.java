package com.lm.hook.yyb;


import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class YYBHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        new LogHookImpl().init(param);
    }
}
