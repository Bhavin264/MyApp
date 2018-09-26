package com.fittrack.Model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/11/2017.
 */
public class GsonSendMsg {
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
    private DataSendMsg data;

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

    public DataSendMsg getData() {
        return data;
    }

    public void setData(DataSendMsg data) {
        this.data = data;
    }
}
