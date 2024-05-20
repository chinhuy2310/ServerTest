package com.example.servertest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servertest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public SelectedImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }
    private OnListEmptyListener mListener;



    public interface OnListEmptyListener {
        void onListEmpty();
    }

    public void setOnListEmptyListener(OnListEmptyListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);
        Picasso.get().load(imageUrl).into(holder.imageViewItem);
        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nhấn nút Remove
                // Trong trường hợp này, bạn có thể xóa ảnh tương ứng khỏi danh sách
                imageUrls.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imageUrls.size());

                if (imageUrls.isEmpty() && mListener != null) {
                    mListener.onListEmpty();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
    public List<String> getImageUrls() {
        return imageUrls;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem,buttonRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

}
