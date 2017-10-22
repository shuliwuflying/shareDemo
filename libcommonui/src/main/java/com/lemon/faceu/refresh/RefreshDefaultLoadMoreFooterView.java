package com.lemon.faceu.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/10/22
 * @version: 1.0
 * @modify: liwushu
 */
public class RefreshDefaultLoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private OnRetryListener mOnRetryListener;

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_irecyclerview_load_more_footer_view, this, true);

        mLoadingView = findViewById(R.id.loadingView);
        mErrorView = findViewById(R.id.errorView);
        mTheEndView = findViewById(R.id.theEndView);

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(RefreshDefaultLoadMoreFooterView.this);
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
        void onRetry(RefreshDefaultLoadMoreFooterView view);
    }
}