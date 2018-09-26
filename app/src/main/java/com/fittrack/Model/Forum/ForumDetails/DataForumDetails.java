package com.fittrack.Model.Forum.ForumDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Umesh on 8/2/2017.
 */
public class DataForumDetails {
    @SerializedName("forum_id")
    @Expose
    private String forumId;
    @SerializedName("forum_topic")
    @Expose
    private String forumTopic;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("forum_comments")
    @Expose
    private List<ForumComments> forumComments = null;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ForumComments> getForumComments() {
        return forumComments;
    }

    public void setForumComments(List<ForumComments> forumComments) {
        this.forumComments = forumComments;
    }
}
