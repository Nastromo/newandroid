package com.face_location.facelocation.model.GetMainChat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}

