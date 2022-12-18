package com.example.safegallery.utils;

public class Constants {
    public static final String DB_NAME="SafeGallery.db";
    public static final String TB_NAME="UsersTable";
    public static final String COL_1="userID";
    public static final String COL_2="userName";
    public static final String COL_3="userDOB";
    public static final String COL_4="userPassword";
    public static final String COL_5="userQuestion";
    public static final String COL_6="userAnswer";
    public static final int DB_VERSION=1;

    public static final String KEY_INTENT_USER="userKey";
    public static final String PREF_FILE_NAME="preferencesFile";
    public static final String USER_EXISTS_KEY="userExists";
    public static final String KEY_USER_NAME="userNameKey";


    public static final String CREATE_USER_TABLE_QUERY=
            "CREATE TABLE "+TB_NAME+" ("+COL_1+" TEXT PRIMARY KEY, "+COL_2+" TEXT, "+
                    COL_3+" TEXT,"+COL_4+" TEXT,"+COL_5+" TEXT,"+COL_6+" TEXT);";
    public static final String DROP_USER_TABLE_QUERY=
            "DROP TABLE IF EXISTS "+TB_NAME+";";

}
