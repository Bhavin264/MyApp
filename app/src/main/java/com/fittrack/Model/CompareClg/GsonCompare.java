package com.fittrack.Model.CompareClg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 8/2/2017.
 */
public class GsonCompare {

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
    private List<DataCompare> data = null;

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

    public List<DataCompare> getData() {
        return data;
    }

    public void setData(List<DataCompare> data) {
        this.data = data;
    }
}
