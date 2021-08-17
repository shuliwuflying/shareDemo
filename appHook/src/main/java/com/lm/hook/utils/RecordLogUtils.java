package com.lm.hook.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RecordLogUtils {
    private static final String TAG = "RecordLogUtils";
//    private static final String DIR = Environment.getExternalStorageDirectory()+File.separator+"analysis"+File.separator+"log";
    private static Handler logThreadHandler;
    private static RandomAccessFile randomAccessFile;
    private static boolean isInit = false;

    public static void init(String packageName) {
        if (isInit) {
            return ;
        }
        String rootDir = Environment.getExternalStorageDirectory()+File.separator+"analysis"+File.separator+"log";
        LogUtils.e(TAG, "rootDir: "+rootDir);
        HandlerThread handlerThread = new HandlerThread("record_thread");
        handlerThread.start();
        logThreadHandler = new LogThreadHandler(handlerThread.getLooper());
        File dir = new File(rootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String format = simpleDateFormat.format(date);
        String fileName = packageName+"_"+format+".txt";
        File file = new File(dir, fileName);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            isInit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class LogThreadHandler extends Handler {

        public LogThreadHandler(Looper looper) {
            super(looper);

        }

        @Override
        public void handleMessage(Message msg) {
            String content = String.valueOf(msg.obj);
            if (TextUtils.isEmpty(content)) {
                return;
            }
            saveToFile(content);
        }
    }

    public static void recordMsg(String value) {
        Message msg = Message.obtain();
        msg.obj = value;
        logThreadHandler.sendMessage(msg);
    }

    private static void saveToFile(String content) {
        try {
            randomAccessFile.writeBytes(content);
            randomAccessFile.writeBytes("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
