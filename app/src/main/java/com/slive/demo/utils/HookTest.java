package com.slive.demo.utils;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

public class HookTest {

//    @Proxy("e")
//    @TargetClass(value = "android.util.Log", scope = Scope.SELF)
//    public static int anyName(String tag, String msg){
//        msg = msg + "lancet";
//        LogAnalysis.recordLog("anyName");
//        return (int) Origin.call();
//    }
//
//
//    @Proxy("e")
//    @TargetClass(value = "com.slive.demo.utils.BLog")
//    public static int anyKotlin(String tag, String msg){
//        msg = msg + "lancet";
//        return (int) Origin.call();
//    }

    @Insert("UninitializedPropertyAccessException")
    @TargetClass(value = "kotlin.jvm.internal")
    public static void lateInitHook(String propertyName){
        android.util.Log.getStackTraceString(new Throwable("UninitializedPropertyAccessException"));
        android.util.Log.e("sliver", "lateInitHook: "+propertyName);
        Origin.callVoid();
    }

    @Insert("throwUninitializedPropertyAccessException")
    @TargetClass(value = "kotlin.jvm.internal.Intrinsics")
    public static void lateInitHook1111(String propertyName){
        android.util.Log.getStackTraceString(new Throwable("throwUninitializedPropertyAccessException"));
        android.util.Log.e("sliver", "lateInitHook1111: "+propertyName);
        Origin.callVoid();
    }

    @Insert("checkNotNull")
    @TargetClass(value = "kotlin.jvm.internal.Intrinsics")
    public static void checkNotNullHook(Object object){
        android.util.Log.e("sliver", "checkNotNullHook: "+object);
        Origin.callVoid();
    }

    @Insert("checkNotNull")
    @TargetClass(value = "kotlin.jvm.internal.Intrinsics")
    public static void checkNotNullHook1111(Object object, String msg){
        android.util.Log.e("sliver", "checkNotNullHook1111: "+object);
        Origin.callVoid();
    }

    @Insert("throwNpe")
    @TargetClass(value = "kotlin.jvm.internal.Intrinsics")
    public static void throwNpeHook() {
        android.util.Log.e("sliver", "throwNpeHook1111: "+android.util.Log.getStackTraceString(new Throwable("throwNpeHook")));
        Origin.callVoid();
    }


}
