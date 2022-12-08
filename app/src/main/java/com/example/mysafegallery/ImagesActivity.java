package com.example.mysafegallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mysafegallery.adapters.FilesAdapter;
import com.example.mysafegallery.databinding.ActivityImagesBinding;
import com.example.mysafegallery.databinding.ImageDialogViewBinding;
import com.example.mysafegallery.interfaces.ClickListenerInterface;
import com.example.mysafegallery.models.User;
import com.example.mysafegallery.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ClickListenerInterface {
    private ActivityImagesBinding binding;
    private User user;
    private File imagesPath;
    private List<File> imagesFiles=new ArrayList<>();
    private ProgressDialog dialog;
    private FilesAdapter adapter;
    private Thread imagesThread;
    private Handler handler;
    private Runnable imagesRunnable;
    private String folderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getResources().getString(R.string.actionBar_text_images_screen));
        user=getIntent().getParcelableExtra(Constants.KEY_INTENT_USER);
        folderName=user.getUserName().substring(0,4)+"Images";
        imagesPath= new File(getFilesDir(),folderName);
        imagesFiles=getAllImages(imagesPath);
        adapter=new FilesAdapter(this);
        handler=new Handler();
        dialog=new ProgressDialog(this);
        dialog.setMessage("Getting Images...");
        dialog.setCancelable(false);
        dialog.show();
        imagesRunnable=new Runnable() {
            @Override
            public void run() {
                imagesFiles=getAllImages(imagesPath);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(imagesFiles.size()!=0){
                            binding.notFoundContent.setVisibility(View.GONE);
                        }else{
                            binding.notFoundContent.setVisibility(View.VISIBLE);
                        }
                        adapter.setFiles(imagesFiles);
                        binding.imagesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        binding.imagesRecyclerView.setAdapter(adapter);
                        dialog.hide();
                    }
                });
            }
        };
        imagesThread=new Thread(imagesRunnable);
        imagesThread.start();


    }

    private List<File> getAllImages(File path) {
        List<File> filesList=new ArrayList<>();
        File[] files=path.listFiles();
        if(files!=null){
            for(File file:files){
                if(file.isDirectory()){
                    getAllImages(file);
                }else{
                    if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")){
                        filesList.add(file);
                    }
                }
            }
        }
        return filesList;
    }

    @Override
    public void onItemClick(File file) {
        Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view=ImageDialogViewBinding.inflate(getLayoutInflater()).getRoot();
        dialog.setContentView(view);
        ImageView imageView=view.findViewById(R.id.clickedImage);
        imageView.setImageURI(Uri.fromFile(file));
        dialog.show();
    }

    @Override
    public void onItemLongClick(File file) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Want do you want to do with selected image?")
                .setTitle("Image Options!")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(file.delete()){
                            Toast.makeText(ImagesActivity.this, "File deleted", Toast.LENGTH_SHORT).show();
                            imagesFiles.remove(file);
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(ImagesActivity.this, "Unable to delete file", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Constants.EXPORT_IMAGE_DIRECTORY.exists()){
                            Constants.EXPORT_IMAGE_DIRECTORY.mkdir();
                        }
                        File exportedFile=new File(Constants.EXPORT_IMAGE_DIRECTORY,file.getName());
                        try {
                            FileOutputStream outputStream=new FileOutputStream(exportedFile);
                            FileInputStream inputStream=new FileInputStream(file);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            if(bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)){
                                Toast.makeText(ImagesActivity.this, "Image Exported", Toast.LENGTH_SHORT).show();
                                file.delete();
                                imagesFiles.remove(file);
                                adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(ImagesActivity.this, "Image Cannot Exported", Toast.LENGTH_SHORT).show();
                            }
                            inputStream.close();
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(ImagesActivity.this, "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Toast.makeText(ImagesActivity.this, "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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