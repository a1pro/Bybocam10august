package com.example.bybocam.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.bybocam.Activities.Home;
import com.example.bybocam.Activities.MainActivity;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "pass";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
     
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_IMAGE = "user_image";
     
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void createLoginSession(String pass, String email,String userId,String lat,String lon,String username,String image){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, pass);
         
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        //Storing user ID in pref

        editor.putString(USER_ID,userId);

        editor.putString(KEY_LATITUDE,lat);
        editor.putString(KEY_LONGITUDE,lon);
        editor.putString(USER_NAME,username);
        editor.putString(USER_IMAGE,image);
        // commit changes
        editor.commit();
    }   
     
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Home.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);

        }
        else {
            return false;
        }
         return true;
    }
     
     
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_LATITUDE, pref.getString(KEY_LATITUDE, null));
        user.put(KEY_LONGITUDE, pref.getString(KEY_LONGITUDE, null));
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        //user id
        user.put(USER_ID,pref.getString(USER_ID,null));
        user.put(USER_NAME,pref.getString(USER_NAME,null));
        user.put(USER_IMAGE,pref.getString(USER_IMAGE,null));
        // return user
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context,MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}