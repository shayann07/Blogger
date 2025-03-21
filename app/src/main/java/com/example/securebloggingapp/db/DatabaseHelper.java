package com.example.securebloggingapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "blog_app.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_BLOG = "blog";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_DATE_TIME = "date_time";

    private static final String CREATE_TABLE_BLOG =
            "CREATE TABLE " + TABLE_BLOG + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_IMAGE_PATH + " TEXT, " +
                    COLUMN_DATE_TIME + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BLOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOG);
        onCreate(db);
    }
}
