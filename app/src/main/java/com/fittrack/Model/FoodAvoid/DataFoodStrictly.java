package com.fittrack.Model.FoodAvoid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/14/2017.
 */
public class DataFoodStrictly {
    @SerializedName("strictly")
    @Expose
    private String strictly;

    public String getStrictly() {
        return strictly;
    }

    public void setStrictly(String strictly) {
        this.strictly = strictly;
    }

}
