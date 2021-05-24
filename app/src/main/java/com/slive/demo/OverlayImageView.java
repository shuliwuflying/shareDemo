package com.slive.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

class OverlayImageView extends ImageView {
    public OverlayImageView(Context context) {
        super(context);
    }

    public OverlayImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                invalidate();
            }
        });
    }


    @Override
    public boolean hasOverlappingRendering() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
