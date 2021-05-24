package com.slive.demo;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    private long startTime = System.currentTimeMillis();
    private static int count = 0;
    private boolean isDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.e("sliver", "activity assetManager: "+getAssets()+"  getResource: "+getResources());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        count ++;
        if(action == MotionEvent.ACTION_DOWN ) {
            isDown = true;
            android.util.Log.e("sliver", "4444444444 activity: " + this.getClass().getSimpleName() + "   action_down  count: "+count);
        }

        if(action == MotionEvent.ACTION_UP ) {
            android.util.Log.e("sliver", "4444444444 activity: " + this.getClass().getSimpleName() + "   action_UP  count: "+count);
//            android.util.Log.e("sliver", ">>> activity: "+android.util.Log.getStackTraceString(new Throwable("ACTION_UP")));
        }

        if(action == MotionEvent.ACTION_MOVE ) {
            android.util.Log.e("sliver", "4444444444 activity: " + this.getClass().getSimpleName() + "   action_MOVE  count: "+count);
            if (isDown) {
//                android.util.Log.e("sliver", android.util.Log.getStackTraceString(new Throwable("action_move")));
            }
            isDown = false;
        }
        return super.dispatchTouchEvent(event);
    }
}