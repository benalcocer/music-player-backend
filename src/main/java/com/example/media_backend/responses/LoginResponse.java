package com.example.media_backend.responses;

public class LoginResponse {

    private UserResponse userResponse;

    private String token;

    private long expiresIn;

    public LoginResponse(String token, long expiresIn, UserResponse userResponse) {
        this.userResponse = userResponse;
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}