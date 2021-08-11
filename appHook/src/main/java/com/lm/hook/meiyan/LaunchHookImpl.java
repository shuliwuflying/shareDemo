package com.lm.hook.meiyan;

import com.lm.hook.base.LaunchHookBaseImpl;

public class LaunchHookImpl extends LaunchHookBaseImpl {
    private static final String TAG = "MeiYanLaunchHookImpl";


    @Override
    protected String getApplicationClass() {
        return "com.tencent.tinker.loader.app.TinkerApplication";
    }
}
