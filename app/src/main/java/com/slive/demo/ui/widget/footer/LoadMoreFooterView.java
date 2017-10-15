package com.slive.demo.ui.widget.footer;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.slive.demo.R;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/10/15
 * @version: 1.0
 * @modify: liwushu
*/
public class LoadMoreFooterView extends FrameLayout {
    
    public static final int LOAD_GONE = 0;
    public static final int LOADING = 1;
    public static final int LOAD_ERROR = 2;
    public static final int LOAD_END = 3;
    
    @IntDef({LOAD_GONE,LOADING, LOAD_ERROR,LOAD_END})
    @interface Status{}
    
    @Status
    private int mStatus ;

    private View mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private OnRetryListener mOnRetryListener;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_irecyclerview_load_more_footer_view, this, true);

        mLoadingView = findViewById(R.id.loading_view);
        mErrorView = findViewById(R.id.load_error_view);
        mTheEndView = findViewById(R.id.load_end_view);

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        setStatus(LOAD_GONE);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
        updateStatusUI();
    }

    public boolean canLoadMore() {
        return mStatus == LOAD_GONE || mStatus == LOAD_ERROR;
    }

    private void updateStatusUI() {
        switch (mStatus) {
            case LOAD_GONE:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mTheEndView.setVisibility(View.GONE);
                break;

            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mTheEndView.setVisibility(View.GONE);
                break;

            case LOAD_ERROR:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(View.GONE);
                break;

            case LOAD_END:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }

    
    public interface OnRetryListener {
        void onRetry(LoadMoreFooterView view);
    }

}
