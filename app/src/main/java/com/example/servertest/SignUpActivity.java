package com.example.servertest;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText usernameEditText,accnameEditText,passwordEditText,emailEditText,confirmPasswordEditText;
    ImageView eyeIcon1, eyeIcon2;
    TextView loginTextView, signUpButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // 레이아웃에서 뷰들을 찾아와 연결합니다
        // Ánh xạ các view từ layout
        usernameEditText = findViewById(R.id.createUsername);
        accnameEditText = findViewById(R.id.createAccname);
        passwordEditText = findViewById(R.id.createPW);
        confirmPasswordEditText = findViewById(R.id.createPW2);
        emailEditText = findViewById(R.id.createEmail);
        signUpButton = findViewById(R.id.SignUpbutton);
        loginTextView = findViewById(R.id.logintext);
        eyeIcon1 = findViewById(R.id.eye_icon1);
        eyeIcon2 = findViewById(R.id.eye_icon2);

        //chuyển sang login khi nhấn text login
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo intent để chuyển từ SignUpActivity sang LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng SignUpActivity sau khi chuyển sang LoginActivity
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final APIService apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

        // Xử lý sự kiện khi nhấn vào nút Sign Up
        // 가입 버튼 클릭 시 이벤트 처리
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String useraccname = accnameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                if (username.isEmpty() || useraccname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra xác nhận mật khẩu
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra hợp lệ của dữ liệu và tạo yêu cầu đăng ký mới
                SignupRequest signupRequest = new SignupRequest(username, useraccname, password, email);

                // Gửi yêu cầu đăng ký người dùng mới đến máy chủ
                Call<Void> call = apiService.signup(signupRequest);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Đăng ký thành công
                            Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            // Chuyển đến hoạt động đăng nhập
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.code() == 400) {
                                // Nếu mã trạng thái là 400 (Bad Request), tức là trùng useraccname
                                Toast.makeText(SignUpActivity.this, "Account ID already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                // Nếu mã trạng thái khác, hiển thị thông báo mặc định
                                Toast.makeText(SignUpActivity.this, "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(SignUpActivity.this, "response.isSuccessful()) = failed : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý lỗi kết nối
                        Toast.makeText(SignUpActivity.this, "Registration failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // Xử lý sự kiện khi nhấn vào icon eye trong password
        eyeIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIcon1.setImageResource(R.drawable.ic_eye);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon1.setImageResource(R.drawable.ic_eyex);
                }
            }
        });

        // Xử lý sự kiện khi nhấn vào icon eye trong confirm password
        eyeIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIcon2.setImageResource(R.drawable.ic_eye);
                } else {
                    confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon2.setImageResource(R.drawable.ic_eyex);
                }
            }
        });
    }
}