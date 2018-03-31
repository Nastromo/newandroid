package com.face_location.facelocation.model.GetNearestEvents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("avatar_mob")
    @Expose
    private String avatarMob;

    public String getAvatarMob() {
        return avatarMob;
    }

    public void setAvatarMob(String avatarMob) {
        this.avatarMob = avatarMob;
    }

}