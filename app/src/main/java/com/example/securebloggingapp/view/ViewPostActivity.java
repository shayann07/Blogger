package com.example.securebloggingapp.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.securebloggingapp.R;
import com.example.securebloggingapp.model.BlogPost;
import com.example.securebloggingapp.viewmodel.BlogViewModel;

import java.io.File;

public class ViewPostActivity extends AppCompatActivity {

    private BlogViewModel blogViewModel;
    private BlogPost blogPost;
    private TextView viewTitle, viewContent;
    private ImageView viewImage;
    private Button btnShare;
    private Toolbar toolbar;
    private int blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        viewTitle = findViewById(R.id.viewTitle);
        viewContent = findViewById(R.id.viewContent);
        viewImage = findViewById(R.id.viewImage);
        btnShare = findViewById(R.id.btnShare);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            overflowIcon.setTint(getResources().getColor(R.color.on_primary));
            toolbar.setOverflowIcon(overflowIcon);
        }

        blogId = getIntent().getIntExtra("BLOG_ID", -1);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        // ✅ Pull blog post from DB
        for (BlogPost post : blogViewModel.fetchBlogsNow()) {
            if (post.getId() == blogId) {
                blogPost = post;
                showData();
                break;
            }
        }

        btnShare.setOnClickListener(v -> sharePost());
    }

    private void showData() {
        viewTitle.setText(blogPost.getTitle());
        viewContent.setText(blogPost.getContent());

        // ✅ Load image from internal storage file path
        Glide.with(this)
                .load(new File(blogPost.getImagePath()))
                .into(viewImage);
    }

    private void sharePost() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // ✅ Share text content
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, blogPost.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, blogPost.getContent());

        File imageFile = new File(blogPost.getImagePath());
        if (imageFile.exists()) {
            // ✅ Get content:// URI using FileProvider
            Uri imageUri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    imageFile
            );

            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            // If image doesn't exist, share text only
            shareIntent.setType("text/plain");
        }

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) { // Back button
            finish();
            return true;
        } else if (id == R.id.action_edit) {
            editPost();
            return true;
        } else if (id == R.id.action_delete) {
            confirmDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editPost() {
        Intent intent = new Intent(ViewPostActivity.this, AddEditActivity.class);
        intent.putExtra("BLOG_ID", blogPost.getId());
        intent.putExtra("TITLE", blogPost.getTitle());
        intent.putExtra("CONTENT", blogPost.getContent());
        intent.putExtra("IMAGE_PATH", blogPost.getImagePath());
        intent.putExtra("EDIT_MODE", true);  // Flag to indicate editing mode
        startActivity(intent);

    }


    // ✅ Confirm Delete with AlertDialog
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    blogViewModel.delete(blogPost);
                    finish(); // Close activity after delete
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (BlogPost post : blogViewModel.fetchBlogsNow()) {
            if (post.getId() == blogId) {
                blogPost = post;
                showData();
                break;
            }
        }
    }
}
