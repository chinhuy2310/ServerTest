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
//                    ((MainActivity)requireActivity()).switchToMenu3Fragment(groupId);
                }
            });
            gridLayout.addView(itemView);
        }
        ActionViewFlipper();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList = new ArrayList<>();
        adapter = new adapterPostMenu1(getActivity(), postList, new MyOnPostClickListener());
        recyclerView.setAdapter(adapter);

        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

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

    // Flipper advertisement
    private void ActionViewFlipper() {
        ArrayList<String> advertisement = new ArrayList<>();
        // Add advertisement image URLs to the list
        advertisement.add("https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202303/28/dailylife/20230328110003580sufq.jpg");
        advertisement.add("https://static.hubzum.zumst.com/hubzum/2022/02/10/14/3ea39a0f140c44b8b26761a93d11aa18.jpg");
        advertisement.add("https://dulichlive.com/han-quoc/wp-content/uploads/sites/7/2020/02/10-mon-an-Han-Quoc-khong-cay-ngon-noi-tieng.jpg");
        advertisement.add("https://forza.com.vn/wp-content/uploads/2021/07/cach-lam-mi-y-thom-ngon-chuan-vi-tai-nha-6.jpeg");
        for (int i = 0; i < advertisement.size(); i++) {
            ImageView imageView = new ImageView(requireContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(advertisement.get(i)).into(imageView);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
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
