package com.example.servertest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.servertest.R;
import com.example.servertest.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterPostMenu1 extends RecyclerView.Adapter<adapterPostMenu1.ViewHolder> {
    private List<Post> postList;
    private Context context;
    private OnPostClickListener onPostClickListener;

    // Interface to handle click events on posts
    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    // Constructor to initialize context, post list, and click listener
    public adapterPostMenu1(Context context, List<Post> postList, OnPostClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.onPostClickListener = listener;
    }

    // ViewHolder class to hold and recycle views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPost);
            textViewTitle = itemView.findViewById(R.id.postTitle);
        }
    }

    // Inflates the item layout and returns the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_popular_post, parent, false);
        return new ViewHolder(view);
    }

    // Binds data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = postList.get(position);

        // Extract the first image URL from the imageUrls string
        String firstImageUrl = null;
        if (post.getImageUrls() instanceof String) {
            // Kết quả nhận được từ server là một chuỗi, xử lý chuỗi để lấy URL ảnh
            String imageUrlsString = (String) post.getImageUrls();
            //  Phân tách chuỗi để lấy các URL ảnh
            String[] imageUrlArray = imageUrlsString.split(",");
            if (imageUrlArray.length > 0) {
                // Lấy ảnh đầu tiên trong danh sách
                firstImageUrl = imageUrlArray[0];
            }
        }

        if (firstImageUrl != null && !firstImageUrl.isEmpty()) {
            // Hiển thị ảnh đầu tiên
            Picasso.get().load(firstImageUrl).into(holder.imageView);
        } else {
            // Nếu không có ảnh, hiển thị ảnh mặc định
            holder.imageView.setImageResource(R.drawable.food_bg);
        }
        // Hiển thị tiêu đề
        holder.textViewTitle.setText(post.getTitle());

        // Handle click event on image
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    // thực hiện phương thức onPostClick trong menu1Fragment
                    onPostClickListener.onPostClick(position);
                }
            }
        });
    }

    // Returns the total number of items in the data set
    @Override
    public int getItemCount() {
        return postList.size();
    }
}
