package com.face_location.facelocation.model.MyProfile;

/**
 * Created by admin on 04.12.17.
 */

public class ProfileBody {

    String email, username, lastname, phone, job;

    public ProfileBody(String email, String username, String lastname, String phone, String job) {
        this.email = email;
        this.username = username;
        this.lastname = lastname;
        this.phone = phone;
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
