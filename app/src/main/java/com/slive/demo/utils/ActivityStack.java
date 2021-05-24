package com.slive.demo.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityStack {

    private static List<Activity> activityList = new ArrayList<>();

    public static void add(Activity activity) {
        activityList.add(activity);
    }

    public static void remove(Activity activity) {
        activityList.remove(activity);
    }
}
