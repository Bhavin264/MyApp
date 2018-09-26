package com.fittrack.Model.FoodAvoid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/14/2017.
 */
public class DataFoodEat {

    @SerializedName("eat")
    @Expose
    private String eat;

    public String getEat() {
        return eat;
    }

    public void setEat(String eat) {
        this.eat = eat;
    }
}
