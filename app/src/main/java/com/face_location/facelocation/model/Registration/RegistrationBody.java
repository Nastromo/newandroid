package com.face_location.facelocation.model.Registration;

/**
 * Created by admin on 26.11.17.
 */

public class RegistrationBody {
    public String email;
    public String password;

    public RegistrationBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
