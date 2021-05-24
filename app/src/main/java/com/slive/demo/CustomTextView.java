package com.slive.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class CustomTextView extends TextView {
    private int countTouch = 0;
    private int postCount = 0;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        boolean ret = super.onTouchEvent(event);

        if (action == MotionEvent.ACTION_UP) {
            android.util.Log.e("sliver", "CustomTextView action_up  ret: "+ret);
//            sleep(200);
            countTouch ++;
//            android.util.Log.e("sliver", "CustomTextView action: "+action +"  ret: "+ret +"  count: "+countTouch);
//            android.util.Log.e("sliver", android.util.Log.getStackTraceString(new Throwable("onTouchEvent.Action_UP")));
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    postCount ++;
                    android.util.Log.e("sliver", "onTouchEvent post: "+postCount);
                }
            });
        }

        if (action == MotionEvent.ACTION_MOVE) {
            android.util.Log.e("sliver", "CustomTextView action_move  ret: "+ret);
        }

        if (action == MotionEvent.ACTION_DOWN) {
            android.util.Log.e("sliver", "CustomTextView action_down  ret: "+ret);
//            sleep(100);
//            android.util.Log.e("sliver", android.util.Log.getStackTraceString(new Throwable("onTouchEvent.Action_DOWN")));
        }
        return ret;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {

        }
    }
}
