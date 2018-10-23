package com.sung.noel.demo_chatbot.util.ai;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AIActionUtil {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_EVENT_SEARCH,_EVENT_CALL})
    public @interface DialogflowEvents {
    }
    //查詢
    public static final String _EVENT_SEARCH = "Search";
    //播號
    public static final String _EVENT_CALL = "Call";

    //---------------------




}
