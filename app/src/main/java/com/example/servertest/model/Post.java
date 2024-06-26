package com.example.servertest.model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private int user_id;
    private int post_id;
    private String avatar_image;
    private String username;
    private String created_at;
    private String post_title;
    private String post_content;
    private String imageUrls;
    private int is_recipe;
    private List<Comment> comments;
    private int isLiked;
    private int likeCount;
    private int commentCount;



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
        return created_at;
    }


    public String getTitle() {
        return post_title;
    }

    public String getContent() {
        return post_content;
    }


    public String getImageUrls() {
        return imageUrls;
    }


    public int getIsRecipe() {
        return is_recipe;
    }


    public List<Comment> getComments() {
        return comments;
    }


    public int getIsLiked() {
        return isLiked;
    }

    public int getLikeCount() {
        return likeCount;
    }


    public int getCommentCount() {
        return commentCount;
    }


    public void setIsLiked(int i) {
    }
}