package com.heavenlyhell.instamemo.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.heavenlyhell.instamemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sande on 3/6/2018.
 */

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.GridViewHolder> {

    private Context context;
    private List<String> imageurls;

    public GridImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageurls = imageUrls;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(context)).inflate(R.layout.layout_single_image,
                parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
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
}
