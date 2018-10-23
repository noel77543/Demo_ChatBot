package com.sung.noel.demo_chatbot.Talk.model;

import android.support.annotation.IntDef;

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
    private String text;
    private String date;

    public int getType() {
        return type;
    }

    public void setType(@HistoryType int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
