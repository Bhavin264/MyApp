package com.fittrack.Model.FoodCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/9/2017.
 */
public class DataDietFood {

    @SerializedName("food_id")
    @Expose
    private String foodId;
    @SerializedName("food_title")
    @Expose
    private String foodTitle;
    @SerializedName("serving_size")
    @Expose
    private String servingSize;
    @SerializedName("calories")
    @Expose
    private String calories;
    @SerializedName("total_fat")
    @Expose
    private String totalFat;
    @SerializedName("total_carbohydrate")
    @Expose
    private String totalCarbohydrate;
    @SerializedName("protein")
    @Expose
    private String protein;
    @SerializedName("food_image")
    @Expose
    private String foodImage;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(String totalFat) {
        this.totalFat = totalFat;
    }

    public String getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(String totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
