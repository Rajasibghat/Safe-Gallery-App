package com.example.safegallery.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userID,userName,userDOB,userPassword,userQuestion,userAnswer;

    public User() {
    }

    public User(String userName, String userDOB, String userPassword, String userQuestion, String userAnswer) {
        this.userName = userName;
        this.userDOB = userDOB;
        this.userPassword = userPassword;
        this.userQuestion = userQuestion;
        this.userAnswer = userAnswer;
    }

    protected User(Parcel in) {
        userID = in.readString();
        userName = in.readString();
        userDOB = in.readString();
        userPassword = in.readString();
        userQuestion = in.readString();
        userAnswer = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserQuestion() {
        return userQuestion;
    }

    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(userDOB);
        dest.writeString(userPassword);
        dest.writeString(userQuestion);
        dest.writeString(userAnswer);
    }
}
