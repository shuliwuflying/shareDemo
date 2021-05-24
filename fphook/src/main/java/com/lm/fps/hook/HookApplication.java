package com.lm.fps.hook;

import android.app.Application;

public class HookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XposedHookImpl.getInstance().init();
    }
}
