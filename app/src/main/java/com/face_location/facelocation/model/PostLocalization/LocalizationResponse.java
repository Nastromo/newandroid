package com.face_location.facelocation.model.PostLocalization;

import com.face_location.facelocation.model.GetEvent.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalizationResponse {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("status")
    @Expose
    private int status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
