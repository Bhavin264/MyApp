package com.fittrack.Model.WeightSheet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 8/1/2017.
 */
public class DataWeightDate {

    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("weight_data")
    @Expose
    private List<DataWeightSheet> weightData = null;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<DataWeightSheet> getWeightData() {
        return weightData;
    }

    public void setWeightData(List<DataWeightSheet> weightData) {
        this.weightData = weightData;
    }
}
