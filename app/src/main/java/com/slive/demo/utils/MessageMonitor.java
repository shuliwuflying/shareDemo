package com.slive.demo.utils;


import android.os.Message;


import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

public class MessageMonitor {
//
//    @TargetClass(value= "android.os.Handler", scope = Scope.SELF)
//    @Proxy("sendMessageAtTime")
//    public boolean sendMessageAtTime(Message msg, long uptimeMillis){
//        System.out.println("sendMessageAtTime uptimeMillis: "+uptimeMillis);
//        PerfLog.INSTANCE.logI("MessageMonitor", "MessageMonitor onStart");
//        return (Boolean)Origin.call();
//    }
//
//    @TargetClass(value = "android.os.Handler", scope = Scope.SELF)
//    @Proxy("post")
//    public boolean post(Runnable runnable) {
//        System.out.println("MessageMonitor post ");
//        PerfLog.INSTANCE.logI("MessageMonitor","post");
//        return (Boolean)Origin.call();
//    }
//
//    @TargetClass(value = "com.slive.demo.MainActivity", scope = Scope.SELF)
//    @Proxy("initViews")
//    public void initYYYY() {
//        System.out.println("MessageMonitor hello world");
//        PerfLog.INSTANCE.logI("MessageMonitor", "MessageMonitor initYYYY");
//        Origin.callVoid();
//    }
}
