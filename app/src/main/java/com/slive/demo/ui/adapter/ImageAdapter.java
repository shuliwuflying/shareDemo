package com.slive.demo.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flying.common.RefreshViewHolder;
import com.slive.demo.R;
import com.slive.demo.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 16/4/5.
 */
public class ImageAdapter extends RecyclerView.Adapter<RefreshViewHolder> {

    private List<Image> mImages;

    private OnItemClickListener<Image> mOnItemClickListener;

    public ImageAdapter() {
        mImages = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener<Image> listener) {
        this.mOnItemClickListener = listener;
    }

    public void setList(List<Image> images) {
        mImages.clear();
        append(images);
    }

    public void append(List<Image> images) {
        int positionStart = mImages.size();
        int itemCount = images.size();
        mImages.addAll(images);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        mImages.remove(position);
        notifyItemRemoved(position);
    }

    public void clear(){
        mImages.clear();
        notifyDataSetChanged();
    }

    @Override
    public RefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);

        final ViewHolder holder = new ViewHolder(imageView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RefreshViewHolder holder, final int position) {
        ImageView imageView = (ImageView) holder.itemView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Image image = mImages.get(position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, image, v);
                }
            }
        });
        Image image = mImages.get(position);
        Glide.with(imageView.getContext())
                .load(image.image)
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    static class ViewHolder extends RefreshViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
