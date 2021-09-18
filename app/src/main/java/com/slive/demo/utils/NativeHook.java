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

    public static native SigEntity nativeGeneratorSig(String paramString1, byte[][] paramArrayOfByte, String paramString2, Object paramObject);
}
