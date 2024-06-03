package com.example.servertest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.servertest.R;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterPostMenu2 extends RecyclerView.Adapter<adapterPostMenu2.ViewHolder> {
    private List<Post> posts;
    private Context context;
    private OnPostClickListener onPostClickListener;
    private User user;

    // 포스트 클릭 이벤트를 처리하는 인터페이스
    public interface OnPostClickListener {
        void onPostClick(int position);
        void onCommentClick(int position);
        void likePost(int userId, int postId);
        void onDeletePost(int postId, int position);
    }

    // 생성자: 컨텍스트, 포스트 목록, 클릭 리스너 및 사용자 초기화
    public adapterPostMenu2(Context context, List<Post> posts, OnPostClickListener listener, User user) {
        this.context = context;
        this.posts = posts;
        this.onPostClickListener = listener;
        this.user = user;
    }

    // 아이템 레이아웃을 인플레이트하고 ViewHolder를 반환
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new ViewHolder(view);
    }

    // ViewHolder에 데이터를 바인딩
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);

        // 포스트 정보 설정
        holder.textViewUsername.setText(post.getUsername());
        holder.textViewDateTime.setText(post.getDate());
        holder.textViewTitle.setText(post.getTitle());
        holder.buttonLike.setTag(post.getPostId());

        int isLiked = post.getIsLiked();
        int isRecipe = post.getIsRecipe();
        holder.buttonLike.setImageResource(isLiked == 1 ? R.drawable.ic_liked : R.drawable.like);

        // 좋아요 버튼 클릭 이벤트 처리
        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    int userId = user.getUserId();
                    int postId = post.getPostId();
                    onPostClickListener.likePost(userId, postId);
                    notifyDataSetChanged();
                }
            }
        });

        // 포스트 옵션 클릭 이벤트 처리
        holder.postOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.postOptions);
                popupMenu.inflate(R.menu.post_options_menu);

                // PopupMenu 아이템 클릭 이벤트 처리
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_report:
                                Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_delete:
                                if (post.getId() == user.getUserId() || user.getIsAdmin() == 1) {
                                    onPostClickListener.onDeletePost(post.getPostId(), position);
                                } else {
                                    Toast.makeText(context, "You do not have permission to delete this post", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // PopupMenu 표시
                popupMenu.show();
            }
        });

        // 레시피 텍스트뷰 가시성 설정
        holder.txtRecipe.setVisibility(isRecipe == 1 ? View.VISIBLE : View.GONE);

        // 좋아요 및 댓글 수 설정
        int likeCount = post.getLikeCount();
        int commentCount = post.getCommentCount();
        holder.likeCountTextView.setText(String.valueOf(likeCount));
        holder.commentCountTextView.setText(String.valueOf(commentCount));

        // 아바타 이미지 설정
        if (!post.getAvatarUrl().isEmpty()) {
            Picasso.get().load(post.getAvatarUrl()).into(holder.imageViewAvatar);
        } else {
            Picasso.get().load(R.drawable.user_icon2).into(holder.imageViewAvatar);
        }

        // 첫 번째 이미지 URL 추출 및 설정
        String firstImageUrl = null;
        if (post.getImageUrls() instanceof String) {
            String imageUrlsString = (String) post.getImageUrls();
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

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    onPostClickListener.onPostClick(position);
                }
            }
        });

        // 댓글 버튼 클릭 이벤트 처리
        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPostClickListener.onCommentClick(position);
            }
        });
    }

    // 데이터 세트의 총 항목 수 반환
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // ViewHolder 클래스: 뷰를 보유하고 재활용
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton buttonLike, buttonComment, buttonShare;
        TextView textViewUsername, textViewDateTime, textViewTitle, txtRecipe, likeCountTextView, commentCountTextView;
        ImageView imageViewPost, postOptions, imageViewAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            txtRecipe = itemView.findViewById(R.id.txtRecipe);
            postOptions = itemView.findViewById(R.id.options);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            buttonComment = itemView.findViewById(R.id.buttonComment);
        }
    }
}
