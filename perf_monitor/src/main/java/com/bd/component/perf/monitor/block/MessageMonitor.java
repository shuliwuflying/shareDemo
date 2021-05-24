package com.bd.component.perf.monitor.block;


import android.os.Message;

import com.bd.component.perf.monitor.utiils.PerfLog;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

public class MessageMonitor {

//    @TargetClass(value= "android.os.Handler", scope = Scope.ALL)
//    @Proxy("sendMessageAtTime")
//    public boolean sendMessageAtTime(Message msg, long uptimeMillis){
//        System.out.println("sendMessageAtTime uptimeMillis: "+uptimeMillis);
//        PerfLog.INSTANCE.logI("MessageMonitor", "MessageMonitor onStart");
//        return (Boolean)Origin.call();
//    }
//
//    @TargetClass(value = "android.os.Handler", scope = Scope.ALL)
//    @Proxy("post")
//    public boolean post(Runnable runnable) {
//        System.out.println("MessageMonitor post ");
//        PerfLog.INSTANCE.logI("MessageMonitor","post");
//        return (Boolean)Origin.call();
//    }
//
//    @TargetClass(value = "com.slive.demo.MainActivity", scope = Scope.ALL)
//    @Proxy("initViews")
//    public void initViews(){
//        System.out.println("MessageMonitor hello world");
//        PerfLog.INSTANCE.logI("MessageMonitor", "MessageMonitor onStart");
//        Origin.callVoid();
//    }
}
