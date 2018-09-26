package com.fittrack.Model.CompareClg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 8/11/2017.
 */
public class GsonCompareList {
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
    private List<DataCompareList> data = null;

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

    public List<DataCompareList> getData() {
        return data;
    }

    public void setData(List<DataCompareList> data) {
        this.data = data;
    }
}
