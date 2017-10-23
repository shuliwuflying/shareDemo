package com.lemon.faceu.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.flying.common.R;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/10/22
 * @version: 1.0
 * @modify: liwushu
 */
public class DefaultLoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private OnRetryListener mOnRetryListener;

    public DefaultLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mLoadingView = findViewById(R.id.loading_layout);
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

        setStatus(Status.GONE);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
        change();
    }

    public boolean canLoadMore() {
        return mStatus == Status.GONE || mStatus == Status.ERROR;
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

    public enum Status {
        GONE, LOADING, ERROR, THE_END
    }

    public interface OnRetryListener {
        void onRetry(DefaultLoadMoreFooterView view);
    }
}