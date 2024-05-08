package com.example.servertest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servertest.APIService;
import com.example.servertest.R;

import com.example.servertest.RetrofitClientInstance;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class adapterPostMenu2 extends RecyclerView.Adapter<adapterPostMenu2.ViewHolder> {
    private List<Post> posts;
    private Context context;
    private OnPostClickListener onPostClickListener;
    private User user;
    private APIService apiService;

    public interface OnPostClickListener {
        void onPostClick(int position);
        void onCommentClick(int position);

    }

    public adapterPostMenu2(Context context, List<Post> posts, OnPostClickListener listener, User user) {
        this.context = context;
        this.posts = posts;
        this.onPostClickListener = listener;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Post post = posts.get(position);
        holder.textViewUsername.setText(post.getUsername());
        holder.textViewDateTime.setText(post.getDate());
        holder.textViewTitle.setText(post.getTitle());

//        boolean isLiked = databaseHelper.isPostLikedByUser(post.getPostId(), user.getUserId());
//        holder.buttonLike.setImageResource(isLiked ? R.drawable.ic_liked : R.drawable.like);

        holder.txtRecipe.setVisibility(post.getIsRecipe() == 1 ? View.VISIBLE : View.GONE);
//        int likeCount = databaseHelper.getLikeCountForPost(post.getPostId());
//        int commentCount = databaseHelper.getCommentCountForPost(post.getPostId());
//        holder.likeCountTextView.setText(String.valueOf(likeCount));
//        holder.commentCountTextView.setText(String.valueOf(commentCount));

//        Picasso.get().load(post.getAvatarUrl()).into(holder.imageViewAvatar);
        if (!post.getAvatarUrl().isEmpty()) {
            Picasso.get().load(post.getAvatarUrl()).into(holder.imageViewAvatar);
        } else {
//             Nếu avatarUrl là null, hiển thị ảnh từ resource drawable
            Picasso.get().load(R.drawable.user_icon2).into(holder.imageViewAvatar);
        }
        //-------------------------------------------------------------
        // hiển thị ảnh đầu tiên trong danh sách ảnh của bài viết
        String firstImageUrl = null;
        if (post.getImageUrls() instanceof String) {
            // Nếu là một chuỗi, xử lý chuỗi để lấy URL ảnh
            String imageUrlsString = (String) post.getImageUrls();
            // Ví dụ: phân tách chuỗi để lấy các URL ảnh
            String[] imageUrlArray = imageUrlsString.split(",");
            if (imageUrlArray.length > 0) {
                firstImageUrl = imageUrlArray[0];
            }
        }
        if (firstImageUrl != null && !firstImageUrl.isEmpty()) {
            Picasso.get().load(firstImageUrl).into(holder.imageViewPost);
            holder.imageViewPost.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewPost.setVisibility(View.GONE);
        }
        //-----------------------------------------------------

        int currentPosition = position;

        // Gán sự kiện onClickListener cho itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    onPostClickListener.onPostClick(currentPosition);    // Gọi phương thức trong interface
                }
            }
        });

        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPostClickListener.onCommentClick(position);
            }
        });



        //---------------------------------------------------------------------------------
        //hiển thị số lượng ike và comment
//        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

//        apiService.getCommentCountForPost(post.getPostId()).enqueue(new Callback<Integer>() {
//            @Override
//            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                int commentCount = response.body();
//                holder.commentCountTextView.setText(String.valueOf(commentCount));
//            }
//            @Override
//            public void onFailure(Call<Integer> call, Throwable t) {
//                // Handle error
//            }
//        });
//        apiService.getLikeCountForPost(post.getPostId()).enqueue(new Callback<Integer>() {
//            @Override
//            public void onResponse(Call<Integer> call, Response<Integer> response) {
//                int likeCount = response.body();
//                holder.likeCountTextView.setText(String.valueOf(likeCount));
//            }
//
//            @Override
//            public void onFailure(Call<Integer> call, Throwable t) {
//                // Handle error
//            }
//        });
        //------------------------------------------------------------------------------------


//        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isLiked = post.isLiked();
//                int likeCount = databaseHelper.getLikeCountForPost(post.getPostId());
//                if (isLiked) {
//                    databaseHelper.toggleLike(user.getUserId(), post.getPostId(), false);
//                   likeCount--;
//                }else{
//                    databaseHelper.toggleLike(user.getUserId(), post.getPostId(), true);
//                    likeCount++;
//                }
//                post.setLiked(!isLiked);
//                holder.buttonLike.setImageResource(!isLiked ? R.drawable.ic_liked : R.drawable.like);
//                holder.likeCountTextView.setText(String.valueOf(likeCount));
//            }
//        });


//        holder.postOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Tạo PopupMenu
//                PopupMenu popupMenu = new PopupMenu(context, holder.postOptions);
//                popupMenu.inflate(R.menu.post_options_menu); // Sử dụng menu resource đã tạo
//
//                // Xử lý sự kiện khi một item trong PopupMenu được chọn
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.menu_report:
//                                // Xử lý khi chọn Report
//                                // Ví dụ: Hiển thị thông báo "Reported"
//                                Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
//                                return true;
//                            case R.id.menu_delete:
//
////                                posts.remove(position);
////                                notifyItemRemoved(position);
//                                if(post.getId()== user.getUserId()){
//                                    Log.e("post userid", String.valueOf(post.getId()));
//                                    Log.e(" userid", String.valueOf(user.getUserId()));
//
//                                    databaseHelper.deletePost(post.getPostId());
//                                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
//                                    retrievePostsFromDatabase();
//                                }else if(user.getIsAdmin()==1){  //If you're an admin, you can delete any post
//                                    databaseHelper.deletePost(post.getPostId());
//                                    retrievePostsFromDatabase();
//                                }
//                                else{
//                                    Toast.makeText(context, "You do not have permission to delete this post", Toast.LENGTH_SHORT).show();
//                            }
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//
//                // Hiển thị PopupMenu
//                popupMenu.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton buttonLike,buttonComment,buttonShare;
        TextView textViewUsername, textViewDateTime, textViewTitle, txtRecipe,likeCountTextView,commentCountTextView;
        ImageView imageViewPost, postOptions, imageViewAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            txtRecipe = itemView.findViewById(R.id.txtRecipe);
            postOptions = itemView.findViewById(R.id.postoptions);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            buttonComment = itemView.findViewById(R.id.buttonComment);
        }
    }

//    private void retrievePostsFromDatabase() {
//        posts.clear();
//        posts.addAll(databaseHelper.getAllPosts());
//        notifyDataSetChanged();
//    }

}
