package com.example.servertest.model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private int user_id;
    private int post_id;
    private String avatar_image;
    private String username;
    private String date;
    private String post_title;
    private String content;
    private String imageUrls;
    private int isRecipe;
    private List<Comment> comments;
    private int isLiked;

    public Post(int user_id,int post_id,String avatar_image, String username, String date, String post_title, String content, String imageUrls, int isRecipe,List<Comment> comments) {
        this.user_id = user_id;
        this.avatar_image = avatar_image;
        this.username = username;
        this.date = date;
        this.post_title = post_title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.isRecipe = isRecipe;
        this.comments = comments;
        this.post_id = post_id;
        this.isLiked = isLiked;
    }

    public int getPostId() {return post_id;}

    public int getId() {
        return user_id;
    }

    public void setId(int user_id) {
        this.user_id = user_id;
    }

    public String getAvatarUrl() {
        return avatar_image;
    }



    public String getUsername() {
        return username;
    }



    public String getDate() {
        return date;
    }


    public String getTitle() {
        return post_title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrls() {
        return imageUrls;
    }


    public int getIsRecipe() {
        return isRecipe;
    }


    public List<Comment> getComments() {
        return comments;
    }


    public int isLiked() {
        return isLiked;
    }

    public void setLiked(int liked) {isLiked = isLiked;}


}
