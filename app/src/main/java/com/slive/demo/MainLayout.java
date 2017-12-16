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

    ImageView imageView;
    int mStatusBarHeight;
    public MainLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initStatusBarInfo();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = (ImageView) findViewById(R.id.image_view);
        MarginLayoutParams layoutParams = (MarginLayoutParams) imageView.getLayoutParams();
        layoutParams.topMargin = -mStatusBarHeight;
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
