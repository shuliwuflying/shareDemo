package com.lemon.faceu.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flying.common.R;


/**
 * @author: liwushu
 * @description: 默认下拉刷新布局
 * @created: 2017/10/22
 * @version: 1.0
 * @modify: liwushu
*/
public class DefaultHeaderView extends LinearLayout implements RefreshStateListener {

    private TextView mRefreshLoadingTitle;

    private RoundProgressBar mRefreshLoadingProgressBar;

    private Animation mLoadingRotateAnim;

    private boolean rotated = false;

    private int mHeight;

    public DefaultHeaderView(Context context) {
        this(context, null);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mLoadingRotateAnim = AnimationUtils.loadAnimation(getContext(),R.anim.refresh_loading_rotate_anim);
        mRefreshLoadingTitle = (TextView) findViewById(R.id.refresh_loading_title);
        mRefreshLoadingProgressBar = (RoundProgressBar) findViewById(R.id.refresh_loading_bar);
    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        this.mHeight = headerHeight;
    }

    @Override
    public void onMove(boolean isComplete, boolean automatic, int moved) {
        if (!isComplete) {
            if (moved <= mHeight) {
                if (rotated) {
                    rotated = false;
                }
                int rate = (moved-getPaddingBottom()-getPaddingTop())*100/mHeight;
                mRefreshLoadingTitle.setText(R.string.refresh_load_pull_down_title);
                if(rate <= 75 && rate >=0)
                    mRefreshLoadingProgressBar.setProgress(rate);
            } else{
                mRefreshLoadingTitle.setText(R.string.refresh_load_release_title);
                if (!rotated) {
                    rotated = true;
                }
                mRefreshLoadingProgressBar.setProgress(75);
            }
        }
    }


    @Override
    public void onRefresh() {
        mRefreshLoadingTitle.setText(R.string.refresh_load_loading_title);
        mRefreshLoadingProgressBar.setAnimation(mLoadingRotateAnim);
        mRefreshLoadingProgressBar.startAnimation(mLoadingRotateAnim);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        rotated = false;
        mRefreshLoadingTitle.setText(R.string.refresh_load_complete_title);
        mRefreshLoadingProgressBar.clearAnimation();
        mRefreshLoadingProgressBar.setProgress(100);
    }

    @Override
    public void onReset() {
        rotated = false;
    }
}
