package com.face_location.facelocation.model.GetMainChat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hide_ {

    @SerializedName("email")
    @Expose
    private Boolean email;
    @SerializedName("phone")
    @Expose
    private Boolean phone;

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }

}
