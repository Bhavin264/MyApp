package com.fittrack.Model.FoodCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 9/9/2017.
 */
public class GsonFoodCategory {

    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<DataFoodCategory> data = null;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataFoodCategory> getData() {
        return data;
    }

    public void setData(List<DataFoodCategory> data) {
        this.data = data;
    }
}
