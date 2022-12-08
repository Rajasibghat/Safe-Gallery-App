package com.example.mysafegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;

import com.example.mysafegallery.databinding.ActivityVideoPlayScreenBinding;

public class VideoPlayScreen extends AppCompatActivity {
    private ActivityVideoPlayScreenBinding binding;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideoPlayScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        filePath=getIntent().getStringExtra("filePath");
        binding.clickedVideo.setVideoPath(filePath);
        binding.clickedVideo.start();
        MediaController controller=new MediaController(this);
        controller.setAnchorView(binding.clickedVideo);
        binding.clickedVideo.setMediaController(controller);

    }
}