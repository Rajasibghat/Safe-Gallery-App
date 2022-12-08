package com.example.mysafegallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mysafegallery.adapters.FilesAdapter;
import com.example.mysafegallery.databinding.ActivityVideosBinding;
import com.example.mysafegallery.interfaces.ClickListenerInterface;
import com.example.mysafegallery.models.User;
import com.example.mysafegallery.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideosActivity extends AppCompatActivity implements ClickListenerInterface {
    private ActivityVideosBinding binding;
    private User user;
    private File videosPath;
    private List<File> videosFiles=new ArrayList<>();
    private ProgressDialog dialog;
    private FilesAdapter adapter;
    private Thread videosThread;
    private Handler handler;
    private Runnable videosRunnable;
    private String folderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getResources().getString(R.string.actionBar_text_videos_screen));
        user=getIntent().getParcelableExtra(Constants.KEY_INTENT_USER);
        folderName=user.getUserName().substring(0,4)+"Videos";
        videosPath= new File(getFilesDir(),folderName);
        videosFiles=getAllVideos(videosPath);
        adapter=new FilesAdapter(this);
        handler=new Handler();
        dialog=new ProgressDialog(this);
        dialog.setMessage("Getting Videos...");
        dialog.setCancelable(false);
        dialog.show();
        videosRunnable=new Runnable() {
            @Override
            public void run() {
                videosFiles=getAllVideos(videosPath);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(videosFiles.size()!=0){
                            binding.notFoundContent.setVisibility(View.GONE);
                        }else{
                            binding.notFoundContent.setVisibility(View.VISIBLE);
                        }
                        adapter.setFiles(videosFiles);
                        binding.videosRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        binding.videosRecyclerView.setAdapter(adapter);
                        dialog.hide();
                    }
                });
            }
        };
        videosThread=new Thread(videosRunnable);
        videosThread.start();

    }
    private List<File> getAllVideos(File path) {
        List<File> filesList=new ArrayList<>();
        File[] files=path.listFiles();
        if(files!=null){
            for(File file:files){
                if(file.isDirectory()){
                    getAllVideos(file);
                }else{
                    if(file.getName().endsWith(".mp4")){
                        filesList.add(file);
                    }
                }
            }
        }
        return filesList;
    }

    @Override
    public void onItemClick(File file) {
        Intent intent=new Intent(VideosActivity.this,VideoPlayScreen.class);
        intent.putExtra("filePath",file.getPath());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(File file) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Want do you want to do with selected video?")
                .setTitle("Video Options!")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(file.delete()){
                            Toast.makeText(VideosActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
                            videosFiles.remove(file);
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(VideosActivity.this, "Unable to delete file", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Constants.EXPORT_VIDEO_DIRECTORY.exists()){
                            Constants.EXPORT_VIDEO_DIRECTORY.mkdir();
                        }
                        File exportedFile=new File(Constants.EXPORT_VIDEO_DIRECTORY,file.getName());
                        try {
                            FileOutputStream outputStream=new FileOutputStream(exportedFile);
                            FileInputStream inputStream=new FileInputStream(file);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            if(bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)){
                                Toast.makeText(VideosActivity.this, "Video Exported", Toast.LENGTH_SHORT).show();
                                file.delete();
                                videosFiles.remove(file);
                                adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(VideosActivity.this, "Video Cannot Exported", Toast.LENGTH_SHORT).show();
                            }
                            inputStream.close();
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(VideosActivity.this, "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Toast.makeText(VideosActivity.this, "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }


}