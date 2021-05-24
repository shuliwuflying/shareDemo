package com.slive.demo.utils;

import android.os.Looper;


import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

public class ViewAop {

//    @TargetClass(value = "android.view.View", scope = Scope.LEAF)
//    @Insert(value = "isOpaque", mayCreateSuper = true)
//    public boolean isOpaque() {
//        checkThread((android.view.View) This.get(), "isOpaque");
//        return (boolean) Origin.call();
//    }
//
//
//    @TargetClass(value = "android.view.View", scope = Scope.LEAF)
//    @Insert(value = "invalidate", mayCreateSuper = true)
//    public void invalidate() {
//        checkThread((android.view.View) This.get(), "invalidate");
//        Origin.callVoid();
//    }
//
//
//    @TargetClass(value = "android.view.ViewGroup", scope = Scope.LEAF)
//    @Insert(value = "onDescendantInvalidated", mayCreateSuper = true)
//    public void onDescendantInvalidated(android.view.View child, android.view.View target) {
//        checkThread((android.view.View) This.get(),"onDescendantInvalidated");
//        Origin.callVoid();
//    }
//
//    public static void checkThread(android.view.View view, String tag) {
//        android.util.Log.e("sliver", view+"."+tag);
//        if (Looper.getMainLooper() != Looper.myLooper()) {
//            android.util.Log.e("sliver", tag+" 11111111111111");
//        }
//    }

}
