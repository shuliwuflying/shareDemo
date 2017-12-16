package com.flying.blur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    View mainView;
    View mainView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.main_view);
        mainView2 = findViewById(R.id.main_view2);
//        mainView.post(new Runnable() {
//            @Override
//            public void run() {
//                int mainViewHeight=mainView.getMeasuredHeight();
//                int mainViewWidth = mainView.getMeasuredWidth();
//                int mainView2Height= mainView2.getMeasuredHeight();
//                int mainView2Width = mainView2.getMeasuredWidth();
//                Bitmap bitmap1 = Bitmap.createBitmap(mainViewWidth,mainViewHeight, Bitmap.Config.ARGB_8888);
//                Bitmap bitmap2 = Bitmap.createBitmap(mainView2Width,mainView2Height,Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap1);
//                canvas.drawColor(Color.CYAN);
//                Canvas canvas1 = new Canvas(bitmap2);
//                canvas1.drawColor(Color.CYAN);
//                mainView.setBackground(new BitmapDrawable(getResources(),bitmap1));
//                mainView2.setBackground(new BitmapDrawable(getResources(),BlurBitmapUtil.blurBitmap(getApplicationContext(),bitmap2,20)));
//            }
//        });
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onClick","onClick");
                //Uri uri = Uri.parse("faceu://live/share");
                Uri uri = Uri.parse("faceu://faceu.zmxy.callback");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(uri);
                Log.e("onclick","schema:"+uri.getScheme()+"  host: "+uri.getHost()+" package: "+getPackageName());
                //intent.setPackage("com.lemon.faceu");
                startActivity(intent);
            }
        });

        mainView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MyService.class);
                startService(intent);
            }
        });
    }
}
