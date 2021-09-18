package com.slive.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;

public class MyService2 extends Service {
    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags,startId);
        try {
            android.util.Log.e("sliver", "onStartCommand111");
            File file = new File(getExternalCacheDir().getAbsolutePath()+File.separator+"text111.txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            android.util.Log.e("sliver", "file111.exists: "+file.exists());
            if (file.exists()) {
                android.util.Log.e("sliver", "file111.getAbsolutePath: "+file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY_COMPATIBILITY;
    }
}