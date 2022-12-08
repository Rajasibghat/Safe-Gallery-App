package com.example.mysafegallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.mysafegallery.databinding.ActivitySplashBinding;
import com.example.mysafegallery.utils.Constants;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences=getSharedPreferences(Constants.PREF_FILE_NAME,MODE_PRIVATE);
        if(preferences.getBoolean(Constants.PREF_THEME_KEY,false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        binding.animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(preferences.getBoolean(Constants.USER_EXISTS_KEY,false)){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this,NewUserActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferences.getBoolean(Constants.USER_EXISTS_KEY,false)){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }else{
                startActivity(new Intent(SplashActivity.this,NewUserActivity.class));
                }
                finish();
            }
        },2000);*/
    }
}