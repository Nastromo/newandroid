package com.face_location.facelocation.model.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address {

    @SerializedName("position")
    @Expose
    private List<Float> position = null;
    @SerializedName("marker")
    @Expose
    private Marker marker;

    public List<Float> getPosition() {
        return position;
    }

    public void setPosition(List<Float> position) {
        this.position = position;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

}
