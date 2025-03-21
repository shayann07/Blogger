package com.example.securebloggingapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.securebloggingapp.R;
import com.example.securebloggingapp.model.BlogPost;
import com.example.securebloggingapp.viewmodel.BlogViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    private EditText titleEdit, contentEdit;
    private ImageView imageView;
    private Button btnSave;
    private BlogViewModel blogViewModel;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            selectedImageUri = result.getData().getData();
            Glide.with(this).load(selectedImageUri).into(imageView);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        titleEdit = findViewById(R.id.editTitle);
        contentEdit = findViewById(R.id.editContent);
        imageView = findViewById(R.id.imageView);
        btnSave = findViewById(R.id.btnSave);

        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        imageView.setOnClickListener(v -> pickImageFromGallery());

        btnSave.setOnClickListener(v -> {
            String title = titleEdit.getText().toString();
            String content = contentEdit.getText().toString();
            String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            String imagePath = selectedImageUri != null ? selectedImageUri.toString() : "";

            BlogPost post = new BlogPost(0, title, content, imagePath, dateTime);
            blogViewModel.insert(post);
            finish();
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}