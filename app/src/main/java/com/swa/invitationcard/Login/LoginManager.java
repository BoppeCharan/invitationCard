package com.swa.invitationcard.Login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginManager {
    private static LoginManager instance = null;
    public Context context;
    boolean isLoggedIn;
    String ownerPhoneNumber;
    private static final String Shared_Pref_Name = "loginPref";
    private static final String key_PhnNo = "phoneNo";
    private static final String key_isLoggedIn = "isLoggedIn";
    private  static SharedPreferences sharedPreferences;

    public LoginManager(){}

    public LoginManager(Context context){
        this.context = context;
    }

    public static LoginManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new LoginManager(context);
        }

        sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,MODE_PRIVATE);
        return instance;
    }

    public boolean isUserLoggedIn()
    {
        isLoggedIn = sharedPreferences.getBoolean(key_isLoggedIn,false);
        return isLoggedIn;
    }

    public void login(String phoneNumber){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_PhnNo,phoneNumber);
        editor.putBoolean(key_isLoggedIn, true);
        editor.apply();
    }

    public String getUserPhoneNumber() {

        if (ownerPhoneNumber == null) {
            ownerPhoneNumber = sharedPreferences.getString(key_PhnNo,null);
        }
        return ownerPhoneNumber;
    }

    public void logOut(){
        ownerPhoneNumber = null;
        isLoggedIn = false;
        eraseSharedPref();
    }
    private void eraseSharedPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

