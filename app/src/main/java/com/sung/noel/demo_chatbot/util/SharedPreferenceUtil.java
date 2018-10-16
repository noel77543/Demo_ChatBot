package com.sung.noel.demo_chatbot.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String _NAME_USER_PREFERENCE = "UserPreference";
    private final String _IS_FIRST_TIME = "isFirstTime";


    public SharedPreferenceUtil(Context context,String name) {
        sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //------------

    public void setIsFirstTime(boolean isFirstTime){
        editor.putBoolean(_IS_FIRST_TIME,isFirstTime).commit();
    }

    //--------------

    public boolean isIsFirstTime(){
        return sharedPreferences.getBoolean(_IS_FIRST_TIME,true);
    }

}
