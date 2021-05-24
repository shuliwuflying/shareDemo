package com.slive.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

class NoneOverlayImageView extends ImageView {
    public NoneOverlayImageView(Context context) {
        super(context);
    }

    public NoneOverlayImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoneOverlayImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
