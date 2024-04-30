package com.example.servertest.model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private int userid;
    private int postId;
    private String avatarUrl;
    private String username;
    private String date;
    private String post_title;
    private String content;
    private String imageUrls;
    private int isRecipe;
    private List<Comment> comments;
    private int isLiked;

    public Post(int userid,int postId,String avatarUrl, String username, String date, String post_title, String content, String imageUrls, int isRecipe,List<Comment> comments) {
        this.userid = userid;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.date = date;
        this.post_title = post_title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.isRecipe = isRecipe;
        this.comments = comments;
        this.postId = postId;
        this.isLiked = 0;
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

    public void setImageUrls(String imageUrls) {
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

    public int isLiked() {
        return isLiked;
    }

    public void setLiked(int liked) {isLiked = liked;}


}
