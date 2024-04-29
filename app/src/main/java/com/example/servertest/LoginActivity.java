package com.example.servertest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.servertest.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView signUpTextView, findPassword, buttonLogin;
    EditText editTextUserAccname, editTextPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        editTextUserAccname = findViewById(R.id.taikhoan);
        editTextPassword = findViewById(R.id.matkhau);
        buttonLogin = findViewById(R.id.loginbutton);
        signUpTextView = findViewById(R.id.signUpTextView);
        findPassword = findViewById(R.id.findPasswordText);

        // Set up Retrofit
        final APIService apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

        // Handle login button click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useraccname = editTextUserAccname.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                // Check if username and password are not empty
                if (!useraccname.isEmpty() && !password.isEmpty()) {
                    // Call login API
                    LoginRequest loginRequest = new LoginRequest(useraccname, password);
                    Call<User> call = apiService.login(loginRequest);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                // Login successful
                                User user = response.body();
                                if (user != null) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.e("LoginActivity", "User data is null");
                                    Toast.makeText(LoginActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Login failed
                                Log.e("LoginActivity", "Response code: " + response.code());

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            // Handle network errors
                            Toast.makeText(LoginActivity.this, "Login failed: Cannot connect to server" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Empty username or password
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        // Handle action when pressing Enter key on keyboard
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Perform login action
                    buttonLogin.performClick();
                    return true;
                }
                return false;
            }
        });

        // Redirect to sign up screen when clicking on sign up text



        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
