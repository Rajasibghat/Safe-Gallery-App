package com.example.mysafegallery.utils.databasehelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mysafegallery.models.User;
import com.example.mysafegallery.utils.Constants;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class CRUDHelper{
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public CRUDHelper(Context context) {
        this.context = context;
        this.dataBaseHelper = new DataBaseHelper(context);
        this.sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    public long insertUser(User user){
        ContentValues contentValues=new ContentValues(6);
        contentValues.put(Constants.COL_1,user.getUserID());
        contentValues.put(Constants.COL_2,user.getUserName());
        contentValues.put(Constants.COL_3,user.getUserDOB());
        contentValues.put(Constants.COL_4,user.getUserPassword());
        contentValues.put(Constants.COL_5,user.getUserQuestion());
        contentValues.put(Constants.COL_6,user.getUserAnswer());
        return sqLiteDatabase.insert(Constants.TB_NAME,null,contentValues);
    }
    @SuppressLint("Range")
    public List<User> getUsers(){
        List<User> usersList=new ArrayList<>();
        Cursor cursor=sqLiteDatabase.query(Constants.TB_NAME,new String[]{Constants.COL_1,Constants.COL_2,Constants.COL_3,Constants.COL_4,Constants.COL_5,Constants.COL_6}
        ,null,null,null,null,null);
        User user=new User();
        while (cursor.moveToNext()){
            user.setUserID(cursor.getString(cursor.getColumnIndex(Constants.COL_1)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(Constants.COL_2)));
            user.setUserDOB(cursor.getString(cursor.getColumnIndex(Constants.COL_3)));
            user.setUserPassword(cursor.getString(cursor.getColumnIndex(Constants.COL_4)));
            user.setUserQuestion(cursor.getString(cursor.getColumnIndex(Constants.COL_5)));
            user.setUserAnswer(cursor.getString(cursor.getColumnIndex(Constants.COL_6)));
            usersList.add(user);
        }
        cursor.close();
        return usersList;
    }
    @SuppressLint("Range")
    public User getUser(String username, String password){
        User user = new User();
        String[] args=new String[]{username,password};
        Cursor cursor=sqLiteDatabase.query(Constants.TB_NAME,
                new String[]{
                        Constants.COL_1,Constants.COL_2,Constants.COL_3,Constants.COL_4,Constants.COL_5,Constants.COL_6
                },Constants.COL_2+" =? AND "+Constants.COL_4+" =?",args,null,null,null);
        while (cursor.moveToNext()) {
            user.setUserID(cursor.getString(cursor.getColumnIndex(Constants.COL_1)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(Constants.COL_2)));
            user.setUserDOB(cursor.getString(cursor.getColumnIndex(Constants.COL_3)));
            user.setUserPassword(cursor.getString(cursor.getColumnIndex(Constants.COL_4)));
            user.setUserQuestion(cursor.getString(cursor.getColumnIndex(Constants.COL_5)));
            user.setUserAnswer(cursor.getString(cursor.getColumnIndex(Constants.COL_6)));
        }
        cursor.close();
        return user;
    }
    public Boolean checkUserAlreadyExists(String username,String password){
        boolean exists = false;
        String[] args=new String[]{username,password};
        Cursor cursor=sqLiteDatabase.query(Constants.TB_NAME,
                new String[]{
                        Constants.COL_1,Constants.COL_2,Constants.COL_3,Constants.COL_4,Constants.COL_5,Constants.COL_6
                },Constants.COL_2+" =? AND "+Constants.COL_4+" =?",args,null,null,null);
        while (cursor.moveToNext()){
            exists=true;
        }
        cursor.close();
        return exists;
    }
    @SuppressLint("Range")
    public User checkRecordExists(String name,String dob, String question, String answer){
        User user = new User();
        String[] args=new String[]{name,dob,question,answer};
        Cursor cursor=sqLiteDatabase.query(Constants.TB_NAME,
                new String[]{
                        Constants.COL_1,Constants.COL_2,Constants.COL_3,Constants.COL_4,Constants.COL_5,Constants.COL_6
                },
                Constants.COL_2+" =? AND "+Constants.COL_3+" =? AND "+Constants.COL_5+" =? AND "+Constants.COL_6+" =?",
                args,null,null,null);
        while (cursor.moveToNext()){
            user.setUserID(cursor.getString(cursor.getColumnIndex(Constants.COL_1)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(Constants.COL_2)));
            user.setUserDOB(cursor.getString(cursor.getColumnIndex(Constants.COL_3)));
            user.setUserPassword(cursor.getString(cursor.getColumnIndex(Constants.COL_4)));
            user.setUserQuestion(cursor.getString(cursor.getColumnIndex(Constants.COL_5)));
            user.setUserAnswer(cursor.getString(cursor.getColumnIndex(Constants.COL_6)));
        }
        cursor.close();
        return user;
    }
    public int updateUser(User user,String newPassword){
        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.COL_4,newPassword);
        return sqLiteDatabase.update(Constants.TB_NAME,contentValues,Constants.COL_4+" =?",new String[]{user.getUserPassword()});
    }


    public void openDataBase(){
        sqLiteDatabase=dataBaseHelper.getWritableDatabase();
    }

    public void closeDataBase(){
        sqLiteDatabase.close();
    }
}
