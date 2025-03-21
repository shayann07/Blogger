// ✅ FINAL PATCH - BlogViewModel.java exposes fetchBlogsNow()
package com.example.securebloggingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.securebloggingapp.model.BlogPost;
import com.example.securebloggingapp.repository.BlogRepository;

import java.util.List;

public class BlogViewModel extends AndroidViewModel {
    private BlogRepository repository;

    public BlogViewModel(@NonNull Application application) {
        super(application);
        repository = new BlogRepository(application);
    }

    // ✅ Manual pull for fresh data
    public List<BlogPost> fetchBlogsNow() {
        return repository.fetchBlogsNow();
    }

    public void insert(BlogPost post) {
        repository.insertBlog(post);
    }
}
