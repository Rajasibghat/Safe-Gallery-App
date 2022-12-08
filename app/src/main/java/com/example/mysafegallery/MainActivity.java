package com.example.mysafegallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mysafegallery.databinding.ActivityMainBinding;
import com.example.mysafegallery.models.User;
import com.example.mysafegallery.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<Uri> imagesFiles=new ArrayList<>();
    private List<Uri> videosFiles=new ArrayList<>();
    private User user;
    private Runnable imagesRunnable;
    private Runnable videosRunnable;
    private Handler handler;
    private ProgressDialog dialog;
    private Thread imagesThread;
    private Thread videosThread;
    private SharedPreferences.Editor sharedPreferencesEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getResources().getString(R.string.actionBar_text_main_screen));
        user=getIntent().getParcelableExtra(Constants.KEY_INTENT_USER);
        handler=new Handler();
        dialog=new ProgressDialog(this);
        sharedPreferencesEditor=getSharedPreferences(Constants.PREF_FILE_NAME,MODE_PRIVATE).edit();
        imagesRunnable=new Runnable() {
            @Override
            public void run() {
                saveImages();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Images Saved", Toast.LENGTH_SHORT).show();
                        binding.textSelectedImages.setVisibility(View.GONE);
                        dialog.hide();
                    }
                });
            }
        };

        imagesThread=new Thread(imagesRunnable);
        videosRunnable=new Runnable() {
            @Override
            public void run() {
                saveVideos();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Videos Saved", Toast.LENGTH_SHORT).show();
                        binding.textSelectedVideos.setVisibility(View.GONE);
                        dialog.hide();
                    }
                });
            }
        };
        videosThread=new Thread(videosRunnable);

        binding.btnImportImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imagesLauncher.launch(intent);
            }
        });
        binding.btnImportVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                videosLauncher.launch(intent);
            }
        });
        binding.textSelectedImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Saving Images...");
                dialog.setCancelable(false);
                dialog.show();
                imagesThread.start();
            }
        });
        binding.textSelectedVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Saving Videos...");
                dialog.setCancelable(false);
                dialog.show();
                videosThread.start();
            }
        });
        binding.cardImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImagesActivity.class);
                intent.putExtra(Constants.KEY_INTENT_USER,user);
                startActivity(intent);
            }
        });
        binding.cardVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VideosActivity.class);
                intent.putExtra(Constants.KEY_INTENT_USER,user);
                startActivity(intent);
            }
        });

        }

    private void saveImages() {
        String imagesFolderName=user.getUserName().substring(0,4)+"Images/";
        File mainPath=new File(getFilesDir(),imagesFolderName);
        if(!mainPath.exists()){
            mainPath.mkdir();
        }
        for(int i=0;i<imagesFiles.size();i++){
        File fileName=new File(mainPath,System.currentTimeMillis()+".jpg");
            try {
                FileOutputStream outputStream=new FileOutputStream(fileName,false);
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imagesFiles.get(i));
                bitmap.compress(Bitmap.CompressFormat.JPEG,95,outputStream);
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveVideos() {
        String videosFolderName=user.getUserName().substring(0,4)+"Videos/";
        File mainPath=new File(getFilesDir(),videosFolderName);
        if(!mainPath.exists()){
            mainPath.mkdir();
        }
        for(int i=0;i<videosFiles.size();i++){
            File fileName=new File(mainPath,System.currentTimeMillis()+".mp4");
            try {
                FileOutputStream outputStream=new FileOutputStream(fileName,false);
                InputStream inputStream=getContentResolver().openInputStream(videosFiles.get(i));
                byte[] bytes=new byte[1024];
                int ch;
                while((ch=inputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,ch);
                }
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ActivityResultLauncher<Intent> videosLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                int count=0;
                videosFiles.clear();
                if(result.getData().getClipData()!=null){
                    count=result.getData().getClipData().getItemCount();
                    for(int i=0;i<count;i++){
                        Uri uri=result.getData().getClipData().getItemAt(i).getUri();
                        videosFiles.add(uri);
                    }
                }else{
                    Uri uri=result.getData().getData();
                    videosFiles.add(uri);
                    count=1;
                }
                binding.textSelectedVideos.setText(count+" videos selected. Click to Save");
                binding.textSelectedVideos.setVisibility(View.VISIBLE);
            }
        }
    });

    private ActivityResultLauncher<Intent> imagesLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                int count=0;
                imagesFiles.clear();
                if(result.getData().getClipData()!=null){
                    count=result.getData().getClipData().getItemCount();
                    for(int i=0;i<count;i++){
                        Uri uri=result.getData().getClipData().getItemAt(i).getUri();
                        imagesFiles.add(uri);
                    }
                }else{
                    Uri uri=result.getData().getData();
                    imagesFiles.add(uri);
                    count=1;
                }
                binding.textSelectedImages.setText(count+" Images selected. Click to Save");
                binding.textSelectedImages.setVisibility(View.VISIBLE);
            }
        }
    });


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.menuChangeTheme);
        View view = (View) menuItem.getActionView();
        SwitchCompat switchCompat=view.findViewById(R.id.switchChangeTheme);
        SharedPreferences preferences=getSharedPreferences(Constants.PREF_FILE_NAME,MODE_PRIVATE);
        switchCompat.setChecked(preferences.getBoolean(Constants.PREF_THEME_KEY,false));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferencesEditor.putBoolean(Constants.PREF_THEME_KEY,true);
                    sharedPreferencesEditor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    sharedPreferencesEditor.putBoolean(Constants.PREF_THEME_KEY,false);
                    sharedPreferencesEditor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}