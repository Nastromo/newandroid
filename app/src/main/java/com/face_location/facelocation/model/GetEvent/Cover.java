package com.face_location.facelocation.model.GetEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cover {

    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("location_mob")
    @Expose
    private String locationMob;
    @SerializedName("location")
    @Expose
    private String location;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLocationMob() {
        return locationMob;
    }

    public void setLocationMob(String locationMob) {
        this.locationMob = locationMob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}