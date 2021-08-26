package com.lm.hook.kw;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class KWPushHookManager {

    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        new ContextHookImpl().init(param);
    }
}
