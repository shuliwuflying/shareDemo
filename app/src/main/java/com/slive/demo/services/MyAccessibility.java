package com.slive.demo.services;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.slive.demo.ContextHolder;

import java.util.List;

public class MyAccessibility extends AccessibilityService {

    private static final String TAG = "sliver";
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName().equals("com.android.systemui")) {
            Log.i(TAG, "com.android.systemui");
            return;
        }
        print();
        Log.e(TAG, "onAccessibilityEvent: " + event.toString());
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG, "onServiceConnected");
    }

    private void print() {
        AccessibilityNodeInfo accessibilityNodeInfo= getRootInActiveWindow();
        Log.e(TAG, "accessibilityNodeInfo: "+accessibilityNodeInfo);
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> list =  accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.slive.demo:id/container_test");
        Log.e(TAG, "list: "+list);
        for (final AccessibilityNodeInfo info: list) {
            Log.e(TAG, "info: "+info);
            final View view = buildView();
//            uiHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    info.addChild(view);
//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ViewParent parent = view.getParent();
//                            Log.e(TAG, "view.getParent: "+parent);
//                            if (parent != null) {
//                                ViewGroup group = (ViewGroup) parent;
//                                Log.e(TAG, "children: : "+group.getChildCount());
//                            }
//
//                        }
//                    });
//                }
//            });
            List<AccessibilityNodeInfo.AccessibilityAction> actionList = info.getActionList();
            if (actionList != null) {
                for(AccessibilityNodeInfo.AccessibilityAction action: actionList) {
                    Log.e(TAG, "action: "+action);
                }
            }
        }
    }

    private View buildView() {
        View view = new View(ContextHolder.sContext);
        return view;
    }
}
