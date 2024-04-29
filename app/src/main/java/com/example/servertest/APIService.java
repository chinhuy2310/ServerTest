package com.example.servertest;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @POST("login")
    Call<User> login(@Body LoginRequest loginRequest);
    @POST("signup")
    Call<Void> signup(@Body SignupRequest signupRequest);
    @GET("popularposts")
    Call<List<Post>> getPopularPosts();

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
