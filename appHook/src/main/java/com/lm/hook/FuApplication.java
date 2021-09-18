package com.lm.hook;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class FuApplication extends Application {

    public FuApplication() {
        Log.e("sliver","FuApplication");
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.e("sliver","FuApplication#attachBaseContext");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("sliver", "FuApplication#onCreate0000");
        Log.e("sliver", android.util.Log.getStackTraceString(new Throwable("FuApplication#onCreate")));
        Log.e("sliver", "FuApplication#onCreate1111");
    }
}
