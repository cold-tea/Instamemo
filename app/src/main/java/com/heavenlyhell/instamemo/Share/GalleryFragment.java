package com.heavenlyhell.instamemo.Share;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.models.PhotosModel;
import com.heavenlyhell.instamemo.utils.GalleryHelper;
import com.heavenlyhell.instamemo.utils.GridGalleryAdapter;
import com.heavenlyhell.instamemo.utils.GridImageAdapter;
import com.heavenlyhell.instamemo.utils.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sande on 3/5/2018.
 */

public class GalleryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        GridGalleryAdapter.ItemClickListener{

    private static final String TAG = "GalleryFragment";

    private ImageView ivGalleryAppbar;
    private TextView tvGalleryNext;
    private ImageView ivGalleryMain;
    private RecyclerView rvGallery;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ivGalleryAppbar = view.findViewById(R.id.ivGalleryAppbar);
        ivGalleryMain = view.findViewById(R.id.ivGalleryMain);
        rvGallery = view.findViewById(R.id.rvGallery);
        tvGalleryNext = view.findViewById(R.id.tvShareAppbarNext);

        ivGalleryAppbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tvGalleryNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Next Pressed", Toast.LENGTH_LONG).show();
            }
        });
        getLoaderManager().initLoader(1, null, this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tempRecyclerSetup();
    }

    private void tempRecyclerSetup() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL,
                false);
        rvGallery.setHasFixedSize(true);
        rvGallery.setLayoutManager(manager);
        rvGallery.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset));
    }

    private List<String> prepareUrls() {
        Log.d(TAG, "prepareUrls: Called");
        List<String> imageUrls = new ArrayList<>();
        for (PhotosModel photosModel : Constants.photos) {
            imageUrls.add(photosModel.getImagePath());
        }
        return imageUrls;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: Called");
        //create and return CursorLoader. that will take care of creating a Cursor for the data being displayed by uri. and projections and sort the images (showed in step 2)
        return new CursorLoader(getContext(),
                GalleryHelper.uri,
                GalleryHelper.projections,
                null,
                null,
                GalleryHelper.sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: Called");
        if (Constants.photos.size() == 0)
            Constants.photos = GalleryHelper.getData(true, data);
        Log.d(TAG, "onLoadFinished: " + Constants.photos);
        rvGallery.setAdapter(new GridGalleryAdapter(getContext(), GalleryFragment.this, prepareUrls
                ()));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(Bitmap bitmap) {
        ivGalleryMain.setImageBitmap(bitmap);
        tvGalleryNext.setEnabled(true);
    }
}
