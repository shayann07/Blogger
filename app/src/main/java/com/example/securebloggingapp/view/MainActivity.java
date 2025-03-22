package com.example.securebloggingapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.securebloggingapp.R;
import com.example.securebloggingapp.adapter.BlogAdapter;
import com.example.securebloggingapp.model.BlogPost;
import com.example.securebloggingapp.viewmodel.BlogViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BlogViewModel blogViewModel;
    private BlogAdapter adapter;
    private RecyclerView recyclerView;
    private View btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setVisibility(View.GONE); // Hide delete button initially

        adapter = new BlogAdapter(this, new ArrayList<>(), post -> {
            Intent intent = new Intent(MainActivity.this, ViewPostActivity.class);
            intent.putExtra("BLOG_ID", post.getId());
            startActivity(intent);
        }, selectedCount -> {
            // Show delete button only when items are selected
            btnDelete.setVisibility(selectedCount > 0 ? View.VISIBLE : View.GONE);
        });

        recyclerView.setAdapter(adapter);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        // Search functionality
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        // Floating Action Button to Add New Post
        findViewById(R.id.fabAdd).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, AddEditActivity.class))
        );

        // Delete selected posts
        btnDelete.setOnClickListener(v -> {
            List<BlogPost> selected = adapter.getSelectedPosts();
            if (!selected.isEmpty()) {
                List<Integer> ids = new ArrayList<>();
                for (BlogPost post : selected) {
                    ids.add(post.getId());
                }
                blogViewModel.deleteMultiple(ids);
                adapter.setMultiSelectMode(false);
                adapter.clearSelection();
                adapter.updateData(blogViewModel.fetchBlogsNow());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateData(blogViewModel.fetchBlogsNow());
    }
}
