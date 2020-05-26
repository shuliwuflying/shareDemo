package com.slive.demo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lemon.faceu.uimodule.refresh.RefreshViewHolder;
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Note:
                 * in order to get the right position, you must use the method with i- prefix in
                 * {@link RefreshViewHolder} eg:
                 * {@code RefreshViewHolder.getIPosition()}
                 * {@code RefreshViewHolder.getILayoutPosition()}
                 * {@code RefreshViewHolder.getIAdapterPosition()}
                 */
                final int position = holder.getIAdapterPosition();
                final Image image = mImages.get(position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, image, v);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RefreshViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView;
        Image image = mImages.get(position);
        Glide.with(imageView.getContext()).load(image.image).dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
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
