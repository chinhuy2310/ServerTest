package com.example.servertest.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Post implements Serializable {
    private int userId;
    private int postId;
    private String avatar_image;
    private String username;
    private String date;
    private String post_title;
    private String post_content;
    private int isRecipe;
    private List<Comment> comments;
    private int isLiked;
    private int likeCount;
    private int commentCount;
    private int postGroupId;
    private String imageUrls;
    private List<String> imageUrlList;
    public Post(int userId, int postGroupId, int isRecipe, String post_title, String post_content,List<String> imageUrlList) {
        this.userId = userId;
        this.postGroupId = postGroupId;
        this.isRecipe = isRecipe;
        this.post_title = post_title;
        this.post_content = post_content;
//        this.imageUrlList = Arrays.asList(imageUrls.split(","));
        this.imageUrlList = imageUrlList;
    }
    public int getPostId() {return postId;}

    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
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
    public List<String> getImageUrlList(){return imageUrlList;}

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
