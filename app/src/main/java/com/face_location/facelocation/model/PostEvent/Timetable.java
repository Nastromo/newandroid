package com.face_location.facelocation.model.PostEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timetable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
