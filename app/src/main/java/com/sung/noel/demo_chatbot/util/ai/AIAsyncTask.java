package com.sung.noel.demo_chatbot.util.ai;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.util.Log;

import com.sung.noel.demo_chatbot.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class AIAsyncTask extends AsyncTask<AIRequest, Void, AIResponse> {

    //連線前
    public static final int STATE_PREPARE = 789;
    //連線中
    public static final int STATE_CONNECTING = 790;
    //連線後
    public static final int STATE_FINISH = 791;

    @IntDef({STATE_PREPARE,STATE_CONNECTING,STATE_FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogflowConnectState{}

    private OnConnectToDialogflowListener onConnectToDialogflowListener;
    private AIDataService aiDataService;
    private AIRequest aiRequest;

    public AIAsyncTask(Context context) {
        aiDataService = new AIDataService(new AIConfiguration(context.getString(R.string.client_access_token), AIConfiguration.SupportedLanguages.ChineseTaiwan, AIConfiguration.RecognitionEngine.System));
        aiRequest = new AIRequest();
    }

    //---------------------
    @Override
    protected void onPreExecute() {
        onConnectToDialogflowListener.onDialogflowConnectStateChanged(STATE_PREPARE);
    }

    //--------------------

    @Override
    protected AIResponse doInBackground(AIRequest... aiRequests) {
        onConnectToDialogflowListener.onDialogflowConnectStateChanged(STATE_CONNECTING);
        try {
            return aiDataService.request(aiRequest);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    //--------------------

    @Override
    protected void onPostExecute(AIResponse aiResponse) {
        onConnectToDialogflowListener.onDialogflowConnectStateChanged(STATE_FINISH);
        if (aiResponse != null) {
            onConnectToDialogflowListener.onRespondFromDialogflow(aiResponse);
        }
    }


    //--------------------------

    /***
     * 將語音文字帶入
     * @param queryText
     */
    public AIAsyncTask setQuery(String queryText) {
        aiRequest.setQuery(queryText);
        return this;
    }


    //-------------------------

    public interface OnConnectToDialogflowListener {
        void onDialogflowConnectStateChanged(@DialogflowConnectState int state);
        void onRespondFromDialogflow(AIResponse aiResponse);
    }

    public AIAsyncTask setOnConnectToDialogflowListener(OnConnectToDialogflowListener onConnectToDialogflowListener) {
        this.onConnectToDialogflowListener = onConnectToDialogflowListener;
        return this;
    }
}
