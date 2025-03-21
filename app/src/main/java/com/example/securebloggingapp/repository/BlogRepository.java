package com.example.securebloggingapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.securebloggingapp.db.DatabaseHelper;
import com.example.securebloggingapp.model.BlogPost;

import java.util.ArrayList;
import java.util.List;

public class BlogRepository {

    private DatabaseHelper dbHelper;

    public BlogRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertBlog(BlogPost post) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, post.getTitle());
        values.put(DatabaseHelper.COLUMN_CONTENT, post.getContent());
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, post.getImagePath());
        values.put(DatabaseHelper.COLUMN_DATE_TIME, post.getDateTime());
        db.insert(DatabaseHelper.TABLE_BLOG, null, values);
        db.close();
    }

    public void deleteBlog(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_BLOG, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateBlog(BlogPost post) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, post.getTitle());
        values.put(DatabaseHelper.COLUMN_CONTENT, post.getContent());
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, post.getImagePath());
        values.put(DatabaseHelper.COLUMN_DATE_TIME, post.getDateTime());
        db.update(DatabaseHelper.TABLE_BLOG, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(post.getId())});
        db.close();
    }

    public LiveData<List<BlogPost>> getAllBlogs() {
        MutableLiveData<List<BlogPost>> blogsLiveData = new MutableLiveData<>();
        List<BlogPost> blogList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_BLOG, null, null, null, null, null, DatabaseHelper.COLUMN_ID + "DESC");

        if (cursor.moveToFirst()) {
            do {
                BlogPost post = new BlogPost(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)), cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT)), cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_PATH)), cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_TIME)));
                blogList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        blogsLiveData.setValue(blogList);
        return blogsLiveData;
    }
}
