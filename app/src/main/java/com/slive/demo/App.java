package com.slive.demo;

import android.app.Application;

import com.aspsine.irecyclerview.demo.network.OkHttp;

/**
 * @author:shuliwu
 * @description:
 * @version:1.0
 * @created:2017/10/14
 * @modify:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttp.init(getApplicationContext());
    }

}
