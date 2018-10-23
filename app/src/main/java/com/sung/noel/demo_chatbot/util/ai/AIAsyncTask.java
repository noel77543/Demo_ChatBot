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
        onConnectToDialogflowListener.onStartToConnectDialogflow();
    }

    //--------------------

    @Override
    protected AIResponse doInBackground(AIRequest... aiRequests) {
        onConnectToDialogflowListener.onConnectingDialogflow();
        try {
            return  aiDataService.request(aiRequest);
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
    public void setQuery(String queryText) {
        aiRequest.setQuery(queryText);
    }


    //-------------------------

    public interface OnConnectToDialogflowListener {
        void onStartToConnectDialogflow();

        void onConnectingDialogflow();

        void onRespondFromDialogflow(AIResponse aiResponse);
    }

    public void setOnConnectToDialogflowListener(OnConnectToDialogflowListener onConnectToDialogflowListener) {
        this.onConnectToDialogflowListener = onConnectToDialogflowListener;
    }
}
