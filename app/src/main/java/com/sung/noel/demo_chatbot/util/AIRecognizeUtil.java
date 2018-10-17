package com.sung.noel.demo_chatbot.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.sung.noel.demo_chatbot.R;

import java.util.ArrayList;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.Result;

public class AIRecognizeUtil implements AIListener {
    private OnTextGetFromRecordListener onTextGetFromRecordListener;
    private Context context;

    private AIService aiService;

    public AIRecognizeUtil(Context context) {
        this.context = context;
        final AIConfiguration config = new AIConfiguration(context.getString(R.string.client_access_token),
                AIConfiguration.SupportedLanguages.ChineseTaiwan,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(context, config);
        aiService.setListener(this);

    }

    public void startListen() {
        aiService.startListening();
    }

    //--------------------

    public interface OnTextGetFromRecordListener {
        void onTextGetFromRecord(String results);
    }

    public void setOnTextGetFromRecordListener(OnTextGetFromRecordListener onTextGetFromRecordListener) {
        this.onTextGetFromRecordListener = onTextGetFromRecordListener;
    }


    //-----------------



    @Override
    public void onResult(ai.api.model.AIResponse response) {

        Result result = response.getResult();

//        // Get parameters
//        String parameterString = "";
//        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
//            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
//                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
//            }
//        }
//        Log.e("TTT",);


        if (onTextGetFromRecordListener != null) {
            onTextGetFromRecordListener.onTextGetFromRecord(result.getFulfillment().getSpeech());
        }
    }

    @Override
    public void onError(ai.api.model.AIError error) {
        Toast.makeText(context, context.getString(R.string.toast_listen_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
