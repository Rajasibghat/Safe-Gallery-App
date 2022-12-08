package com.example.mysafegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mysafegallery.databinding.ActivityLoginBinding;
import com.example.mysafegallery.models.User;
import com.example.mysafegallery.utils.Constants;
import com.example.mysafegallery.utils.databasehelper.CRUDHelper;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private CRUDHelper crudHelper;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getResources().getString(R.string.actionBar_text_login_screen));
        initViews();
        binding.btnLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validUser()){
                    String name=binding.inputUserName.getText().toString();
                    String pass=binding.inputPassword.getText().toString();
                    dialog.show();
                    if(crudHelper.checkUserAlreadyExists(name,pass)){
                        dialog.hide();
                        User user=crudHelper.getUser(name,pass);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra(Constants.KEY_INTENT_USER,user);
                        startActivity(intent);
                        finish();
                    }else{
                        Snackbar.make(v,"User doesn't Exist",Snackbar.LENGTH_SHORT).show();
                    }
                        dialog.hide();
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

    private void initViews() {
        crudHelper=new CRUDHelper(this);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Login User...");
        dialog.setCancelable(false);
        SharedPreferences preferences=getSharedPreferences(Constants.PREF_FILE_NAME,MODE_PRIVATE);
        binding.inputUserName.setText(preferences.getString(Constants.KEY_USER_NAME,""));
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