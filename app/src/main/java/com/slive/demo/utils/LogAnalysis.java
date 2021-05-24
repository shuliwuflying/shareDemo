package com.slive.demo.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LogAnalysis {

    private static final int TIME_THRESHOLD = 10*1000;
    private static Handler sLogHandler;
    private static HashMap<String, Integer> logOutMap = new HashMap<>();
    private static boolean sIsOpen = false;

    public static void init(boolean isOpen) {
        sIsOpen = isOpen;
        if (!isOpen) {
            return;
        }
        HandlerThread handlerThread = new HandlerThread("log-analysis");
        handlerThread.start();
        sLogHandler = new Handler(handlerThread.getLooper());
        printlnLogAnalysis();
    }

    public static void recordLog(String filter) {
//        try {
//            throw new RuntimeException(new Throwable("recordLog"));
//        } catch (Throwable e) {
//            recordLog(e, filter);
//        }
        recordLogNew(Thread.currentThread().getStackTrace(), filter);
    }

    private static void recordLog(final Throwable e, final String filter) {
        if (!sIsOpen) {
            return;
        }
        sLogHandler.post(new Runnable() {
            @Override
            public void run() {
                StackTraceElement[] elements = e.getStackTrace();
                String lastTrace = "";
                System.out.println("elements.length: "+elements.length);
                for(StackTraceElement s: elements) {
                    System.out.println("s: "+s.toString());
                    if (s.toString().toLowerCase().contains("Log".toLowerCase()) || s.toString().toLowerCase().contains(filter.toLowerCase())) {
                        continue;
                    }
                    if (TextUtils.isEmpty(s.toString())) {
                        continue;
                    }
                    String className = s.toString();
                    int count = 1;
                    if (logOutMap.containsKey(className)) {
                        count = logOutMap.get(className);
                        count ++;
                    }
                    logOutMap.put(className, count);
                    return;
                }
            }
        });
    }

    private static void recordLogNew(final StackTraceElement[] elements, final String filter) {
        if (!sIsOpen) {
            return;
        }
        sLogHandler.post(new Runnable() {
            @Override
            public void run() {
                String lastTrace = "";
                System.out.println("elements.length: "+elements.length);
                boolean isAlreadyFilterLogMethod = false;
                for(StackTraceElement s: elements) {
                    System.out.println("s: "+s.toString());
                    if (s.toString().toLowerCase().contains("Log".toLowerCase()) || s.toString().toLowerCase().contains(filter.toLowerCase())) {
                        isAlreadyFilterLogMethod = true;
                        continue;
                    }
                    if (!isAlreadyFilterLogMethod) {
                        continue;
                    }

                    if (TextUtils.isEmpty(s.toString())) {
                        continue;
                    }
                    String className = s.toString();
                    int count = 1;
                    if (logOutMap.containsKey(className)) {
                        count = logOutMap.get(className);
                        count ++;
                    }
                    logOutMap.put(className, count);
                    return;
                }
            }
        });
    }

    private static void printlnLogAnalysis() {
//        sLogHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Iterator<Map.Entry<String, Integer>> it = logOutMap.entrySet().iterator();
//                System.out.println("printlnLogAnalysis size: "+logOutMap.size());
//                while(it.hasNext()) {
//                    Map.Entry<String, Integer> entry = it.next();
//                    System.out.println(entry.getKey()+" 111:111  "+entry.getValue());
//                }
//                printlnLogAnalysis();
//            }
//        },TIME_THRESHOLD);
    }
}
