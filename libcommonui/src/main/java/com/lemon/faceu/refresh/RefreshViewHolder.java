package com.lemon.faceu.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author: liwushu
 * @description: recycler viewholder
 * @created: 2017/10/22
 * @version: 1.0
 * @modify: liwushu
*/
public abstract class RefreshViewHolder extends RecyclerView.ViewHolder {

    public RefreshViewHolder(View itemView) {
        super(itemView);
    }

    @Deprecated
    public final int getIPosition() {
        return getPosition() - 2;
    }

    public final int getILayoutPosition() {
        return getLayoutPosition() - 2;
    }

    public final int getIAdapterPosition() {
        return getAdapterPosition() - 2;
    }

    public final int getIOldPosition() {
        return getOldPosition() - 2;
    }

    public final long getIItemId() {
        return getItemId();
    }

    public final int getIItemViewType() {
        return getItemViewType();
    }
}
