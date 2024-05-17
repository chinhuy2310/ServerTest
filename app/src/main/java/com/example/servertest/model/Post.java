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
    private String post_content;
    private String imageUrls;
    private int isRecipe;
    private List<Comment> comments;
    private int isLiked;
    private int likeCount;
    private int commentCount;

//    public Post(int user_id,int post_id,String avatar_image, String username, String date, String post_title, String content, String imageUrls, int isRecipe,List<Comment> comments) {
//        this.user_id = user_id;
//        this.avatar_image = avatar_image;
//        this.username = username;
//        this.date = date;
//        this.post_title = post_title;
//        this.content = content;
//        this.imageUrls = imageUrls;
//        this.isRecipe = isRecipe;
//        this.comments = comments;
//        this.post_id = post_id;
//    }

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
        return post_content;
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


    public int getIsLiked() {
        return isLiked;
    }

    public int getLikeCount() {
        return likeCount;
    }


    public int getCommentCount() {
        return commentCount;
    }

    public void setLikeCount(int i) {
    }

    public void setIsLiked(int i) {
    }
}
