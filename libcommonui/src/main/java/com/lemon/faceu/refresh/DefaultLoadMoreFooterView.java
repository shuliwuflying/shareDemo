package com.lemon.faceu.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.flying.common.R;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/10/22
 * @version: 1.0
 * @modify: liwushu
 */
public class DefaultLoadMoreFooterView extends FrameLayout implements ILoadMoreFooter{

    private LoadMoreStatus mStatus;

    private RoundProgressBar mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private OnRetryListener mOnRetryListener;

    private Animation mRotateAnimation;

    public DefaultLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mLoadingView = (RoundProgressBar) findViewById(R.id.loading_layout);
        mLoadingView.setProgress(75);
        mRotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.refresh_loading_rotate_anim);
        mErrorView = findViewById(R.id.load_error);
        mTheEndView = findViewById(R.id.load_end);
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(DefaultLoadMoreFooterView.this);
                }
            }
        });

        setStatus(LoadMoreStatus.GONE);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public LoadMoreStatus getStatus() {
        return mStatus;
    }

    public void setStatus(LoadMoreStatus status) {
        this.mStatus = status;
        change();
    }


    @Override
    public boolean canLoadMore() {
        return mStatus == LoadMoreStatus.GONE || mStatus == LoadMoreStatus.ERROR;
    }

    private void change() {
        switch (mStatus) {
            case GONE:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mLoadingView.setAnimation(mRotateAnimation);
                mLoadingView.startAnimation(mRotateAnimation);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                break;

            case THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }


    public interface OnRetryListener {
        void onRetry(DefaultLoadMoreFooterView view);
    }
}