package com.fittrack.Model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 9/9/2017.
 */
public class DataChat {

    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("time_compare")
    @Expose
    private String timeCompare;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("send_from")
    @Expose
    private String sendFrom;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

}
