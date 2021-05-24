package com.slive.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by shuliwu on 2017/12/6.
 */

public class MainLayout extends RelativeLayout {


    int mStatusBarHeight;
    public MainLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initStatusBarInfo();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ImageView imageView1 = (ImageView) findViewById(R.id.image_view1);
        ImageView imageView2 = (ImageView) findViewById(R.id.image_view2);
        ImageView imageView3 = (ImageView) findViewById(R.id.image_view3);
        ImageView imageView4 = (ImageView) findViewById(R.id.image_view4);
        ImageView imageView5 = (ImageView) findViewById(R.id.image_view5);
        ImageView imageView6 = (ImageView) findViewById(R.id.image_view6);

//        imageView1.setImageAlpha((int) (0.6f * 255));
//        imageView2.setImageAlpha((int) (0.6f * 255));
//        imageView3.setImageAlpha((int) (0.6f * 255));
//        imageView4.setImageAlpha((int) (0.6f * 255));
//        imageView5.setImageAlpha((int) (0.6f * 255));
//        imageView6.setImageAlpha((int) (0.6f * 255));
        imageView1.setAlpha(0.6f);
        imageView2.setAlpha(0.6f);
        imageView3.setAlpha(0.6f);
        imageView4.setAlpha(0.6f);
        imageView5.setAlpha(0.6f);
        imageView6.setAlpha(0.6f);
    }

    private void initStatusBarInfo() {
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        Log.e("TAG","resourceId: "+resourceId);
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            mStatusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
            Log.e("TAG","mStatusBarHeight: "+mStatusBarHeight);
        }
    }
}
