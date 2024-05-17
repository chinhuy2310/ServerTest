package com.example.servertest.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCommenterAvatar;
        TextView textViewUsername, textViewContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCommenterAvatar = itemView.findViewById(R.id.imageViewCommentAvatar);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewContent = itemView.findViewById(R.id.textViewComments);
        }
    }
}