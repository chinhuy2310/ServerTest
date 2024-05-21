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

    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    public adapterPostMenu1(Context context, List<Post> postList, OnPostClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.onPostClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgNewTruyen);
            textViewTitle = itemView.findViewById(R.id.textviewTenTruyen);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_popular_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = postList.get(position);


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
//        List<String> imageUrls = post.getImageUrlList();
//        String firstImageUrl = null;
//        if (imageUrls != null && !imageUrls.isEmpty()) {
//            firstImageUrl = imageUrls.get(0);
//            Log.e("img",String.valueOf(firstImageUrl));
//        }


        if (firstImageUrl != null && !firstImageUrl.isEmpty()) {
            Picasso.get().load(firstImageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.food_bg);
        }

        holder.textViewTitle.setText(post.getTitle());

        // Handle click event on image
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    onPostClickListener.onPostClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
