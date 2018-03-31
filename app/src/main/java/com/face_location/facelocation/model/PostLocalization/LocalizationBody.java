package com.face_location.facelocation.model.PostLocalization;

/**
 * Created by admin on 04.01.18.
 */

public class LocalizationBody {
    double latitude;
    double longitude;

    public LocalizationBody(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
