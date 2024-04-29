package com.example.servertest.model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private int userid;
    private int postId;
    private String avatarUrl;
    private String username;
    private String date;
    private String title;
    private String content;
    private List<String> imageUrls;
    private int isRecipe;
    private List<Comment> comments;
    private boolean isLiked;

    public Post(int userid,int postId,String avatarUrl, String username, String date, String title, String content, List<String> imageUrls, int isRecipe,List<Comment> comments) {
        this.userid = userid;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.date = date;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.isRecipe = isRecipe;
        this.comments = comments;
        this.postId = postId;
        this.isLiked = false;
    }

    public int getPostId() {return postId;}
    public void setPostId(int postId) {this.postId =postId;}

    public int getId() {
        return userid;
    }

    public void setId(int userid) {
        this.userid = userid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getIsRecipe() {
        return isRecipe;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
