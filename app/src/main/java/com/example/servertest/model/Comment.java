package com.example.servertest.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String avatar_image;
    private String username;
    private String comment_content;
    private String created_at;
    private int comment_id ;
    private int user_id;
    private int post_id;
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

    public String getContent() {
        return comment_content;
    }

    public String getTime(){return created_at;}

    public int getComment_id(){return comment_id;}
    public int getUser_id(){return user_id;}

    public int getPost_id() {
        return post_id;
    }
}
