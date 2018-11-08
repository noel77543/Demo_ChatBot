package com.sung.noel.demo_chatbot.util.window.talk.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.IntDef;

import com.sung.noel.demo_chatbot.util.data.DataBaseHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Talk {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({_TYPE_BOT, _TYPE_USER})
    public @interface HistoryType {
    }

    public static final int _TYPE_BOT = 77;
    public static final int _TYPE_USER = 78;
    private @HistoryType
    int type;
    private String message;
    private int sid;

    public int getType() {
        return type;
    }

    public void setType(@HistoryType int type) {
        this.type = type;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //-----------

    /***
     * 資料取出
     * @param c
     */
    public void fromCursor(Cursor c) {
        sid = c.getInt(c.getColumnIndex(DataBaseHelper._DB_KEY_SID));
        type = c.getInt(c.getColumnIndex(DataBaseHelper._DB_KEY_TYPE));
        message = c.getString(c.getColumnIndex(DataBaseHelper._DB_KEY_MESSAGE));
    }
    //-----------

    /***
     * 資料存入
     * @return
     */
    public ContentValues toContentValues() {
        ContentValues ret = new ContentValues();
        ret.put(DataBaseHelper._DB_KEY_TYPE, getType());
        ret.put(DataBaseHelper._DB_KEY_MESSAGE, getMessage());
        return ret;
    }
}
