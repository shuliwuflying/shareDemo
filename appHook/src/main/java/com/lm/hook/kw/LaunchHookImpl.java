package com.lm.hook.kw;


import com.lm.hook.base.LaunchHookBaseImpl;

/**
 * 启动耗时
 */
class LaunchHookImpl extends LaunchHookBaseImpl {
    boolean isInit = false;

    @Override
    protected String getApplicationClass() {
        return "com.kwai.m2u.CameraApplication";
    }

    @Override
    protected void onLaunchCompleted() {
        super.onLaunchCompleted();
        if (!isInit) {
            new BitmapHookImpl().init(hookParam);
            isInit = true;
        }
    }
}
