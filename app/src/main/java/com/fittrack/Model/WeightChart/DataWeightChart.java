package com.fittrack.Model.WeightChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 8/8/2017.
 */
public class DataWeightChart {

    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("weight")
    @Expose
    private String weight;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
