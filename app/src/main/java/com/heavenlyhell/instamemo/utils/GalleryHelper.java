package com.heavenlyhell.instamemo.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.heavenlyhell.instamemo.models.PhotosModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sande on 3/10/2018.
 */

public class GalleryHelper {
    public static String sortOrder = MediaStore.Images.Media.DATA + " DESC";
    public static final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final String[] projections =
            {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.DISPLAY_NAME};



    public static List<PhotosModel> getData(boolean home, Cursor cursor) {
        boolean dir = home;//used in GalleryPickerAdpater to load either image on buckets
        List<PhotosModel> photos = new ArrayList<>();
        List<String> bucketIds = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();
        String[] projections = GalleryHelper.projections;

        //Using cursor data get all image details as we discussed in step 2
        while (cursor.moveToNext()) {
            String imageName = cursor.getString(cursor.getColumnIndex(projections[4]));
            String imageBucket = cursor.getString(cursor.getColumnIndex(projections[2]));
            String bucketId = cursor.getString(cursor.getColumnIndex(projections[3]));
            String imagePath = cursor.getString(cursor.getColumnIndex(projections[1]));
            PhotosModel model;

            //if it is true add buckets into PhotosModel
            if (home) {
                model = new PhotosModel(bucketId, imageBucket, imagePath, null);

                //check to overcome duplicate bucketIds
                //if (!bucketIds.contains(bucketId)) {
                    photos.add(model);
                    bucketIds.add(bucketId);
                //}
            } else {
                //used to add images into PhotosModel
                model = new PhotosModel(bucketId, imageBucket, imagePath, imageName);

                //check to overcome duplicate image path
                if (!imagePaths.contains(imagePath)) {
                    photos.add(model);
                    imagePaths.add(imagePath);
                }
            }
        }
        return photos;
    }
}
