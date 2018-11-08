package com.sung.noel.demo_chatbot.util.ai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.sung.noel.demo_chatbot.connect.ConnectInfo;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class AIActionUtil {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_EVENT_SEARCH, _EVENT_CALL})
    //在Dialogflow consloe 定義的意圖
    public @interface DialogflowEvents {
    }

    //查詢
    public static final String _EVENT_SEARCH = "Search";
    //播號
    public static final String _EVENT_CALL = "Call";


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({_STATE_PREPARE, _STATE_CONNECTING, _STATE_CONNECTED})
    public @interface DialogflowState {
    }

    //正要發起連線
    public static final int _STATE_PREPARE = 11;
    //連線處理中
    public static final int _STATE_CONNECTING = 12;
    //完成 並接收回覆
    public static final int _STATE_CONNECTED = 13;

    private Context context;

    public AIActionUtil(Context context) {
        this.context = context;
    }
    //---------------------

    /***
     * 開啟瀏覽器 搜尋關鍵字
     */
    public void searchOnBrowser(String text) {
        try {
            String escapedQuery = URLEncoder.encode(text, "UTF-8");
            Uri uri = Uri.parse(MessageFormat.format(ConnectInfo.URL_GOOGLE_SEARCH,escapedQuery));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }

    }
}
