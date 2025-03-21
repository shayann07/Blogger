package com.example.securebloggingapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.securebloggingapp.R;
import com.example.securebloggingapp.model.BlogPost;
import com.example.securebloggingapp.viewmodel.BlogViewModel;

public class ViewPostActivity extends AppCompatActivity {

    private BlogViewModel blogViewModel;
    private BlogPost blogPost;
    private TextView viewTitle, viewContent;
    private ImageView viewImage;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        viewTitle = findViewById(R.id.viewTitle);
        viewContent = findViewById(R.id.viewContent);
        viewImage = findViewById(R.id.viewImage);
        btnShare = findViewById(R.id.btnShare);

        int blogId = getIntent().getIntExtra("BLOG_ID", -1);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        blogViewModel.getAllBlogs().observe(this, blogs -> {
            for (BlogPost post : blogs) {
                if (post.getId() == blogId) {
                    blogPost = post;
                    showData();
                }
            }
        });

        btnShare.setOnClickListener(v -> sharePost());
    }

    private void showData() {
        viewTitle.setText(blogPost.getTitle());
        viewContent.setText(blogPost.getContent());
        Glide.with(this).load(blogPost.getImagePath()).into(viewImage);
    }

    private void sharePost() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, blogPost.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, blogPost.getContent());

        if (blogPost.getImagePath() != null && !blogPost.getImagePath().isEmpty()) {
            Uri imageUri = Uri.parse(blogPost.getImagePath());
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
        }

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}