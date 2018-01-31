package com.flying.processor;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        TextView tv = findViewById(R.id.test);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(v);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(flag);
        }
    }

    private void showPop(final View view) {
        View main = LayoutInflater.from(this).inflate(R.layout.layout_main_activity,null);
        final PopupWindow popupWindow = new PopupWindow(main,500,500);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        popupWindow.setContentView(main);
        TextView tv = main.findViewById(R.id.test);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setFocusable(true);
                Toast.makeText(view.getContext(),"onClick",Toast.LENGTH_SHORT).show();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        View root = main.getRootView();
        if(root != null) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            root.setSystemUiVisibility(flag);
        }
        popupWindow.showAsDropDown(view);
    }
}
