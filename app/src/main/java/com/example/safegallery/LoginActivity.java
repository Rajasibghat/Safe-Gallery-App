package com.example.safegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.safegallery.databinding.ActivityLoginBinding;
import com.example.safegallery.models.User;
import com.example.safegallery.utils.Constants;
import com.example.safegallery.utils.databasehelper.CRUDHelper;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private CRUDHelper crudHelper;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences=getSharedPreferences(Constants.PREF_FILE_NAME,MODE_PRIVATE);
        crudHelper=new CRUDHelper(this);
        String name=preferences.getString(Constants.KEY_USER_NAME,"");
        binding.inputUserName.setText(name);



        binding.btnLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validUser()){
                    String name=binding.inputUserName.getText().toString();
                    String pass=binding.inputPassword.getText().toString();
                    if(crudHelper.checkUserAlreadyExists(name,pass)){
                        User user= crudHelper.getUser(name,pass);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra(Constants.KEY_INTENT_USER,user);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.textCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,NewUserActivity.class));
                finish();
            }
        });
        binding.textResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });

    }
    private Boolean validUser() {
        boolean validity;
        String userName=binding.inputUserName.getText().toString();
        String userPassword=binding.inputPassword.getText().toString();
        if(userName.trim().length()==0){
            binding.inputUserName.setError("Username is empty");
            binding.inputUserName.requestFocus();
            validity= false;
        }else if(userName.length()<4){
            binding.inputUserName.setError("Username too short");
            binding.inputUserName.requestFocus();
            validity= false;
        }else if(userPassword.trim().length()==0){
            binding.inputPassword.setError("Password cannot be empty");
            binding.inputPassword.requestFocus();
            validity= false;
        }else if(userPassword.length()<8){
            binding.inputPassword.setError("Password must be 8 characters long");
            binding.inputPassword.requestFocus();
            validity= false;
        }else{
            validity= true;
        }
        return validity;
    }

}