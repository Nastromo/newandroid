package com.face_location.facelocation.model.Events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cover_ {

    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("location")
    @Expose
    private String location;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
