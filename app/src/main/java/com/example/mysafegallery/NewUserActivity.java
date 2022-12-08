package com.example.mysafegallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TintInfo;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.mysafegallery.databinding.ActivityNewUserBinding;
import com.example.mysafegallery.models.User;
import com.example.mysafegallery.utils.Constants;
import com.example.mysafegallery.utils.HelperClass;
import com.example.mysafegallery.utils.SpinnerDataProvider;
import com.example.mysafegallery.utils.databasehelper.CRUDHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NewUserActivity extends AppCompatActivity {
    private ActivityNewUserBinding binding;
    private CRUDHelper crudHelper;
    private Calendar calendarDate=Calendar.getInstance();
    private ProgressDialog dialog;
    private SharedPreferences.Editor preferencesEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getResources().getString(R.string.actionBar_text_create_account_screen));
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(HelperClass.checkReadPermission(this) && HelperClass.checkWritePermission(this))){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
        initViews();
        ArrayList<String> list= SpinnerDataProvider.getSpinnerData(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        binding.spinnerSecurityQuestion.setAdapter(adapter);
        binding.spinnerSecurityQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(binding.spinnerSecurityQuestion.getText().toString().equals(getResources().getString(R.string.user_security_text))){
                    binding.inputLayoutAnswer.setVisibility(View.GONE);
                }else{
                    binding.inputLayoutAnswer.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validUser()){
                    User user=new User(binding.inputUserName.getText().toString(),
                            binding.inputDOB.getText().toString(),
                            binding.inputPassword.getText().toString(),
                            binding.spinnerSecurityQuestion.getText().toString(),
                            binding.inputAnswer.getText().toString()
                    );
                    user.setUserID(UUID.randomUUID().toString());
                    dialog.show();
                    if(!crudHelper.checkUserAlreadyExists(user.getUserName(),user.getUserPassword())){
                        crudHelper.insertUser(user);
                        preferencesEditor.putBoolean(Constants.USER_EXISTS_KEY,true);
                        preferencesEditor.putString(Constants.KEY_USER_NAME,user.getUserName());
                        preferencesEditor.apply();
                        Intent intent=new Intent(NewUserActivity.this,MainActivity.class);
                        intent.putExtra(Constants.KEY_INTENT_USER,user);
                        startActivity(intent);
                        finish();
                    }
                        dialog.hide();
                }else{
                    Toast.makeText(NewUserActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.inputDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener =new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       calendarDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                       calendarDate.set(Calendar.MONTH,month);
                       calendarDate.set(Calendar.YEAR,year);
                       updateInputDOB();
                    }
                };
                new DatePickerDialog(NewUserActivity.this,
                        listener,calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),
                        calendarDate.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

    }

    private void initViews() {
        dialog=new ProgressDialog(this);
        dialog.setMessage("Creating User");
        dialog.setCancelable(false);
        crudHelper= new CRUDHelper(this);
        crudHelper.openDataBase();
        preferencesEditor=getSharedPreferences(Constants.PREF_FILE_NAME,MODE_PRIVATE).edit();
    }

    private void updateInputDOB(){
        String formatString="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(formatString, Locale.US);
        binding.inputDOB.setText(dateFormat.format(calendarDate.getTime()));
    }

    private Boolean validUser() {
        boolean validity;
        String userName=binding.inputUserName.getText().toString();
        String userDOB=binding.inputDOB.getText().toString();
        String userPassword=binding.inputPassword.getText().toString();
        String userQuestion=binding.spinnerSecurityQuestion.getText().toString();
        String userAnswer=binding.inputAnswer.getText().toString();
        if(userName.trim().length()==0){
            binding.inputUserName.setError("Username is empty");
            binding.inputUserName.requestFocus();
            validity= false;
        }else if(userName.length()<4){
            binding.inputUserName.setError("Username too short");
            binding.inputUserName.requestFocus();
            validity= false;
        }else if(userDOB.trim().length()==0){
            binding.inputDOB.setError("User dob is empty");
            binding.inputDOB.requestFocus();;
            validity= false;
        }else if(userPassword.trim().length()==0){
            binding.inputPassword.setError("Password cannot be empty");
            binding.inputPassword.requestFocus();
            validity= false;
        }else if(userPassword.length()<8){
            binding.inputPassword.setError("Password must be 8 characters long");
            binding.inputPassword.requestFocus();
            validity= false;
        }else if(userQuestion.equals(getResources().getString(R.string.user_security_text))){
            binding.spinnerSecurityQuestion.setError("Select a security question");
            binding.spinnerSecurityQuestion.requestFocus();
            validity= false;
        }else if(userAnswer.trim().length()==0){
            binding.inputAnswer.setError("Answer must not be empty");
            binding.inputAnswer.requestFocus();
            validity= false;
        }else{
            validity= true;
        }
        return validity;
    }
}