package com.example.safegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.example.safegallery.databinding.ActivityNewUserBinding;
import com.example.safegallery.utils.PermissionsHelper;
import com.example.safegallery.utils.SpinnerDataProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NewUserActivity extends AppCompatActivity {
    private ActivityNewUserBinding binding;
    private Calendar calendarDate=Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(PermissionsHelper.checkReadPermission(this) && PermissionsHelper.checkWritePermission(this))){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        }
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

                }
            }
        });






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

    private void updateInputDOB(){
        String formatString="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(formatString, Locale.US);
        binding.inputDOB.setText(dateFormat.format(calendarDate.getTime()));
    }
}