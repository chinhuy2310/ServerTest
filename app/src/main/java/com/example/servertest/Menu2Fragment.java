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

    private void retrievePostsFromServer() {
        Call<List<Post>> call = apiService.getAllPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    // Nếu yêu cầu thành công, cập nhật danh sách bài đăng và thông báo Adapter
                    List<Post> posts = response.body();
                    postList.clear();
                    postList.addAll(posts);
                    adapter.notifyDataSetChanged();
                } else {
                    // Xử lý khi yêu cầu không thành công
                    Log.e("API Error", "Failed to retrieve posts: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Xử lý khi yêu cầu gặp lỗi
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
//    public void likePost(int userId, int postId) {
//        if (userId != -1) {
//            LikeRequest likeRequest = new LikeRequest(userId, postId);
//            Call<Void> call = apiService.likePost(likeRequest);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        for (int i = 0; i < postList.size(); i++) {
//                            Post post = postList.get(i);
//                            if (post.getPostId() == postId) {
//                                // Đảo trạng thái like
//                                int currentLikeState = post.getIsLiked();
//                                post.setIsLiked(currentLikeState == 1 ? 0 : 1);
//                                // Tăng hoặc giảm số lượng like tùy thuộc vào trạng thái like
//                                post.setLikeCount(currentLikeState == 1 ? post.getLikeCount() - 1 : post.getLikeCount() + 1);
//                                // Thông báo cho Adapter biết rằng chỉ có item i được thay đổi
//                                adapter.notifyItemChanged(i);
//                                break; // Thoát khỏi vòng lặp sau khi tìm thấy bài đăng cần cập nhật
//                            }
//                        }
//                        Log.e("like","ok");
//                    } else {
//                        // Xử lý lỗi
//                        Log.e("like","response : " + response.message());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    // Xử lý lỗi kết nối
//                }
//            });
//
//            // Ngay sau khi gửi yêu cầu like, cập nhật trạng thái nút like mà không chờ phản hồi từ server
//            for (int i = 0; i < postList.size(); i++) {
//                Post post = postList.get(i);
//                if (post.getPostId() == postId) {
//                    // Đảo trạng thái like
//                    int currentLikeState = post.getIsLiked();
//                    post.setIsLiked(currentLikeState == 1 ? 0 : 1);
//                    // Tăng hoặc giảm số lượng like tùy thuộc vào trạng thái like
//                    post.setLikeCount(currentLikeState == 1 ? post.getLikeCount() - 1 : post.getLikeCount() + 1);
//                    // Thông báo cho Adapter biết rằng chỉ có item i được thay đổi
//                    adapter.notifyItemChanged(i);
//                    break; // Thoát khỏi vòng lặp sau khi tìm thấy bài đăng cần cập nhật
//                }
//            }
//        } else {
//            // Xử lý trường hợp không tìm thấy userId
//        }
//    }



}
