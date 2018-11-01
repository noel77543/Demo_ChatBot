package com.sung.noel.demo_chatbot.util.ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.SharedPreferenceUtil;
import com.sung.noel.demo_chatbot.util.TimeUtil;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import ai.api.model.AIResponse;
import ai.api.model.Result;

public class AIRecognizeUtil implements RecognitionListener, AIAsyncTask.OnConnectToDialogflowListener {

    private OnTextGetFromRecordListener onTextGetFromRecordListener;

    private AIActionUtil aiActionUtil;
    private Context context;
    private SpeechRecognizer speechRecognizer;
    //是否正在聆聽
    private boolean isListening = false;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public AIRecognizeUtil(Context context) {
        this.context = context;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);
        aiActionUtil = new AIActionUtil(context);
        sharedPreferenceUtil = new SharedPreferenceUtil(context, SharedPreferenceUtil._USER_DEFAULT_NAME);
    }

    //-------------

    /***
     * 開始聆聽
     */
    public void startListen() {
        if (!isListening) {
            isListening = true;
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizer.startListening(intent);
        } else {
            Toast.makeText(context, context.getString(R.string.toast_speech_listening), Toast.LENGTH_SHORT).show();
        }
    }
    //-------------

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float v) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    public void onEndOfSpeech() {
        isListening = false;
    }
    //--------------------

    /***
     * 當無法成功轉譯
     * @param i
     */
    @Override
    public void onError(int i) {
        isListening = false;
        connectToDialogflow("error");
    }

    //--------------------

    /***
     * 當成功語音轉文字
     * @param bundle
     */
    @Override
    public void onResults(Bundle bundle) {
        String text = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        Talk talk = new Talk();
        talk.setText(text);
        talk.setType(Talk._TYPE_USER);

        sharedPreferenceUtil.addTalk(talk);
        connectToDialogflow(text);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    //--------------------

    /***
     * 取得Dialogflow回傳
     */
    @Override
    public void onRespondFromDialogflow(AIResponse aiResponse) {

        Result result = aiResponse.getResult();
//        // Get parameters
//        String parameterString = "";
//        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
//            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
//                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
//            }
//        }

        @AIActionUtil.DialogflowEvents
        String event = result.getMetadata().getIntentName();
        switch (event) {
            //查詢
            case AIActionUtil._EVENT_SEARCH:
                aiActionUtil.searchOnBrowser(result.getResolvedQuery());
                break;
            //播號
            case AIActionUtil._EVENT_CALL:
                break;
        }


        Talk talk = new Talk();
        talk.setText(result.getFulfillment().getSpeech());
        talk.setType(Talk._TYPE_BOT);
        sharedPreferenceUtil.addTalk(talk);

        if (onTextGetFromRecordListener != null) {
            onTextGetFromRecordListener.onTextGetFromRecord(result.getFulfillment().getSpeech());
        }
    }

    //--------------------

    /***
     * 釋放資源
     */
    public void destroy() {
        speechRecognizer.destroy();
    }
    //-------------------

    /***
     * 發起連線至dialogflow
     */
    public void connectToDialogflow(String text) {
        new AIAsyncTask(context).setOnConnectToDialogflowListener(this).setQuery(text).execute();
    }

    //--------------------

    public interface OnTextGetFromRecordListener {
        void onTextGetFromRecord(String results);
    }

    public void setOnTextGetFromRecordListener(OnTextGetFromRecordListener onTextGetFromRecordListener) {
        this.onTextGetFromRecordListener = onTextGetFromRecordListener;
    }
}
