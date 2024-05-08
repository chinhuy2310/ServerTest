package com.example.servertest.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public UserResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
