package com.bytedance.android.hacker;

public class NativeHacker {
    public static void hook() {
        nativeHook();
    }
    public static void unhook() {
        nativeUnhook();
    }

    private static native void nativeHook();
    private static native void nativeUnhook();
}
