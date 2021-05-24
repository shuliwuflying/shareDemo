package com.bd.component.perf.monitor.block;


import android.view.LayoutInflater;

import com.bd.component.perf.monitor.IPerfMonitor;

public class InflaterMonitor implements IPerfMonitor {

    @Override
    public String getHookClassName() {
        return LayoutInflater.class.getName();
    }

    @Override
    public String getHookMethodName() {
        return "null";
    }

    @Override
    public String getHookMethodSign() {
        return null;
    }
}
