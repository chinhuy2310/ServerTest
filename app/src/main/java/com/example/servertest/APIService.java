package com.example.servertest;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.example.servertest.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
