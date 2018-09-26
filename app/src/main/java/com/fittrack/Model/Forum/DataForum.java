package com.fittrack.Model.Forum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Umesh on 8/2/2017.
 */
public class DataForum {

    @SerializedName("forum_id")
    @Expose
    private String forumId;
    @SerializedName("forum_topic")
    @Expose
    private String forumTopic;
    @SerializedName("forum_title")
    @Expose
    private String forumTitle;

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getForumTopic() {
        return forumTopic;
    }

    public void setForumTopic(String forumTopic) {
        this.forumTopic = forumTopic;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }
}
