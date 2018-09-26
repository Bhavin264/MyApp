package com.fittrack.Model.WeightSheet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 8/1/2017.
 */
public class GsonAddWeightSheet {
    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("data")
    @Expose
    private DataAddWeightSheet data;

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

    public DataAddWeightSheet getData() {
        return data;
    }

    public void setData(DataAddWeightSheet data) {
        this.data = data;
    }
}
