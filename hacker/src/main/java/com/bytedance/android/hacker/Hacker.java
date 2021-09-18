package com.bytedance.android.hacker;

import android.util.Log;

import com.bytedance.android.bytehook.ByteHook;

public class Hacker {
    private static boolean isInit = false;
    public static synchronized void init() {

        if (isInit) {
            return;
        }
        isInit = true;
        // init bytehook
        int status = ByteHook.init(new ByteHook.ConfigBuilder()
                .setMode(ByteHook.Mode.AUTOMATIC)
                .setDebug(true).build());

        // load hacker
        Log.e("sliver", "load hacker");
        System.loadLibrary("hacker");
        hook();
        if(status != 0) {
            Log.e("sliver", "bytehook init FAILED, status: " + status);
        } else {
            Log.e("sliver", "bytehook init Success, status: " + status);
        }
    }

    public static void hook() {
        NativeHacker.hook();
    }

    public static void unhook() {
        NativeHacker.unhook();
    }
}
