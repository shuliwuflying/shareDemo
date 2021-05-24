package com.slive.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.creation.ultrasonic.DoctorSingleton;
import com.creation.ultrasonic.IExamination;
import com.creation.ultrasonic.LineWords;
import com.slive.demo.hook.Hook;
import com.slive.demo.hook.LaunchHook;
import com.slive.demo.network.OkHttp;
import com.slive.demo.utils.BLog;
import com.slive.demo.utils.LogAnalysis;
import com.slive.demo.utils.NativeHook;


/**
 * @author:shuliwu
 * @description:
 * @version:1.0
 * @created:2017/10/14
 * @modify:
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        Hacker.init();
//        LaunchHook.initHook();
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        Hacker.hook();
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
        ContextHolder.sContext = this;
        OkHttp.init(getApplicationContext());
        LogAnalysis.init(true);
        Log.e("sliver", "Application onCreate");
        BLog.e("sliver", "Blog onCreate");
        android.util.Log.e("sliver", "app assetManager: "+getAssets());
    }


}
