package com.example.safegallery.utils;

import android.content.Context;

import com.example.safegallery.R;

import java.util.ArrayList;

public class SpinnerDataProvider {
    public static ArrayList<String> getSpinnerData(Context context){
        ArrayList<String> questionsList=new ArrayList<>();
        questionsList.add(context.getResources().getString(R.string.user_security_text));
        questionsList.add(context.getResources().getString(R.string.security_question_1));
        questionsList.add(context.getResources().getString(R.string.security_question_2));
        questionsList.add(context.getResources().getString(R.string.security_question_3));
        questionsList.add(context.getResources().getString(R.string.security_question_4));
        questionsList.add(context.getResources().getString(R.string.security_question_5));
        return questionsList;
    }
}
