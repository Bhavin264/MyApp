package com.fittrack.Model.PhotoGallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 8/2/2017.
 */
public class DataPhoto {
    @SerializedName("user_photo_id")
    @Expose
    private String userPhotoId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;

    public String getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(String userPhotoId) {
        this.userPhotoId = userPhotoId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

}
