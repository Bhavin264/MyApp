package com.fittrack.Model.CompareClg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 8/11/2017.
 */
public class DataCompareList {

    @SerializedName("user_compare_photo_id")
    @Expose
    private String userComparePhotoId;
    @SerializedName("before_date")
    @Expose
    private String beforeDate;
    @SerializedName("before_weight")
    @Expose
    private String beforeWeight;
    @SerializedName("user_before_photo")
    @Expose
    private String userBeforePhoto;
    @SerializedName("after_date")
    @Expose
    private String afterDate;
    @SerializedName("after_weight")
    @Expose
    private String afterWeight;
    @SerializedName("user_after_photo")
    @Expose
    private String userAfterPhoto;

    public String getUserComparePhotoId() {
        return userComparePhotoId;
    }

    public void setUserComparePhotoId(String userComparePhotoId) {
        this.userComparePhotoId = userComparePhotoId;
    }

    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }

    public String getBeforeWeight() {
        return beforeWeight;
    }

    public void setBeforeWeight(String beforeWeight) {
        this.beforeWeight = beforeWeight;
    }

    public String getUserBeforePhoto() {
        return userBeforePhoto;
    }

    public void setUserBeforePhoto(String userBeforePhoto) {
        this.userBeforePhoto = userBeforePhoto;
    }

    public String getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(String afterDate) {
        this.afterDate = afterDate;
    }

    public String getAfterWeight() {
        return afterWeight;
    }

    public void setAfterWeight(String afterWeight) {
        this.afterWeight = afterWeight;
    }

    public String getUserAfterPhoto() {
        return userAfterPhoto;
    }

    public void setUserAfterPhoto(String userAfterPhoto) {
        this.userAfterPhoto = userAfterPhoto;
    }
}
