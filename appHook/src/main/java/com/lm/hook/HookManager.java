package com.lm.hook;

import java.util.ArrayList;
import java.util.List;

public class HookManager {
    private static List<String> hookPackageList = new ArrayList<>();

    public static void init() {
       init(hookPackageList);
    }

    public static void init(List<String> hookList) {
        XposedHookImpl.getInstance().init();
        for(String pkgName: hookList) {
            XposedHookImpl.getInstance().addHookTarget(pkgName);
        }
    }

}
