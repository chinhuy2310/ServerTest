package com.example.servertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;


import androidx.viewpager2.widget.ViewPager2;


import com.example.servertest.adapter.ViewPagerAdapter;
import com.example.servertest.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private User loggedInUser;
    private InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //ẩn nút back
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            loggedInUser = (User) intent.getSerializableExtra("user");
        }


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        // Khởi tạo các Fragment và thêm vào Adapter



        if (loggedInUser != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", loggedInUser);


            Menu1Fragment menu1Fragment = new Menu1Fragment();
            menu1Fragment.setArguments(bundle);
            adapter.addFragment(menu1Fragment);

            Menu2Fragment menu2Fragment = new Menu2Fragment();
            menu2Fragment.setArguments(bundle);
            adapter.addFragment(menu2Fragment);

            Menu3Fragment menu3Fragment = new Menu3Fragment();
            menu3Fragment.setArguments(bundle);
            adapter.addFragment(menu3Fragment);

            adapter.addFragment(new Menu4Fragment());

            Menu5Fragment menu5Fragment = new Menu5Fragment();
            menu5Fragment.setArguments(bundle);
            adapter.addFragment(menu5Fragment);
        }else{
            adapter.addFragment(new Menu1Fragment());
            adapter.addFragment(new Menu2Fragment());
            adapter.addFragment(new Menu3Fragment());
            adapter.addFragment(new Menu4Fragment());
            adapter.addFragment(new Menu5Fragment());
        }

//        if (loggedInUser == null) {
//            Log.e("loggedin ","null");
//        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        int[] tabIcons = {R.drawable.ic_home, R.drawable.bangtin, R.drawable.ic_search, R.drawable.ic_notification, R.drawable.ic_taikhoan};
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Xác định hình ảnh cho tab dựa trên vị trí
            tab.setIcon(tabIcons[position]);
        }).attach();



        // Thiết lập bộ lắng nghe cho sự kiện khi người dùng chọn tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Lấy vị trí của tab được chọn
                int position = tab.getPosition();
                // Di chuyển viewPager đến tab tương ứng
                viewPager.setCurrentItem(position, true);
//                viewPager.setCurrentItem(tab.getPosition(), true);
//                hideKeyboard();

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý khi tab bị bỏ chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần xử lý khi tab được chọn lại
            }
        });
    }

    public void switchToMenu3Fragment(int groupId) {
        ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
        Menu3Fragment menu3Fragment = (Menu3Fragment) adapter.createFragment(2);

        // Perform the Fragment transition
        if (menu3Fragment != null) {
            // Perform any necessary processing before transitioning Fragment (if needed)
            menu3Fragment.fetchDataFromApi(groupId,"");

            // Move to Menu3Fragment
            viewPager.setCurrentItem(2, true);
        }
    }
//    private void hideKeyboard() {
//        if (inputMethodManager != null && getCurrentFocus() != null) {
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//    }

}
