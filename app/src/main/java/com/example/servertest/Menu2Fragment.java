package com.example.servertest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servertest.adapter.adapterPostMenu2;

import com.example.servertest.model.CircleTransform;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menu2Fragment extends Fragment implements adapterPostMenu2.OnPostClickListener {
    private RecyclerView recyclerView;
    private adapterPostMenu2 adapter;
    private List<Post> postList;
    private User user;
    private APIService apiService;
//    private InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu2fragment, container, false);
        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

//        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            ImageView imgviewMAvt = view.findViewById(R.id.imageViewMyAvt);
            if (user != null && user.getAvatarImage() != null && !user.getAvatarImage().isEmpty()) {
                Picasso.get().load(user.getAvatarImage())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.custom_border2)
                        .error(R.drawable.custom_border2)
                        .into(imgviewMAvt);
            } else {
                // Nếu không có đường dẫn ảnh hoặc đường dẫn rỗng, hiển thị ảnh mặc định từ thư mục drawable
                imgviewMAvt.setImageResource(R.drawable.user_icon2);
            }
        }
        RelativeLayout topRelativeLayout = view.findViewById(R.id.topRelativeLayout);
        topRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                User user = (User) bundle.getSerializable("user");
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList = new ArrayList<>();

        adapter = new adapterPostMenu2(getActivity(), postList, this, user); // Gán listener
        recyclerView.setAdapter(adapter);
        retrievePostsFromServer();

        return view;
    }
    public void onResume() {
        super.onResume();
        retrievePostsFromServer();
//        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
    @Override
    public void onDeletePost(int postId, int position) {
        deletePost(postId, position);
    }

    private void retrievePostsFromServer() {
        int userId = (user != null) ? user.getUserId() : -1; // Thay đổi userId tùy theo cách bạn lấy userId
        Call<List<Post>> call = apiService.getAllPost(userId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    updateIsLiked(posts);
                    postList.clear();
                    postList.addAll(posts);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Failed to retrieve posts: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("API Error", "Failed to retrieve posts: " + t.getMessage());
            }
        });
    }


    // Phương thức trong interface OnPostClickListener
    @Override
    public void onPostClick(int position) {
        // Lấy đối tượng Post tại vị trí được nhấn
        Post clickedPost = postList.get(position);
        int postId = clickedPost.getPostId();
        // Gửi Intent đến PostDetail Activity và đính kèm đối tượng Post
        Intent intent = new Intent(getActivity(), PostDetail.class);
        intent.putExtra("POST_DETAIL", clickedPost);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    private void updateIsLiked(List<Post> serverPosts) {
        for (Post serverPost : serverPosts) {
            for (Post localPost : postList) {
                if (serverPost.getPostId() == localPost.getPostId()) {
                    localPost.setIsLiked(serverPost.getIsLiked());
                    break;
                }
            }
        }
    }

    public void onCommentClick(int position) {
        // Lấy đối tượng Post tại vị trí được nhấn
        Post clickedPost = postList.get(position);
        int postId = clickedPost.getPostId();
        // Gửi Intent đến PostDetail Activity và đính kèm đối tượng Post cũng như flag để chỉ định cuộn đến phần comment
        Intent intent = new Intent(getActivity(), PostDetail.class);
        intent.putExtra("POST_DETAIL", clickedPost);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("SCROLL_TO_COMMENT", true); // Gửi flag để báo cho PostDetail cuộn đến phần comment
        intent.putExtra("user", user);
        startActivity(intent);
    }


    public void likePost(int userId, int postId)  {
        if (userId != -1) {
            LikeRequest likeRequest = new LikeRequest(userId, postId);
            Call<Void> call = apiService.likePost(likeRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
//                        for (int i = 0; i < postList.size(); i++) {
//                            Post post = postList.get(i);
//                            if (post.getPostId() == postId) {
//                                post.setIsLiked(post.getIsLiked() == 1 ? 0 : 1);
//                                post.setLikeCount(post.getIsLiked() == 1 ? post.getLikeCount() + 1 : post.getLikeCount() - 1);
//                                if(post.getIsLiked()==1){
//                                    post.setIsLiked(0);
//                                    post.setLikeCount(post.getLikeCount()-1);
//                                }else {
//                                    post.setIsLiked(1);
//                                    post.setLikeCount(post.getLikeCount()+1);
//                                }
//                                adapter.notifyItemChanged(i);
//                                retrievePostsFromServer();
//                                break;
//                            }
//                        }

                        // Notify the adapter of the data set change
                        retrievePostsFromServer();

                        adapter.notifyDataSetChanged();
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


    private void deletePost(int postId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Người dùng đã nhấn OK, tiến hành xóa bài đăng
                    performDeletePost(postId, position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Người dùng đã nhấn Cancel, không thực hiện xóa bài đăng
                    dialog.dismiss(); // Đóng dialog
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void performDeletePost(int postId, int position) {
        Call<Void> call = apiService.deletePost(postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Post deleted", Toast.LENGTH_SHORT).show();
                    postList.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    if (response.code() == 404) {
                        Toast.makeText(getActivity(), "Post "+ postId+" not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to delete post", Toast.LENGTH_SHORT).show();
                        Log.e("response","err: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
