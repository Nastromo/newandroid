package com.facelocation.testretrofit;


public class RegistrationBody {
    public String email;
    public String password;

    public RegistrationBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
