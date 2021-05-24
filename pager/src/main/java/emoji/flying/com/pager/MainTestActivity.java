package emoji.flying.com.pager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flying.common.ConstantUtils;

import java.util.List;

public class MainTestActivity extends Activity {

    TextView contentTv;
    TextView standard;
    TextView singleTop;
    TextView singleTask;
    TextView singleInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
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
    }


    private void callStartActivity(String action) {
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);


        startActivity(intent);
    }
}
