package com.sung.noel.demo_chatbot.util.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static SQLiteDatabase database;
    public static final int _DB_VERSION = 1;
    public static final String _DB_NAME = "ChatBoardData.db";

    //--------


    public static final String _DB_TABLE_TALK = "Talk";
    public static final String _DB_KEY_SID = "Sid";
    public static final String _DB_KEY_TYPE = "Type";
    public static final String _DB_KEY_MESSAGE = "Message";

    //建立Talk table
    public static final String _DB_CREATE_TABLE_TALK = "CREATE  TABLE " + _DB_TABLE_TALK +
            "(" +
            _DB_KEY_SID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            _DB_KEY_TYPE + " INTEGER , " +
            _DB_KEY_MESSAGE + " TEXT" +
            ");";


    //-----------
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    //------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTalkTable(db);
    }

    //------------

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    //------------

    /***
     * 建立 Talk table
     * @param db
     */
    private void createTalkTable(SQLiteDatabase db) {
        db.execSQL(_DB_CREATE_TABLE_TALK);
    }
    //---------------

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    private static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DataBaseHelper(context, _DB_NAME, null, _DB_VERSION).getWritableDatabase();
        }
        return database;
    }

    //---------------


    /**
     * 取得所有聊天內容並依照sid排序
     *
     * @return
     */
    public ArrayList<Talk> getTalkDatas() {
        SQLiteDatabase db = getDatabase(context);
        String sql = "SELECT * FROM Talk ORDER BY Sid";
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Talk> talks = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Talk talk = new Talk();
                talk.fromCursor(c);
                talks.add(talk);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return talks;
    }


    //----------------

    /**
     * 新增資料
     */
    public void insert(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = getDatabase(context);
        try {
            db.insert(tableName, null, contentValues);
        } catch (Exception ex) {
            Log.e("SQLITE", ex.getMessage());
        }
        db.close();
    }


    //--------

    /**
     * 更改第[index]筆資料
     */
    public void update(String tableName, ContentValues contentValues, int index) {
        SQLiteDatabase db = getDatabase(context);
        try {
            db.update(tableName, contentValues, "Sid = ?", new String[]{String.valueOf(index)});
        } catch (Exception ex) {
            Log.e("SQLITE", ex.getMessage());
        }
        db.close();
    }

    //--------

    /**
     * 清除 所有Talk table中的資料
     */
    public void deleteTalk() {

        SQLiteDatabase db = getDatabase(context);
        try {
            db.execSQL("DELETE FROM `Talk`");
        } catch (Exception ex) {
            Log.e("SQLITE", ex.getMessage());
        }
        db.close();
    }
}
