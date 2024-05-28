package com.example.servertest;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servertest.adapter.adapterPostMenu1;
import com.example.servertest.model.ImageResponse;
import com.example.servertest.model.ItemData;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menu1Fragment extends Fragment {

    ViewFlipper viewFlipper;
    private RecyclerView recyclerView;
    private adapterPostMenu1 adapter;
    private List<Post> postList;
    private APIService apiService;
    private List<ItemData> itemgridList;
    private User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu1fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");}
        viewFlipper = view.findViewById(R.id.viewflipper);
        // Home menu item list
        itemgridList = createItemgridList();
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        for (final ItemData item : itemgridList) {
            View itemView = getLayoutInflater().inflate(R.layout.item_grid, gridLayout, false);
            ImageView imageView = itemView.findViewById(R.id.itemgridImage);
            TextView textView = itemView.findViewById(R.id.itemgridText);
            // Set image and text for each item
            imageView.setImageResource(item.getImageResId());
            textView.setText(item.getItemgridName());
            // Add click listener to each item
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // add activity/intent here
                    Toast.makeText(requireContext(), item.getItemgridName(), Toast.LENGTH_SHORT).show();
                    int groupId = item.getGroupId();
                    ((MainActivity)requireActivity()).switchToMenu3Fragment(groupId);
                }
            });
            gridLayout.addView(itemView);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList = new ArrayList<>();
        adapter = new adapterPostMenu1(getActivity(), postList, new MyOnPostClickListener());
        recyclerView.setAdapter(adapter);

        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        fetchAdvertisementImages();

        // Lấy danh sách các bài viết phổ biến từ máy chủ
        
        getPopularPostsFromServer();

        return view;
    }
    public void onResume() {
        super.onResume();
        getPopularPostsFromServer() ;
    }

    private void getPopularPostsFromServer() {
        int userId = (user != null) ? user.getUserId() : -1;
        Call<List<Post>> call = apiService.getPopularPosts(userId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    postList.clear();
                    postList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve popular posts", Toast.LENGTH_SHORT).show();
                    Log.e("Menu1Frament", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network error: ",t.getMessage());
            }
        });
    }

    private class MyOnPostClickListener implements adapterPostMenu1.OnPostClickListener {
        @Override
        public void onPostClick(int position) {
            Post clickedPost = postList.get(position);
            int postId = clickedPost.getPostId();
            // Gửi Intent đến PostDetail Activity và đính kèm đối tượng Post
            Intent intent = new Intent(getActivity(), PostDetail.class);
            intent.putExtra("POST_DETAIL", clickedPost);
            intent.putExtra("POST_ID", postId);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
    private void fetchAdvertisementImages() {
        Call<ImageResponse> call = apiService.getImageUrls();
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> imageUrls = response.body().getAdvertisement();
                    setupViewFlipper(imageUrls);
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve advertisement images", Toast.LENGTH_SHORT).show();
                    Log.e("Menu1Fragment", "Response code: " + response.code());
                    setupViewFlipper(new ArrayList<>());  // Call with empty list to avoid null issues
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network error: ", t.getMessage());
                setupViewFlipper(new ArrayList<>());  // Call with empty list to avoid null issues
            }
        });
    }

    private void setupViewFlipper(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            Toast.makeText(getActivity(), "No advertisement images to display", Toast.LENGTH_SHORT).show();
            Log.e("Menu1Fragment", "Image URL list is null or empty");
            return;
        }

        viewFlipper.removeAllViews();  // Clear previous views

        for (String url : imageUrls) {
            ImageView imageView = new ImageView(requireContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // Use Target to ensure the image is loaded before adding to ViewFlipper
            Picasso.get().load(url).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    viewFlipper.addView(imageView);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Picasso error", "Error loading image: " + url, e);
                }
            });
            Log.e("ViewFlipper", url);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);

        // Start the flipper if not already started
        if (!viewFlipper.isFlipping()) {
            viewFlipper.startFlipping();
        }
    }


    private List<ItemData> createItemgridList() {
        List<ItemData> itemgridList = new ArrayList<>();
        itemgridList.add(new ItemData(R.drawable.meat, "고기",1));
        itemgridList.add(new ItemData(R.drawable.seafood, "생선",2));
        itemgridList.add(new ItemData(R.drawable.cereal, "곡류",3));
        itemgridList.add(new ItemData(R.drawable.vegetable, "채소",4));
//        itemgridList.add(new ItemData(R.drawable.botmi, "간식"));
        itemgridList.add(new ItemData(R.drawable.dessert, "디저트",5));
        itemgridList.add(new ItemData(R.drawable.cooking, " 끓임",6));
        itemgridList.add(new ItemData(R.drawable.deep_fried, "튀김",7));
        itemgridList.add(new ItemData(R.drawable.soup, " 국",8));
        itemgridList.add(new ItemData(R.drawable.grill, "구워",9));
        itemgridList.add(new ItemData(R.drawable.fried, "볶음",10));
        itemgridList.add(new ItemData(R.drawable.smoothie, "스무티",11));
        itemgridList.add(new ItemData(R.drawable.ic_delete, "Item 12",12));
        itemgridList.add(new ItemData(R.drawable.ic_delete, "Item 13",13));
        itemgridList.add(new ItemData(R.drawable.ic_delete, "Item 14",14));
        // Add data for other items here
        return itemgridList;
    }
}
