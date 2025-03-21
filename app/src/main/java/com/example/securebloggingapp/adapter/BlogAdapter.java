package com.example.securebloggingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.securebloggingapp.R;
import com.example.securebloggingapp.model.BlogPost;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private Context context;
    private List<BlogPost> blogList;
    private List<BlogPost> originalList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BlogPost post);
    }

    public BlogAdapter(Context context, List<BlogPost> blogList, OnItemClickListener listener) {
        this.context = context;
        this.blogList = blogList;
        this.originalList = new ArrayList<>(blogList);
        this.listener = listener;
    }

    @NotNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_blog_post, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull BlogViewHolder holder, int position) {
        BlogPost post = blogList.get(position);
        holder.title.setText(post.getTitle());
        holder.dateTime.setText(post.getDateTime());
        Glide.with(context).load(post.getImagePath()).into(holder.image);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(post));
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public void filter(String text) {
        blogList.clear();
        if (text.isEmpty()) {
            blogList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (BlogPost post : originalList) {
                if (post.getTitle().toLowerCase().contains(text)) {
                    blogList.add(post);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView title, dateTime;
        ImageView image;

        public BlogViewHolder(@NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            dateTime = itemView.findViewById(R.id.postDate);
            image = itemView.findViewById(R.id.postImage);

        }
    }
}
