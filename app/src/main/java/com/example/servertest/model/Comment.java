package com.example.servertest.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String avatar_image;
    private String username;
    private String comment_content;

    public Comment(String avatar_image,String username, String comment_content) {
        this.avatar_image = avatar_image;
        this.username = username;
        this.comment_content = comment_content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatar_image;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatar_image = avatarUrl;
    }

    public String getContent() {
        return comment_content;
    }


}
