package com.example.securebloggingapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securebloggingapp.R;
import com.example.securebloggingapp.adapter.BlogAdapter;
import com.example.securebloggingapp.viewmodel.BlogViewModel;

public class MainActivity extends AppCompatActivity {

    private BlogViewModel blogViewModel;
    private BlogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.searchView);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        blogViewModel.getAllBlogs().observe(this, blogPosts -> {
            adapter = new BlogAdapter(this, blogPosts, post -> {
                Intent intent = new Intent(MainActivity.this, ViewPostActivity.class);
                intent.putExtra("BLOG_ID", post.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        });

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

        findViewById(R.id.fabAdd).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddEditActivity.class));
        });
    }
}