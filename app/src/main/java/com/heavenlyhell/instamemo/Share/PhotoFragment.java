package com.heavenlyhell.instamemo.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.heavenlyhell.instamemo.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by sande on 3/5/2018.
 */

public class PhotoFragment extends Fragment {

    private static final String TAG = "PhotoFragment";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FloatingActionButton fabShare;
    private CropImageView cropImagePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        cropImagePhoto = view.findViewById(R.id.cropImagePhoto);
        fabShare = view.findViewById(R.id.fabShare);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intentCamera.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cropImagePhoto.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                Log.d(TAG, "onCropImageComplete: Cropping completed");
                Bitmap cropped = view.getCroppedImage();
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Further process for sharing...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: Written something ....");
            }
        });
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Further process for sharing...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: Written something ....");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            cropImagePhoto.setImageBitmap(thumbnail);
        } else {
            ((ShareActivity) getActivity()).setCurrentItem(0);
        }
    }
}
