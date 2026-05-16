package com.magictech.alphapay; // Aapka package name yahan aayega

public class LoginRequest {
    private String email;
    private String password;

    // Constructor to initialize credentials
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters (Retrofit will use these to build JSON)
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