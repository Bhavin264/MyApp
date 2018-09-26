package com.fittrack.Model.Profile;

import com.fittrack.Model.Register.DataRegister;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 8/1/2017.
 */
public class GsonSaveProfile {

    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DataRegister data;

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

    public DataRegister getData() {
        return data;
    }

    public void setData(DataRegister data) {
        this.data = data;
    }
}
