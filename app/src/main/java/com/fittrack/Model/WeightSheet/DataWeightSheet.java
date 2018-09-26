package com.fittrack.Model.WeightSheet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 7/28/2017.
 */
public class DataWeightSheet {

    @SerializedName("weight_id")
    @Expose
    private String weightId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("weight")
    @Expose
    private String weight;

    public String getWeightId() {
        return weightId;
    }

    public void setWeightId(String weightId) {
        this.weightId = weightId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
