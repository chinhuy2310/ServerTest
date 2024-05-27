package com.example.servertest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servertest.adapter.SelectedImageAdapter;
import com.example.servertest.adapter.spinnerAdapter;


import com.example.servertest.model.ItemData;
import com.example.servertest.model.Post;
import com.example.servertest.model.User;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private CheckBox checkBoxIsRecipe;
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonChooseImage;
    private RecyclerView imageViewSelected;
    private Button buttonSubmit;
    private User loggedInUser;
    private Spinner spinner;
    private boolean isSpinnerOpen = false;
    private APIService apiService;
    private ProgressDialog progressDialog;
    private List<Uri> imageUris = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            loggedInUser = (User) intent.getSerializableExtra("user");
//                Log.e("userid dang nhap","trang addpost :"+loggedInUser.getUserId());
        }
        apiService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);

        // Ánh xạ các view từ layout
        checkBoxIsRecipe = findViewById(R.id.checkBoxIsRecipe);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        spinner = findViewById(R.id.spinner);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isSpinnerOpen==false) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            editTextContent.clearFocus();
                            editTextTitle.clearFocus();
                            isSpinnerOpen = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isSpinnerOpen = false;
                        break;
                }
                return false;
            }
        });
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        // Thiết lập layout manager cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageViewSelected.setLayoutManager(layoutManager);

        // Thiết lập nút back trên ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // Đặt icon back tại đây
        }
        List<ItemData> itemList = createItemgridList();
        spinnerAdapter adapter = new spinnerAdapter(this, itemList);
        spinner.setAdapter(adapter);

        // Xử lý sự kiện khi nhấn nút Gửi bài viết
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_title = editTextTitle.getText().toString().trim();
                String post_content = editTextContent.getText().toString().trim();
                int isRecipe = checkBoxIsRecipe.isChecked() ? 1 : 0;
                int userId = loggedInUser.getUserId();
                ItemData selectedItem = (ItemData) spinner.getSelectedItem();
                int postGroupId = selectedItem.getGroupId();

                // Tạo RequestBody để gửi dữ liệu bài viết đến API
                RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), post_title);
                RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), post_content);
                RequestBody isRecipeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isRecipe));
                RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
                RequestBody postGroupBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postGroupId));

                // Tạo danh sách các file ảnh đã chọn
                List<MultipartBody.Part> imageParts = new ArrayList<>();
                for (Uri uri : imageUris) {
                    File file = new File(getRealPathFromURI(uri));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
                    imageParts.add(imagePart);
                }

                // Gửi dữ liệu bài viết và ảnh đến API endpoint
                Call<Void> call = apiService.createPostWithImages(titleBody, contentBody, isRecipeBody, userIdBody, postGroupBody, imageParts);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddPostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddPostActivity.this, "Failed to create post", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AddPostActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", t.getMessage());
                    }
                });
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting...");
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showCancelConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Nếu người dùng đã chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                imageUris.clear(); // Xóa các URI cũ
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri); // Thêm URI mới vào danh sách
                }
                displaySelectedImages(imageUris);
            } else if (data.getData() != null) {
                // Nếu người dùng chỉ chọn một ảnh
                Uri imageUri = data.getData();
                imageUris.clear(); // Xóa các URI cũ
                imageUris.add(imageUri); // Thêm URI mới vào danh sách
                displaySelectedImages(imageUris);
            }
        }
    }
    private void displaySelectedImages(List<Uri> imageUris) {
        List<String> imagePaths = new ArrayList<>();
        for (Uri uri : imageUris) {
            // Lấy đường dẫn của ảnh và thêm vào danh sách
            String imagePath = uri.toString();
            imagePaths.add(imagePath);
        }
        if (!imagePaths.isEmpty()) {
            // Tạo adapter và thiết lập cho RecyclerView
            SelectedImageAdapter adapter1 = new SelectedImageAdapter(this, imagePaths);
            adapter1.setOnListEmptyListener(new SelectedImageAdapter.OnListEmptyListener() {
                @Override
                public void onListEmpty() {
                    imageViewSelected.setVisibility(View.GONE);
                }
            });
            imageViewSelected.setAdapter(adapter1);
            imageViewSelected.setVisibility(View.VISIBLE);
        } else {
            imageViewSelected.setVisibility(View.GONE);
        }
    }
    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Cancel Post");
        builder.setMessage("Do you want to cancel this post?");
        builder.setPositiveButton("Continue Writing", (dialogInterface, i) -> {
            // Do nothing, continue writing
        });
        builder.setNegativeButton("Cancel Post", (dialogInterface, i) -> {
            finish(); // Đóng Activity nếu người dùng chọn hủy bài viết
        });
        builder.show();
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