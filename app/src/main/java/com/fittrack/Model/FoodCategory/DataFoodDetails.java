package com.fittrack.Model.FoodCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/9/2017.
 */
public class DataFoodDetails {

    @SerializedName("food_id")
    @Expose
    private String foodId;
    @SerializedName("food_title")
    @Expose
    private String foodTitle;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("service_size")
    @Expose
    private String serviceSize;
    @SerializedName("calories")
    @Expose
    private String calories;
    @SerializedName("total_fat")
    @Expose
    private String totalFat;
    @SerializedName("saturated_fat")
    @Expose
    private String saturatedFat;
    @SerializedName("polyunsaturated_fat")
    @Expose
    private String polyunsaturatedFat;
    @SerializedName("monounsaturated_fat")
    @Expose
    private String monounsaturatedFat;
    @SerializedName("trans_fat")
    @Expose
    private String transFat;
    @SerializedName("cholesterol")
    @Expose
    private String cholesterol;
    @SerializedName("sodium")
    @Expose
    private String sodium;
    @SerializedName("potassium")
    @Expose
    private String potassium;
    @SerializedName("total_carbohydrate")
    @Expose
    private String totalCarbohydrate;
    @SerializedName("dietary_fiber")
    @Expose
    private String dietaryFiber;
    @SerializedName("sugar")
    @Expose
    private String sugar;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getServiceSize() {
        return serviceSize;
    }

    public void setServiceSize(String serviceSize) {
        this.serviceSize = serviceSize;
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

    public String getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(String saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public String getPolyunsaturatedFat() {
        return polyunsaturatedFat;
    }

    public void setPolyunsaturatedFat(String polyunsaturatedFat) {
        this.polyunsaturatedFat = polyunsaturatedFat;
    }

    public String getMonounsaturatedFat() {
        return monounsaturatedFat;
    }

    public void setMonounsaturatedFat(String monounsaturatedFat) {
        this.monounsaturatedFat = monounsaturatedFat;
    }

    public String getTransFat() {
        return transFat;
    }

    public void setTransFat(String transFat) {
        this.transFat = transFat;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getPotassium() {
        return potassium;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }

    public String getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(String totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public String getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(String dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
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
