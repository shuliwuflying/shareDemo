package com.bytedance.android.hacker;

import android.util.Log;

import com.bytedance.android.bytehook.ByteHook;

public class Hacker {
    public static synchronized void init() {
        // init bytehook
        int status = ByteHook.init();
        if(status != 0) {
            Log.e("sliver", "bytehook init FAILED, status: " + status);
        } else {
            Log.e("sliver", "bytehook init Success, status: " + status);
        }

        // load hacker
        System.loadLibrary("hacker");
    }

    public static void hook() {
        NativeHacker.hook();
    }

    public static void unhook() {
        NativeHacker.unhook();
    }
}
