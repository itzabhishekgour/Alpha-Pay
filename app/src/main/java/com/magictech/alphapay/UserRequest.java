package com.magictech.alphapay;

public class UserRequest {
    private String fullName;
    private String email;
    private String password;

    public UserRequest(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
    // Getters and Setters can be added here if needed
}
