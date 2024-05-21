package com.example.servertest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.servertest.adapter.CommentAdapter;
import com.example.servertest.adapter.PostImageAdapter;
import com.example.servertest.adapter.adapterPostMenu2;
import com.example.servertest.model.Comment;

import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.squareup.picasso.Picasso;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetail extends AppCompatActivity {
    private NestedScrollView netscrollview;
    private ImageButton buttonLike,buttonComment;
    private ImageView imageViewAvatar,sendComment;
    private TextView textViewUsername, textViewDateTime, textViewTitle, textViewContent, txtRecipe,likeCountTextView,commentCountTextView;
    private RecyclerView recyclerViewImages, recyclerViewComments;
    private EditText writecomment;
    private APIService apiService;
    private User user;
    private int postId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);



        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            user = (User) intent.getSerializableExtra("user");
//            if (user != null) {
//                Log.e("userid dang nhap", "tại trang nội dung: " + user.getUserId());
//            } else {
//                Log.e("PostDetail", "User is null");
//            }
        } else {
            // Xử lý trường hợp Intent không chứa "user"
            Log.e("PostDetail", "Intent does not contain 'user' extra");
        }
        Post post = (Post) getIntent().getSerializableExtra("POST_DETAIL");
        postId = intent.getIntExtra("POST_ID", -1);
        int userId = user.getUserId();
        // Ánh xạ các thành phần UI từ layout
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewDateTime = findViewById(R.id.textViewDateTime);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewContent = findViewById(R.id.textViewContent);
        txtRecipe = findViewById(R.id.txtRecipe);
        netscrollview = findViewById(R.id.netscrollview);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        writecomment = findViewById(R.id.writeComment);
        sendComment = findViewById(R.id.sendComment);

        likeCountTextView = findViewById(R.id.likeCountTextView);
        commentCountTextView = findViewById(R.id.commentCountTextView);


        // Ánh xạ nút like
        buttonLike = findViewById(R.id.buttonLike);
//        buttonLike.setImageResource(post.getIsLiked() ==1 ? R.drawable.ic_liked : R.drawable.like);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isLiked = post.getIsLiked();
                post.setIsLiked(isLiked);
//                buttonLike.setImageResource(isLiked ==1 ? R.drawable.ic_liked : R.drawable.like);
                likePost(userId,postId);
            }
        });
        buttonComment = findViewById(R.id.buttonComment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writecomment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(writecomment, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        boolean scrollToComment = getIntent().getBooleanExtra("SCROLL_TO_COMMENT", false);
        if (scrollToComment) {
            recyclerViewComments.post(new Runnable() {
                @Override
                public void run() {
                    // Cuộn RecyclerView đến phần comment
                    scrollToComments();
                    // Hiển thị bàn phím để nhập comment
                    writecomment.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(writecomment, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }

        writecomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to implement anything here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to implement anything here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Kiểm tra nếu text đã được nhập vào EditText
                if (s.toString().trim().isEmpty()) {
                    sendComment.setVisibility(View.GONE);
                } else {

                    sendComment.setVisibility(View.VISIBLE);
                }
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức để xử lý việc gửi comment

                sendComment();
            }
        });
        //hiện nút back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // Đặt icon back tại đây
        }

        // Lấy dữ liệu bài đăng từ Intent

//        Log.e("postid:  ", String.valueOf(postId));
        // Kiểm tra xem post có null hay không trước khi hiển thị
        if (post != null) {
            // Hiển thị dữ liệu của bài đăng trên layout
            displayPostDetails(post);
            displayComments(post.getComments());
        } else {
            // Xử lý trường hợp post là null
        }
        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<List<Comment>> call = apiService.getComments(postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body();
                    // Hiển thị dữ liệu bình luận lên giao diện
                    displayComments(comments);
                } else {
                    // Xử lý khi không thành công
                    Toast.makeText(PostDetail.this, "Failed to fetch comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối hoặc lỗi không xác định
                Toast.makeText(PostDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void sendComment() {
        if (user != null) {
            String commentContent = writecomment.getText().toString().trim();
            int userId = user.getUserId();
            if (!commentContent.isEmpty()) {
                Call<Void> call = apiService.addComment(new CommentData(userId, postId, commentContent));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Xử lý response khi comment được thêm thành công
                            Toast.makeText(PostDetail.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                            writecomment.setText("");
                            hideKeyboard();
                            writecomment.clearFocus();
                            getComments();
                            getPost();
                        } else {
                            // Xử lý response khi có lỗi xảy ra
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý khi có lỗi xảy ra trong quá trình gửi request
                    }
                });
                // Thêm comment vào cơ sở dữ liệu
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Xử lý trường hợp loggedInUser là null
            Log.e("PostDetail", "loggedInUser is null");
        }
    }
    public void likePost(int userId, int postId)  {

        if (userId != -1) {
            LikeRequest likeRequest = new LikeRequest(userId, postId);
            Call<Void> call = apiService.likePost(likeRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {

                        getPost();

                        Log.e("like","ok");
                    } else {
                        // Xử lý lỗi
                        Log.e("like","response : " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Xử lý lỗi kết nối
                }
            });
        } else {
            // Xử lý trường hợp không tìm thấy userid
        }
    }
    private void scrollToComments() {
        if (recyclerViewComments != null) {
            int totalHeight = netscrollview.getChildAt(0).getHeight();
            int recyclerViewHeight = recyclerViewComments.getHeight();
            if(totalHeight >recyclerViewHeight){
                int scrollPosition = totalHeight - recyclerViewHeight ;
                netscrollview.scrollTo(0, scrollPosition);
            }
            else {
                netscrollview.scrollTo(0, recyclerViewComments.getTop());
            }
        }
    }
    private void displayPostDetails(Post post) {
        String avatarUrl = post.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Picasso.get().load(avatarUrl).into(imageViewAvatar);
        }else{
            Picasso.get().load(R.drawable.user_icon2).into(imageViewAvatar);
        }
        textViewUsername.setText(post.getUsername());
        textViewDateTime.setText(post.getDate());
        textViewTitle.setText(post.getTitle());
        textViewContent.setText(post.getContent());
        likeCountTextView.setText(String.valueOf(post.getLikeCount()));
        commentCountTextView.setText(String.valueOf(post.getCommentCount()));
        buttonLike.setImageResource(post.getIsLiked() == 1 ? R.drawable.ic_liked : R.drawable.like);
        // Hiển thị "Recipe" nếu bài đăng là công thức nấu ăn
        if (post.getIsRecipe() == 1) {
            txtRecipe.setVisibility(View.VISIBLE);
        } else {
            txtRecipe.setVisibility(View.GONE);
        }

        // Hiển thị danh sách ảnh nếu có
        List<String> imageUrlList = Arrays.asList(post.getImageUrls().split(","));
        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            recyclerViewImages.setVisibility(View.VISIBLE);
            recyclerViewImages.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewImages.setAdapter(new PostImageAdapter(this, imageUrlList));
        } else {
            recyclerViewImages.setVisibility(View.GONE);
        }
    }

    private void displayComments(List<Comment> comments) {
        // Hiển thị danh sách các comment trong RecyclerView
        if (comments != null && !comments.isEmpty()) {
            recyclerViewComments.setVisibility(View.VISIBLE);
            recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewComments.setAdapter(new CommentAdapter(this, comments));
        } else {
            recyclerViewComments.setVisibility(View.GONE);
        }
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Quay lại Fragment trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getComments() {
        Call<List<Comment>> call = apiService.getComments(postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body();
                    // Hiển thị dữ liệu bình luận lên giao diện
                    displayComments(comments);
                } else {
                    // Xử lý khi không thành công
                    Toast.makeText(PostDetail.this, "Failed to fetch comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối hoặc lỗi không xác định
                Toast.makeText(PostDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPost() {
        int userId = (user != null) ? user.getUserId() : -1;
        Call<Post> call = apiService.getPost(postId,userId);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post post = response.body();

                    displayPostDetails(post);

                } else {
                    Log.e("a", "Failed to get post detail: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("TAG", "Failed to get post detail: " + t.getMessage());
            }
        });

    }
}