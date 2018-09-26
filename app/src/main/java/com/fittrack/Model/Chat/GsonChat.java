package com.fittrack.Model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 9/9/2017.
 */
public class GsonChat {
    @SerializedName("next_offset")
    @Expose
    private Integer nextOffset;
    @SerializedName("flag")
    @Expose
    private Integer flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<DataChat> data = null;

    public Integer getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(Integer nextOffset) {
        this.nextOffset = nextOffset;
    }

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

    public List<DataChat> getData() {
        return data;
    }

    public void setData(List<DataChat> data) {
        this.data = data;
    }

}
