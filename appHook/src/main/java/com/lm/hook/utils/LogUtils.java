package com.lm.hook.utils;

public class LogUtils {

    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
    }


    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }


    public static void i(String tag, String msg) {
        android.util.Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void recordLog(String tag, String msg) {
        android.util.Log.w(tag, msg);
        RecordLogUtils.recordMsg(msg);
    }
}
