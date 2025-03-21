package com.example.securebloggingapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class ImageUtils {

    public static final int PICK_IMAGE = 100;

    public static void pickImageFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, PICK_IMAGE);
    }

    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
    }
}