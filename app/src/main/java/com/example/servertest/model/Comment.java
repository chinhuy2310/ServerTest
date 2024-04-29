package com.example.servertest.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String username;
    private String avatarUrl;
    private String content;

    public Comment(String avatarUrl,String username, String content) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
