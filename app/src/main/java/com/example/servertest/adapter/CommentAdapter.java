package com.example.servertest.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servertest.R;
import com.example.servertest.model.CircleTransform;
import com.example.servertest.model.Comment;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment comment = commentList.get(position);
        holder.textViewUsername.setText(comment.getUsername());
        holder.textViewContent.setText(comment.getContent());
        // Set ảnh đại diện của người bình luận
        if (!comment.getAvatarUrl().isEmpty()){
            Picasso.get().load(comment.getAvatarUrl())
                    .transform(new CircleTransform())
                    .into(holder.imageViewCommenterAvatar);
        }
        else{
            Picasso.get().load(R.drawable.user_icon2).into(holder.imageViewCommenterAvatar);
        }
        holder.postOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, holder.postOptions);
                popupMenu.inflate(R.menu.post_options_menu); // Sử dụng menu resource đã tạo

                // Xử lý sự kiện khi một item trong PopupMenu được chọn
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_report:
                                // Xử lý khi chọn Report

                                Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_delete:
                                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                // Hiển thị PopupMenu
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCommenterAvatar,postOptions;
        TextView textViewUsername, textViewContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCommenterAvatar = itemView.findViewById(R.id.imageViewCommentAvatar);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewContent = itemView.findViewById(R.id.textViewComments);
            postOptions = itemView.findViewById(R.id.postoptions);
        }
    }
}