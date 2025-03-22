// BlogViewModel.java
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

    public List<BlogPost> fetchBlogsNow() {
        return repository.fetchBlogsNow();
    }

    public void insert(BlogPost post) {
        repository.insertBlog(post);
    }

    public void update(BlogPost post) {
        repository.updateBlog(post);
    }

    public void delete(BlogPost post) {
        repository.deleteBlog(post.getId());
    }

    public void deleteMultiple(List<Integer> ids) {
        repository.deleteBlogs(ids);
    }
}
