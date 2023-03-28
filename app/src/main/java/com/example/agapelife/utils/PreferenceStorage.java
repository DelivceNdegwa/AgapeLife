package com.example.agapelife.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceStorage {

    Context context;
    SharedPreferences myFile;
    public static final String PREF_NAME = "com.example.agapelife.SHARED_PREF";
    public static final String USER_NAME = "com.example.agapelife.USER_NAME";
    public static final String FIRST_NAME = "com.example.agapelife.FIRST_NAME";
    public static final String LAST_NAME = "com.example.agapelife.LAST_NAME";
    public static final String USER_IMAGE = "com.example.agapelife.USER_IMAGE";
    public static final String USER_PASSWORD = "com.example.agapelife.USER_USER_PASSWORD";
    public static final String USER_EMAIL = "com.example.agapelife.USER_EMAIL";
    public static final String USER_NUMBER = "com.example.agapelife.USER_NUMBER";
    public static final String IS_LOGGED_IN = "com.example.agapelife.IS_LOGGED_IN";
    public static final String IS_APP_ACTIVE = "com.example.agapelife.IS_PAUSED";
    public static final String IS_DOCTOR = "com.example.agapelife.IS_DOCTOR";
    public static final String ID_NUMBER = "com.example.agapelife.ID_NUMBER";
    public static final String IS_AVAILABLE = "com.example.agapelife.IS_AVAILABLE";
    public static final String DOCTOR_GENDER = "com.example.agapelife.DOCTOR_GENDER";
    public static final String FCM_USER_TOKEN = "com.example.agapelife.FCM_USER_TOKEN";

    public PreferenceStorage(Context context) {
        this.context = context;
        this.myFile = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void writeStringToPreferencesEditor(String storageString, String inputValue){
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putString(storageString, inputValue);
        myEditor.apply();
    }

    public String readStringFromSharedPreferences(String storedString){
        return myFile.getString(storedString, "");
    }

    public String getUserName(){ return readStringFromSharedPreferences(USER_NAME); }

    public String getUserId(){ return readStringFromSharedPreferences(ID_NUMBER); }

    public String getFirstName(){return readStringFromSharedPreferences(FIRST_NAME);}

    public String getLastName(){return readStringFromSharedPreferences(LAST_NAME);}

    public String getFcmUserToken(){
        return readStringFromSharedPreferences(FCM_USER_TOKEN);
    }

    public int getDoctorGender(){ return myFile.getInt(DOCTOR_GENDER, 0); }


    public void saveLoginData(String name, String password) {
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putString(USER_NAME, name);
        myEditor.putString(USER_PASSWORD, password);
        myEditor.apply();
    }

    public void saveUserId(String id){
        writeStringToPreferencesEditor(ID_NUMBER, id);
    }


    public void saveUserDetails(String firstname, String lastname){
        writeStringToPreferencesEditor(FIRST_NAME, firstname);
        writeStringToPreferencesEditor(LAST_NAME, lastname);
    }

    public void saveUserFirstName(String username){
        writeStringToPreferencesEditor(USER_NAME, username);
    }

    public void setFirstName(String firstName){
        writeStringToPreferencesEditor(FIRST_NAME, firstName);
    }

    public void setLastName(String lastName){
        writeStringToPreferencesEditor(LAST_NAME, lastName);
    }

    public void setFcmUserToken(String fcmUserToken){
        writeStringToPreferencesEditor(FCM_USER_TOKEN, fcmUserToken);
    }

    public boolean isDoctor(){
        return myFile.getBoolean(IS_DOCTOR, false);
    }

    public boolean isLoggedIn(){
        return myFile.getBoolean(IS_LOGGED_IN, false);
    }

    public String isAvailable(){
        if(myFile.getBoolean(IS_AVAILABLE, false)){
            return "open";
        }
        else{
            return "closed";
        }
    }

    public boolean isAppActive(){
        return myFile.getBoolean(IS_APP_ACTIVE, false);
    }

    public void setAppActiveStatus(boolean status){
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putBoolean(IS_APP_ACTIVE, status);
        myEditor.apply();
    }

    public boolean isAvailableBool(){
        return myFile.getBoolean(IS_AVAILABLE, false);
    }

    public void setLoggedInStatus(boolean status){

        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putBoolean(IS_LOGGED_IN, status);
        myEditor.apply();
    }

    public void setIsAvailableStatus(boolean status){
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putBoolean(IS_AVAILABLE, status);
        myEditor.apply();
    }

    public void setIsDoctor(boolean status){

        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putBoolean(IS_DOCTOR, status);
        myEditor.apply();
    }

    public void setDoctorGender(int genderChoice){
        SharedPreferences.Editor myEditor = myFile.edit();
        myEditor.putInt(DOCTOR_GENDER, genderChoice);
        myEditor.apply();
    }

    public void setUserId(String id){
        writeStringToPreferencesEditor(ID_NUMBER, id);
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
