package com.slive.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;

public class MyService extends Service {
    public MyService() {
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
            android.util.Log.e("sliver", "onStartCommand");
            File file = new File(getExternalCacheDir().getAbsolutePath()+File.separator+"text.txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.delete();
            file.createNewFile();
            android.util.Log.e("sliver", "file.exists: "+file.exists());
            if (file.exists()) {
                android.util.Log.e("sliver", "file.getAbsolutePath: "+file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY_COMPATIBILITY;
    }
}