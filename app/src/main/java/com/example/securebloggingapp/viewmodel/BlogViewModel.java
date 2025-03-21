package com.example.securebloggingapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.securebloggingapp.model.BlogPost;
import com.example.securebloggingapp.repository.BlogRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlogViewModel extends AndroidViewModel {


    private BlogRepository repository;
    private LiveData<List<BlogPost>> blogList;

    public BlogViewModel(@NotNull Application application) {
        super(application);
        repository = new BlogRepository(application);
        blogList = repository.getAllBlogs();
    }

    public LiveData<List<BlogPost>> getAllBlogs() {
        return blogList;
    }

    public void insert(BlogPost post) {
        repository.insertBlog(post);
    }

    public void delete(int id) {
        repository.deleteBlog(id);
    }

    public void update(BlogPost post) {
        repository.updateBlog(post);
    }

}