package com.lm.hook.bcamera;

import com.lm.hook.base.LaunchHookBaseImpl;

class LaunchHookImpl extends LaunchHookBaseImpl {
    private boolean isInitd = false;
    @Override
    protected String getApplicationClass() {
        return "com.linecorp.b612.android.B612Application";
    }

    @Override
    protected void onLaunchCompleted() {
        super.onLaunchCompleted();
        if (!isInitd) {
            new BitmapHookImpl().init(hookParam);
            isInitd = true;
        }
    }
}
