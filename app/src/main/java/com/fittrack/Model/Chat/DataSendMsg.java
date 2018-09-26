package com.fittrack.Model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/11/2017.
 */
public class DataSendMsg {

    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("send_from")
    @Expose
    private String sendFrom;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("time_compare")
    @Expose
    private String timeCompare;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTimeCompare() {
        return timeCompare;
    }

    public void setTimeCompare(String timeCompare) {
        this.timeCompare = timeCompare;
    }
}
