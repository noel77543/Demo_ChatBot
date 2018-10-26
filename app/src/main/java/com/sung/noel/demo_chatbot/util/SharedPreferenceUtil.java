package com.sung.noel.demo_chatbot.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class SharedPreferenceUtil {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_USER_DEFAULT_NAME})
    public @interface SharePreferenceName {
    }

    public final static String _USER_DEFAULT_NAME = "UserDefault";


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_USER_DEFAULT_SEARCH_HISTORY})
    public @interface SharedPreferenceKey {
    }

    public final static String _USER_DEFAULT_SEARCH_HISTORY = "SearchHistory";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    public SharedPreferenceUtil(Context context, @SharePreferenceName String name) {
        this.context = context;
        gson = new Gson();
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    //---------------

    /***
     * 加入新紀錄
     */
    public void addTalk(Talk talk) {
        ArrayList<Talk> talks = getTalks();
        talks.add(talk);
        editor.putString(_USER_DEFAULT_SEARCH_HISTORY, gson.toJson(talks)).commit();
    }


    //---------------

    /***
     * 取得所有記錄
     */
    public ArrayList<Talk> getTalks() {
        return gson.fromJson(sharedPreferences.getString(_USER_DEFAULT_SEARCH_HISTORY, gson.toJson(new ArrayList<Talk>())), new TypeToken<ArrayList<Talk>>() {
        }.getType());
    }
    //---------------

    /***
     * 清除所有記錄
     */
    public void clearTalks() {
        editor.putString(_USER_DEFAULT_SEARCH_HISTORY, gson.toJson(new ArrayList<Talk>())).commit();
    }


}
