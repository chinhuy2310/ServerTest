package com.example.servertest;
import com.example.servertest.model.Comment;
import com.example.servertest.model.ImageResponse;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.example.servertest.model.UserResponse;



import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface APIService {
    @POST("login")
    Call<User> login(@Body LoginRequest loginRequest);

    @GET("user/{useraccname}")
    Call<UserResponse> getUser(@Path("useraccname") String useraccname);

    @POST("signup")
    Call<Void> signup(@Body SignupRequest signupRequest);
    @GET("popularposts")
    Call<List<Post>> getPopularPosts();
    @GET("allposts")
    Call<List<Post>> getAllPost();
    @POST("likepost")
    Call<Void> likePost(@Body LikeRequest likeRequest);


    @GET("post/{post_id}")
    Call<Post> getPost(@Path("post_id") int postId);

    @GET("/comments/{post_id}")
    Call<List<Comment>> getComments(@Path("post_id") int postId);

    @POST("/addcomment")
    Call<Void> addComment(@Body CommentData commentData);


    @POST("upload")
    @Multipart
    Call<ImageResponse> uploadImages(@Part List<MultipartBody.Part> images);

    @FormUrlEncoded
    @POST("addpost") // Thay thế "addpost" bằng endpoint tương ứng trên server của bạn
    Call<Void> addPost(
            @Field("userId") int userId,
            @Field("postGroupId") int postGroupId,
            @Field("isRecipe") int isRecipe,
            @Field("post_title") String post_title,
            @Field("post_content") String post_content,
            @Field("imageList") List<String> imageList
    );
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
