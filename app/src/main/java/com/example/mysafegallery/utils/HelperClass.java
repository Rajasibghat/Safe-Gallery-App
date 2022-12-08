package com.example.mysafegallery.utils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.mysafegallery.NewUserActivity;

public class HelperClass {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkWritePermission(Context context) {
        return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Boolean checkReadPermission(Context context) {
        return context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
    }
}
