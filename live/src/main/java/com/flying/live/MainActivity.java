package com.flying.live;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.flying.common.ConstantUtils;
import com.flying.live.receiver.Receiver1;
import com.flying.live.receiver.Receiver2;
import com.flying.live.receiver.Receiver3;
import com.flying.live.service.MyService1;
import com.flying.live.service.MyService2;
import com.flying.live.service.MyService3;
import com.flying.live.utils.Contants;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Receiver1 receiver1;
    Receiver2 receiver2;
    Receiver3 receiver3;

    TextView contentTv;
    TextView standard;
    TextView singleTop;
    TextView singleTask;
    TextView singleInstance;
    TextView otherApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register();
        initViews();
        bindListener();
        getRunTask();
    }

    private void initViews() {
        contentTv = (TextView) findViewById(R.id.content);
        standard = (TextView) findViewById(R.id.standard);
        singleTop = (TextView) findViewById(R.id.single_top);
        singleTask = (TextView) findViewById(R.id.single_task);
        singleInstance = (TextView) findViewById(R.id.single_instance);
        otherApp = (TextView) findViewById(R.id.other);
        String contentStr = "taskId:"+getTaskId();
        contentTv.setText(contentStr);
    }

    private void getRunTask() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list =  activityManager.getRunningTasks(3);
        if(list == null) {
            return;
        }

        String content = contentTv.getText().toString();
        StringBuffer sb= new StringBuffer();
        sb.append(content+"\n");
        for(ActivityManager.RunningTaskInfo taskInfo :list) {
            sb.append("  num: "+taskInfo.numActivities+"  baseActivity: "+taskInfo.baseActivity.getClassName()+"  topActivity: "+taskInfo.topActivity.getClassName()+"\n");
        }
        contentTv.setText(sb.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private void register() {
        receiver1 = new Receiver1();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(Contants.Action.RECEIVER_ACTION1);
        registerReceiver(receiver1,intentFilter1);

        receiver2 = new Receiver2();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(Contants.Action.RECEIVER_ACTION2);
        registerReceiver(receiver2,intentFilter2);

        receiver3 = new Receiver3();
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction(Contants.Action.RECEIVER_ACTION3);
        registerReceiver(receiver3,intentFilter3);
    }

    private void bindListener() {
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStartActivity(ConstantUtils.STANDARD_ACTION);
            }
        });

        singleTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStartActivity(ConstantUtils.SINGLE_TOP_ACTION);
            }
        });

        singleInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStartActivity(ConstantUtils.SINGLE_INSTANCE_ACTION);
            }
        });

        singleTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStartActivity(ConstantUtils.SINGLE_TASK_ACTION);
            }
        });

        otherApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStartActivity(ConstantUtils.OTHER_APP_ACTION);
            }
        }) ;
    }

    private void unRegister() {
        if(receiver1 != null) {
            unregisterReceiver(receiver1);
            receiver1 = null;
        }

        if(receiver2 != null) {
            unregisterReceiver(receiver2);
            receiver2 = null;
        }

        if(receiver3 != null) {
            unregisterReceiver(receiver3);
            receiver3 = null;
        }
    }

    private void startService1() {
        Intent intent = new Intent();
        intent.setClass(this, MyService1.class);
        startService(intent);
    }

    private void startService2() {
        Intent intent = new Intent();
        intent.setClass(this, MyService2.class);
        startService(intent);
    }

    private void startService3() {
        Intent intent = new Intent();
        intent.setClass(this, MyService3.class);
        startService(intent);
    }

    private void sendReceiver1() {
        Intent intent = new Intent(Contants.Action.RECEIVER_ACTION1);
        sendBroadcast(intent);
    }

    private void sendReceiver2() {
        Intent intent = new Intent(Contants.Action.RECEIVER_ACTION2);
        sendBroadcast(intent);
    }

    private void sendReceiver3() {
        Intent intent = new Intent(Contants.Action.RECEIVER_ACTION3);
        sendBroadcast(intent);
    }

    private void callStartActivity(String action) {
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }
}
