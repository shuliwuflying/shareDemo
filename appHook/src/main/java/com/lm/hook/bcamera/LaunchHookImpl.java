package com.lm.hook.bcamera;

import com.lm.hook.base.LaunchHookBaseImpl;

class LaunchHookImpl extends LaunchHookBaseImpl {
    @Override
    protected String getApplicationClass() {
        return "com.linecorp.b612.android.B612Application";
    }
}
