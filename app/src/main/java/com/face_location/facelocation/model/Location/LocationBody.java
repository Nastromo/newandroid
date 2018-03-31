package com.face_location.facelocation.model.Location;

public class LocationBody {

    String title, text, contact;
    float longitude, latitude;
    boolean published;

    public LocationBody(String title, String text, String contact, float longitude, float latitude, boolean publised) {
        this.title = title;
        this.text = text;
        this.contact = contact;
        this.longitude = longitude;
        this.latitude = latitude;
        this.published = publised;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public boolean isPublised() {
        return published;
    }

    public void setPublised(boolean publised) {
        this.published = publised;
    }
}
