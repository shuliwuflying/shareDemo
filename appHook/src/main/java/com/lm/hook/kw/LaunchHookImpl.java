package com.lm.hook.kw;


import com.lm.hook.base.LaunchHookBaseImpl;

class LaunchHookImpl extends LaunchHookBaseImpl {

    @Override
    protected String getApplicationClass() {
        return "com.kwai.m2u.CameraApplication";
    }
}