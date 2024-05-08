package com.example.servertest.model;

import java.io.Serializable;

public class User implements Serializable {
    private int user_id;
    private String username;
    private String useraccname;
    private String email;
    private String avatar_image;
    private String cover_image;
    private int is_admin;


    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", useraccname='" + useraccname + '\'' +
                ", email='" + email + '\'' +
                ", avatar_image='" + avatar_image + '\'' +
                ", cover_image='" + cover_image + '\'' +
                ", is_admin=" + is_admin +
                '}';
    }
    public User(int user_id, String username, String useraccname, String email, String avatar_image, String cover_image, int is_admin) {
        this.user_id = user_id;
        this.username = username;
        this.useraccname = useraccname;
        this.email = email;
        this.avatar_image = avatar_image;
        this.cover_image = cover_image;
        this.is_admin = is_admin;
    }


    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getUseraccname() {
        return useraccname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarImage() {
        return avatar_image;
    }

    public String getCoverImage() {
        return cover_image;
    }

    public int getIsAdmin() {
        return is_admin;
    }
}
