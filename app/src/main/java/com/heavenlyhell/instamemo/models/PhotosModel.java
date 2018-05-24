package com.heavenlyhell.instamemo.models;

/**
 * Created by sande on 3/10/2018.
 */

public class PhotosModel {
    private String imageName;
    private String imageBucket;
    private String bucketId;
    private String imagePath;

    public PhotosModel(String bucketId, String imageBucket,  String imagePath, String imageName) {
        this.imageName = imageName;
        this.imageBucket = imageBucket;
        this.bucketId = bucketId;
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageBucket() {
        return imageBucket;
    }

    public void setImageBucket(String imageBucket) {
        this.imageBucket = imageBucket;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "PhotosModel{" +
                "imageName='" + imageName + '\'' +
                ", imageBucket='" + imageBucket + '\'' +
                ", bucketId='" + bucketId + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
