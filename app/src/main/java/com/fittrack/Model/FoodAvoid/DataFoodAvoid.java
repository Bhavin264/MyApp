package com.fittrack.Model.FoodAvoid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 9/9/2017.
 */
public class DataFoodAvoid {

    @SerializedName("food_avoid_id")
    @Expose
    private String foodAvoidId;
    @SerializedName("food_title")
    @Expose
    private String foodTitle;
    @SerializedName("food_strictly_text")
    @Expose
    private List<DataFoodStrictly> foodStrictlyText = null;
    @SerializedName("food_eat_text")
    @Expose
    private List<DataFoodEat> DataFoodEat = null;

    public String getFoodAvoidId() {
        return foodAvoidId;
    }

    public void setFoodAvoidId(String foodAvoidId) {
        this.foodAvoidId = foodAvoidId;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public List<DataFoodStrictly> getFoodStrictlyText() {
        return foodStrictlyText;
    }

    public void setFoodStrictlyText(List<DataFoodStrictly> foodStrictlyText) {
        this.foodStrictlyText = foodStrictlyText;
    }

    public List<DataFoodEat> getDataFoodEat() {
        return DataFoodEat;
    }

    public void setDataFoodEat(List<DataFoodEat> DataFoodEat) {
        this.DataFoodEat = DataFoodEat;
    }


}
