package com.example.safegallery.utils.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.safegallery.models.User;
import com.example.safegallery.utils.Constants;


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

    public void openDataBase(){
        sqLiteDatabase=dataBaseHelper.getWritableDatabase();
    }

    public void closeDataBase(){
        sqLiteDatabase.close();
    }



}
