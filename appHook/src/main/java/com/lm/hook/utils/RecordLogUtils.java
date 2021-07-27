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
    private static final String DIR = Environment.getExternalStorageDirectory()+File.separator+"analysis"+File.separator+"log";
    private static Handler logThreadHandler;
    private static RandomAccessFile randomAccessFile;
    private static boolean isInit = false;

    public static void init(String packageName) {
        if (isInit) {
            return ;
        }
        HandlerThread handlerThread = new HandlerThread("record_thread");
        handlerThread.start();
        logThreadHandler = new LogThreadHandler(handlerThread.getLooper());
        File dir = new File(DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String format = simpleDateFormat.format(date);
        Random random = new Random(System.currentTimeMillis());
        int randomNum = random.nextInt() % 1000;
        String fileName = packageName+"_"+format+"_"+randomNum+".txt";
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
