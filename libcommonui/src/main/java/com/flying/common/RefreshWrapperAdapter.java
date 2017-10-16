package com.flying.common;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author: liwushu
 * @description: refresh recycler view 的装饰Adapter
 * @created: 2017/10/15
 * @version: 1.0
 * @modify: liwushu
*/
public class RefreshWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int REFRESH_HEADER = Integer.MIN_VALUE;
    protected static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;
    protected static final int START_OFFSET = 1;

    private final RecyclerView.Adapter mAdapter;

    private final RefreshHeaderLayout mRefreshHeaderContainer;

    private final FrameLayout mLoadMoreFooterContainer;

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            RefreshWrapperAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            RefreshWrapperAdapter.this.notifyItemRangeChanged(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            RefreshWrapperAdapter.this.notifyItemRangeChanged(positionStart + 1, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            RefreshWrapperAdapter.this.notifyItemRangeInserted(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            RefreshWrapperAdapter.this.notifyItemRangeRemoved(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            RefreshWrapperAdapter.this.notifyDataSetChanged();
        }
    };

    public RefreshWrapperAdapter(RecyclerView.Adapter adapter, RefreshHeaderLayout refreshHeaderContainer, FrameLayout loadMoreFooterContainer) {
        this.mAdapter = adapter;
        this.mRefreshHeaderContainer = refreshHeaderContainer;
        this.mLoadMoreFooterContainer = loadMoreFooterContainer;
        mAdapter.registerAdapterDataObserver(mObserver);
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RefreshWrapperAdapter refreshWrapperAdapter = (RefreshWrapperAdapter) recyclerView.getAdapter();
                    if (isFullSpanType(refreshWrapperAdapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position - 2);
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    private boolean isFullSpanType(int type) {
        return type == REFRESH_HEADER || type == LOAD_MORE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return REFRESH_HEADER;
        } else if (0 < position && position < mAdapter.getItemCount() + START_OFFSET) {
            return mAdapter.getItemViewType(position - START_OFFSET);
        } else if (position == mAdapter.getItemCount() + START_OFFSET) {
            return LOAD_MORE_FOOTER;
        }

        throw new IllegalArgumentException("Wrong type! Position = " + position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == REFRESH_HEADER) {
            return new RefreshHeaderContainerViewHolder(mRefreshHeaderContainer);
        } else if (viewType == LOAD_MORE_FOOTER) {
            return new LoadMoreFooterContainerViewHolder(mLoadMoreFooterContainer);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (0 < position && position < mAdapter.getItemCount() + 1) {
            mAdapter.onBindViewHolder(holder, position - 1);
        }
    }

    static class RefreshHeaderContainerViewHolder extends RecyclerView.ViewHolder {

        public RefreshHeaderContainerViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class LoadMoreFooterContainerViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreFooterContainerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
