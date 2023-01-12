package com.example.safegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.safegallery.databinding.ActivityResetPasswordBinding;
import com.example.safegallery.models.User;
import com.example.safegallery.utils.SpinnerDataProvider;
import com.example.safegallery.utils.databasehelper.CRUDHelper;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    private CRUDHelper crudHelper;
    private Calendar calendarDate=Calendar.getInstance();
    private User user;

    // comment1
    // comment2
    // comment3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getResources().getString(R.string.actionBar_text_reset_password_screen));
        crudHelper=new CRUDHelper(this);
        ArrayList<String> list= SpinnerDataProvider.getSpinnerData(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        binding.spinnerSecurityQuestion.setAdapter(adapter);

        Toast.makeText(this, "Test feature", Toast.LENGTH_SHORT).show();

        binding.btnVerifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validUser()){
                    String name=binding.inputUserName.getText().toString();
                    String dob=binding.inputDOB.getText().toString();
                    String question=binding.spinnerSecurityQuestion.getText().toString();
                    String answer=binding.inputAnswer.getText().toString();
                    user=crudHelper.checkRecordExists(name,dob,question,answer);
                    if(user!=null){
                        binding.inputLayoutNewPassword.setVisibility(View.VISIBLE);
                        binding.btnChangePassword.setVisibility(View.VISIBLE);
                    }else{
                        Snackbar.make(v,"User doesn't exists",Snackbar.LENGTH_LONG).show();
                        binding.inputLayoutNewPassword.setVisibility(View.GONE);
                        binding.btnChangePassword.setVisibility(View.GONE);
                    }
                }
            }
        });
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword=binding.inputNewPassword.getText().toString();
                if(newPassword.trim().length()==0){
                    binding.inputLayoutNewPassword.setError("Password is empty");
                    binding.inputLayoutNewPassword.requestFocus();
                    return;
                }else if(newPassword.length()<8){
                    binding.inputLayoutNewPassword.setError("Password too short");
                    binding.inputLayoutNewPassword.requestFocus();
                    return;
                }else{
                    int result=crudHelper.updateUser(user,newPassword);
                    if(result==0){
                        Toast.makeText(ResetPasswordActivity.this, "Unable to update Password", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ResetPasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
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
                new DatePickerDialog(ResetPasswordActivity.this,
                        listener,calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),
                        calendarDate.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });







    }



    private Boolean validUser() {
        boolean validity;
        String userName=binding.inputUserName.getText().toString();
        String userDOB=binding.inputDOB.getText().toString();
        String userQuestion=binding.spinnerSecurityQuestion.getText().toString();
        String answer=binding.inputAnswer.getText().toString();
        if(userName.trim().length()==0){
            binding.inputUserName.setError("User name cannot be empty");
            binding.inputUserName.requestFocus();
            validity=false;
        }else if(userDOB.trim().length()==0){
            binding.inputDOB.setError("User dob is empty");
            binding.inputDOB.requestFocus();;
            validity= false;
        }else if(userQuestion.equals(getResources().getString(R.string.user_security_text))){
            binding.spinnerSecurityQuestion.setError("Select a security question");
            binding.spinnerSecurityQuestion.requestFocus();
            validity= false;
        }else if(answer.trim().length()==0){
            binding.inputAnswer.setError("Answer is empty");
            binding.inputAnswer.requestFocus();;
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