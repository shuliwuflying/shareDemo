package com.lm.hook.meiyan;

import com.lm.hook.base.LaunchHookBaseImpl;
import com.lm.hook.utils.LogUtils;

public class LaunchHookImpl extends LaunchHookBaseImpl {
    private static final String TAG = "MeiYanLaunchHookImpl";
    private static final String className1 = "com.tencent.tinker.loader.app.TinkerApplication";
    private static final String className2 = "com.meitu.remote.hotfix.app.RemoteHotfixApplication";
    public static boolean isHighVersion = false;
    private boolean mIsInit = false;

    @Override
    protected String getApplicationClass() {
        String name = className1;
        try {
            hookParam.classLoader.loadClass(className2);
            name = className2;
            isHighVersion = true;
        }catch (Throwable e) {
            name = className1;
            isHighVersion = false;
        }
        LogUtils.e(TAG,"applicationName: "+name);
        return name;
    }

    @Override
    protected void onLaunchCompleted() {
        super.onLaunchCompleted();
        if(!mIsInit) {
            new BitmapHookImpl().init(hookParam);
            mIsInit = true;
        }
    }
}
