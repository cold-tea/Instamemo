package com.heavenlyhell.instamemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.Share.GalleryFragment;

import java.util.List;

/**
 * Created by sande on 3/6/2018.
 */

public class GridGalleryAdapter extends RecyclerView.Adapter<GridGalleryAdapter.GridViewHolder> {

    private Context context;
    private List<String> imageurls;
    private GalleryFragment fragment;

    public GridGalleryAdapter(Context context, GalleryFragment fragment, List<String> imageUrls) {
        this.context = context;
        this.imageurls = imageUrls;
        this.fragment = fragment;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(context)).inflate(R.layout.layout_single_image,
                parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, final int position) {
        holder.ivProfileSingleGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onItemClick(
                        ((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap()
                );
            }
        });
        Glide.with(context).load(imageurls.get(position)).into(holder.ivProfileSingleGrid);
    }

    @Override
    public int getItemCount() {
        return imageurls.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileSingleGrid;
        public GridViewHolder(View itemView) {
            super(itemView);
            ivProfileSingleGrid = itemView.findViewById(R.id.ivProfileSingleGrid);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Bitmap image);
    }
}
