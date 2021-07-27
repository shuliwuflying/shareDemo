package com.slive.demo.hook;

import android.webkit.WebView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.hubble.Hubble;
import com.bytedance.hubble.HubbleEntry;

public class NewLaunchHook {
    private static HubbleEntry webViewEntry = new HubbleEntry();
    private static HubbleEntry threadEntry = new HubbleEntry();

    public static void init() {
        Hubble.setHubbleLogger(new Hubble.HubbleLogger() {
            @Override
            public void onStep(@NonNull String msg) {
                android.util.Log.e("Hubble", "msg: "+msg);
            }

            @Override
            public void onError(@NonNull String error, @Nullable Throwable throwable) {
                android.util.Log.e("Hubble", "error: "+error+"  throwable: "+throwable);
                if (throwable != null) {
                    throwable.printStackTrace();
                }
            }
        });
        hookWeb();
        hookThread();
    }

    public static void hookWeb() {
        //hook
        webViewEntry.setTargetMethod(WebView.class.getName(), "setDataDirectorySuffix", String.class);
        webViewEntry.setProxy(NewLaunchHook.class.getName(), "setDataDirectorySuffix", String.class);
        webViewEntry.setOrigin(NewLaunchHook.class.getName(), "setDataDirectorySuffixOrigin", String.class);
        Hubble.hook(webViewEntry);
    }



    //代理函数
    @Keep
    public static void setDataDirectorySuffix(String dir) {
        android.util.Log.e("Hubble", "setDataDirectorySuffix1111");
        android.util.Log.e("Hubble", android.util.Log.getStackTraceString(new Throwable("setDataDirectorySuffix")));
        Hubble.callOrigin(webViewEntry, null,  dir);
    }
    //备份函数
    @Keep
    public static void setDataDirectorySuffixOrigin(String dir) {

    }



    public static void hookThread() {
        //hook
        threadEntry.setTargetConstructor(Thread.class.getName());
        threadEntry.setProxy(NewLaunchHook.class.getName(), "newThreadProxy", Thread.class);
        threadEntry.setOrigin(NewLaunchHook.class.getName(), "newThreadOrigin", Thread.class);
        Hubble.hook(threadEntry);
    }

    public static void newThreadProxy(Thread thread) {
        android.util.Log.e("Hubble", "call new thread");
        Hubble.callOrigin(threadEntry, thread);
    }
    //备份函数
    @Keep
    public static void newThreadOrigin(Thread thread) {

    }
}