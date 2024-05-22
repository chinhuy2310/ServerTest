package com.example.servertest;
import com.example.servertest.model.Comment;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.example.servertest.model.UserResponse;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIService {
    @POST("login")
    Call<User> login(@Body LoginRequest loginRequest);

    @GET("user/{useraccname}")
    Call<UserResponse> getUser(@Path("useraccname") String useraccname);

    @POST("signup")
    Call<Void> signup(@Body SignupRequest signupRequest);
    @GET("popularposts")
    Call<List<Post>> getPopularPosts(@Query("user_id") int userId);
    @GET("allposts")
    Call<List<Post>> getAllPost(@Query("user_id") int userId);

    @POST("likepost")
    Call<Void> likePost(@Body LikeRequest likeRequest);

    @Multipart
    @POST("/api/posts")
    Call<Void> createPostWithImages(
            @Part("post_title") RequestBody postTitle,
            @Part("post_content") RequestBody postContent,
            @Part("isRecipe") RequestBody isRecipe,
            @Part("user_id") RequestBody userId,
            @Part("post_group") RequestBody postGroup,
            @Part List<MultipartBody.Part> images
    );

    @GET("post/{post_id}")
    Call<Post> getPost(@Query("post_id") int postId,@Query("user_id") int userId);

    @GET("/comments/{post_id}")
    Call<List<Comment>> getComments(@Path("post_id") int postId);

    @POST("/addcomment")
    Call<Void> addComment(@Body CommentData commentData);

}
class CommentData {
    private int userId;
    private int postId;
    private String commentContent;

    public CommentData(int userId, int postId, String commentContent) {
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
    }
}

class LikeRequest {
    private int userId;
    private int postId;

    public LikeRequest(int userId, int postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
class LoginRequest {
    private String useraccname;
    private String password;

    public LoginRequest(String useraccname, String password) {
        this.useraccname = useraccname;
        this.password = password;
    }
}
class SignupRequest {
    private String username;
    private String useraccname;
    private String password;
    private String email;

    public SignupRequest(String username, String useraccname, String password, String email){
        this.username = username;
        this.useraccname = useraccname;
        this.password = password;
        this.email = email;
    }
}