package com.fittrack.Model.WeightChart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 8/8/2017.
 */
public class GsonWeightChart {
    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("month_name")
    @Expose
    private String monthName;
    @SerializedName("data")
    @Expose
    private List<DataWeightChart> data = null;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public List<DataWeightChart> getData() {
        return data;
    }

    public void setData(List<DataWeightChart> data) {
        this.data = data;
    }
}
