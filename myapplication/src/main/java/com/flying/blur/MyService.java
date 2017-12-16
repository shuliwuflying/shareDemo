package com.flying.blur;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyService extends IntentService {

    private static final String TAG = "MyService";

    public MyService(String name) {
        super(name);
    }

    public MyService() {
        super(TAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"onHandleIntent");
        Uri data = intent.getData();
        if(data != null) {
            String host = data.getHost();
            String scheme = data.getScheme();
            Log.e(TAG,"host: "+host);
            Log.e(TAG,"scheme: "+scheme);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }



}
