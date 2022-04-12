package com.example.agapelife.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PreferenceStorage {

    Context context;
    SharedPreferences myFile;
    public static final String PREF_NAME = "com.example.agapelife.SHARED_PREF";
    public static final String USER_NAME = "com.example.agapelife.USER_NAME";
    public static final String USER_IMAGE = "com.example.agapelife.USER_IMAGE";
    public static final String USER_PASSWORD = "com.example.agapelife.USER_USER_PASSWORD";
    public static final String USER_EMAIL = "com.example.agapelife.USER_EMAIL";
    public static final String USER_NUMBER = "com.example.agapelife.USER_NUMBER";
    public static final String IS_LOGGED_IN = "com.example.agapelife.IS_LOGGED_IN";
    public static final String ID_NUMBER = "com.example.agapelife.ID_NUMBER";

    public PreferenceStorage(Context context) {
        this.context = context;
        this.myFile = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getUserName(){
        return myFile.getString(USER_NAME, "");
    }

    public String getUserId(){ return myFile.getString(ID_NUMBER, ""); }


    public void saveLoginData(String name, String password) {
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putString(USER_NAME, name);
        myEditor.putString(USER_PASSWORD, password);
        myEditor.apply();
    }

    public void saveUserId(String id){
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putString(ID_NUMBER, id);
        myEditor.apply();
        Toast.makeText(context, "ID has been saved", Toast.LENGTH_SHORT).show();
    }


    public boolean isLoggedIn(){
        return myFile.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedInStatus(boolean status){

        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putBoolean(IS_LOGGED_IN, status);
        myEditor.apply();
    }

    public boolean authenticate(String username, String password) {
        String currentName = getUserName();
        String currentPassword = myFile.getString(USER_PASSWORD, "");

        if(currentName.contentEquals(username) && currentPassword.contentEquals(password)){
            return true;
        }
        else{
            return false;
        }
    }
}
