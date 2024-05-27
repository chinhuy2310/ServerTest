package com.example.servertest;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.servertest.adapter.adapterPostMenu1;

import com.example.servertest.model.Post;
import com.example.servertest.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Menu3Fragment extends Fragment {
    private RecyclerView recyclerView;
    private adapterPostMenu1 adapter;
    private List<Post> postList;
    private int groupID;
    private TextView emptyView,textView3;
    private EditText searchEditText;
    private ImageView searchButton,filterButton,searchButtonL;
    private HorizontalScrollView horizontalScrollView;
    private String searchText = "";
    private View childView;
    private User user;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu3fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");}
        searchButtonL = view.findViewById(R.id.search_button_left);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        textView3 = view.findViewById(R.id.textview3);

        emptyView = view.findViewById(R.id.emptyView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList = new ArrayList<>();
        adapter = new adapterPostMenu1(getActivity(), postList, new MyOnPostClickListener());
        recyclerView.setAdapter(adapter);
        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.viewPager);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 2) {
                    // Nếu Fragment 3 được hiển thị, reset dữ liệu
                    resetData();
                }
            }
        });
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.clearFocus();
                // Lấy loại mã nhận dạng của mục được chọn
                int groupId = (int) v.getTag();

                // Thực hiện các hoạt động tương ứng với loại dữ liệu
                fetchDataFromApi(groupId,searchText);
                LinearLayout linearLayout = view.findViewById(R.id.filterLayout);
                // Đặt trạng thái chọn cho TextView được chọn và huỷ chọn các TextView khác
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    childView = linearLayout.getChildAt(i);
                    if (childView == v) {
                        // TextView được chọn
                        childView.setSelected(true);
                        Log.e("groupid", String.valueOf(groupId));
                    } else {
                        // TextView khác
                        childView.setSelected(false);
                    }
                }
            }
        };
        searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
                hideKeyboard();
            }
        });

        // Thêm sự kiện lắng nghe cho nút filter
        filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Mở dialog khi nút filter được nhấn
//                FilterDialogFragment dialogFragment = new FilterDialogFragment();
//                dialogFragment.show(getChildFragmentManager(), "FilterDialog");
            }
        });
        // Gắn sự kiện lắng nghe và loại mã nhận dạng cho mỗi TextView trong thanh cuộn ngang
        LinearLayout linearLayout = view.findViewById(R.id.filterLayout);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            childView.setTag(getGroupIdForPosition(i)); // Đặt loại mã nhận dạng cho mỗi mục
            childView.setOnClickListener(itemClickListener);
        }
        searchEditText = view.findViewById(R.id.search_editText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });



        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Xử lý sau khi text thay đổi
                if (editable.length() > 0) {
                    // Nếu có chữ được nhập vào, ẩn nút tìm kiếm
                    searchButton.setVisibility(View.VISIBLE);
                    filterButton.setVisibility(View.GONE);
                    textView3.setVisibility(View.VISIBLE);
                    horizontalScrollView.setVisibility(View.GONE);
                    searchButtonL.setVisibility(View.GONE);

                } else {
                    horizontalScrollView.setVisibility(View.GONE);
                    textView3.setVisibility(View.VISIBLE);

                }
            }
        });
//        retrievePostsByGroupFromDatabase();

        return view;
    }
    public void search(){
        searchText = searchEditText.getText().toString();
        fetchDataFromApi(groupID, searchText);
        filterButton.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.GONE);
        searchButtonL.setVisibility(View.VISIBLE);
        horizontalScrollView.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.GONE);
        searchEditText.clearFocus();
    }
    private void resetData() {
        // Xóa nội dung của EditText và reset dữ liệu cần thiết ở đây
        if (searchEditText != null) {
            searchEditText.setText("");
        }
        filterButton.setVisibility(View.GONE);
        searchButtonL.setVisibility(View.GONE);
//        retrievePostsByGroupFromDatabase();
        groupID=0;
        LinearLayout linearLayout = getView().findViewById(R.id.filterLayout);
        linearLayout.getChildAt(0).setSelected(true);
        for (int i = 1; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            childView.setSelected(false);
        }
    }
    // Phương thức để lấy loại mã nhận dạng (groupId) cho mỗi vị trí trong thanh cuộn ngang
    private int getGroupIdForPosition(int position) {

        if (position == 0) {
            return 0; // Mục All
        } else {
            return position ; // Các mục khác
        }
    }
    private void updateEmptyViewVisibility() {
        if (postList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
    public void fetchDataFromApi(int groupId, String searchText) {
        this.groupID=groupId;
        Call<List<Post>> call = apiService.getSearch(groupId, searchText);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    postList.clear();
                    if (posts != null) {
                        postList.addAll(posts);
                    }
                    adapter.notifyDataSetChanged();
                    updateEmptyViewVisibility();
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch data from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("Menu3Fragment", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(getActivity(), "Failed to fetch data from API", Toast.LENGTH_SHORT).show();
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
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }
}