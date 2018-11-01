package com.sung.noel.demo_chatbot.util.ai;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sung.noel.demo_chatbot.R;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class AIAsyncTask extends AsyncTask<AIRequest, Void, AIResponse> {
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
    }

    //--------------------

    @Override
    protected AIResponse doInBackground(AIRequest... aiRequests) {
        try {
            return aiDataService.request(aiRequest);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(AIResponse aiResponse) {
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
        void onRespondFromDialogflow(AIResponse aiResponse);
    }

    public AIAsyncTask setOnConnectToDialogflowListener(OnConnectToDialogflowListener onConnectToDialogflowListener) {
        this.onConnectToDialogflowListener = onConnectToDialogflowListener;
        return this;
    }
}
