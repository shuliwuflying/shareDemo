package com.slive.demo.utils;

public class NativeHook {

    static {
        System.loadLibrary("loop_hook");
    }

    public static void test() {
        nativeTestStrlen();
        nativeTestAdd();
    }

    private static native void nativeTestStrlen();

    public static native void nativeTestAdd();
}
