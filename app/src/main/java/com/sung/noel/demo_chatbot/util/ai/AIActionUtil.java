package com.sung.noel.demo_chatbot.util.ai;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AIActionUtil {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_EVENT_SEARCH, _EVENT_CALL})
    public @interface DialogflowEvents {
    }

    //查詢
    public static final String _EVENT_SEARCH = "Search";
    //播號
    public static final String _EVENT_CALL = "Call";


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({_STATE_PREPARE,_STATE_CONNECTING,_STATE_CONNECTED})
    public @interface DialogflowState {
    }

    //正要發起連線
    public static final int _STATE_PREPARE = 11;
    //連線處理中
    public static final int _STATE_CONNECTING = 12;
    //完成 並接收回覆
    public static final int _STATE_CONNECTED = 13;


    //---------------------


}
