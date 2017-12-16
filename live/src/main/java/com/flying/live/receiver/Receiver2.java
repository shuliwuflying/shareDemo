package com.flying.live.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shuliwu on 2017/12/12.
 */

public class Receiver2 extends BroadcastReceiver {
    private static final String TAG = Receiver2.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG,"onReceive: "+action);
    }
}
