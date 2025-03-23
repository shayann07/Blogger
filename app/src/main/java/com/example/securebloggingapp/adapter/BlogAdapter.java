package com.example.securebloggingapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
    private OnSelectionChangeListener selectionListener;
    private boolean isMultiSelectMode = false;
    private List<BlogPost> selectedPosts = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(BlogPost post);
    }

    public interface OnSelectionChangeListener {
        void onSelectionChanged(int count);
    }

    public BlogAdapter(Context context, List<BlogPost> blogList, OnItemClickListener listener, OnSelectionChangeListener selectionListener) {
        this.context = context;
        this.blogList = blogList;
        this.originalList = new ArrayList<>(blogList);
        this.listener = listener;
        this.selectionListener = selectionListener;
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

        // Show checkbox only in multi-select mode
        holder.checkBoxSelect.setVisibility(isMultiSelectMode ? View.VISIBLE : View.GONE);
        holder.checkBoxSelect.setChecked(selectedPosts.contains(post));

        // Click to select/deselect in multi-select mode
        holder.itemView.setOnClickListener(v -> {
            if (isMultiSelectMode) {
                toggleSelection(post, holder);
            } else {
                listener.onItemClick(post);
            }
        });

        // Long press to enable multi-select and select item
        holder.itemView.setOnLongClickListener(v -> {
            if (!isMultiSelectMode) {
                setMultiSelectMode(true);
            }
            toggleSelection(post, holder);
            return true; // Consume the event
        });
        holder.checkBoxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !selectedPosts.contains(post)) {
                selectedPosts.add(post);
            } else if (!isChecked && selectedPosts.contains(post)) {
                selectedPosts.remove(post);
            }

            // Notify MainActivity to update delete button visibility
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(selectedPosts.size());
            }

            // Exit multi-select mode if no items are selected
            if (selectedPosts.isEmpty()) {
                setMultiSelectMode(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    private void toggleSelection(BlogPost post, BlogViewHolder holder) {
        if (selectedPosts.contains(post)) {
            selectedPosts.remove(post); // Remove from selected list
            holder.checkBoxSelect.setChecked(false);
        } else {
            selectedPosts.add(post); // Add to selected list
            holder.checkBoxSelect.setChecked(true);
        }

        // Notify MainActivity to update delete button visibility
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedPosts.size());
        }

        // Exit multi-select mode if no items are selected
        if (selectedPosts.isEmpty()) {
            setMultiSelectMode(false);
        }
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

    public void updateData(List<BlogPost> newPosts) {
        originalList.clear();
        originalList.addAll(newPosts);
        blogList.clear();
        blogList.addAll(newPosts);
        notifyDataSetChanged();
    }

    public void setMultiSelectMode(boolean multiSelect) {
        isMultiSelectMode = multiSelect;
        if (!multiSelect) {
            selectedPosts.clear();
        }
        // Delay notifyDataSetChanged() so that it runs after the current layout pass
        new Handler(Looper.getMainLooper()).postDelayed(() -> notifyDataSetChanged(), 50);
    }


    public List<BlogPost> getSelectedPosts() {
        return selectedPosts;
    }

    public void clearSelection() {
        selectedPosts.clear();
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(0);
        }
        notifyDataSetChanged();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView title, dateTime;
        ImageView image;
        CheckBox checkBoxSelect;

        public BlogViewHolder(@NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            dateTime = itemView.findViewById(R.id.postDate);
            image = itemView.findViewById(R.id.postImage);
            checkBoxSelect = itemView.findViewById(R.id.checkBox);
        }
    }
}
