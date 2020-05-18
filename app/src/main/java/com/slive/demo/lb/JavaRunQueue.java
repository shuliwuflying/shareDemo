package com.slive.demo.lb;

import java.util.ArrayList;
import java.util.List;

public class JavaRunQueue {
    static List<Runnable> runnableList = new ArrayList<>();

    public static void addRunnable(Runnable runnable) {
        runnableList.add(runnable);
    }

    public static void removeRunnable(Runnable runnable) {
        runnableList.remove(runnable);
    }

    public static int size() {
        return runnableList.size();
    }
}
