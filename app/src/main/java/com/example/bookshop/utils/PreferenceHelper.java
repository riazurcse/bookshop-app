package com.example.bookshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookshop.common.Constants;

public class PreferenceHelper {

    private Context context;

    public PreferenceHelper(){

    }

    public PreferenceHelper(Context context){
        this.context = context;
    }

    public void saveUserInfo(String key, String value){

        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveUserInfo(String key, boolean value){

        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveUserInfo(String key, int value){

        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getUserInfo(String key){

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String desiredValue = prefs.getString(key, null);
        return desiredValue;
    }

    public Boolean getBooleanValue(String key){

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Boolean desiredValue = prefs.getBoolean(key, false);
        return desiredValue;
    }

    public int getIntValue(String key){

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int desiredValue = prefs.getInt(key, 0);
        return desiredValue;
    }

    public void removeUserInfo(String key){

        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.commit();
    }
}
