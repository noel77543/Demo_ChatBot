package com.sung.noel.demo_chatbot.util.ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.R;

import ai.api.model.AIResponse;
import ai.api.model.Result;

public class AIRecognizeUtil implements RecognitionListener, AIAsyncTask.OnConnectToDialogflowListener {


    private OnTextGetFromRecordListener onTextGetFromRecordListener;
    private Context context;
    private SpeechRecognizer speechRecognizer;
    private AIAsyncTask aiAsyncTask;

    public AIRecognizeUtil(Context context) {
        this.context = context;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);
        aiAsyncTask = new AIAsyncTask(context);
        aiAsyncTask.setOnConnectToDialogflowListener(this);
    }

    //-------------

    /***
     * 開始聆聽
     */
    public void startListen() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.startListening(intent);
    }

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

    }
    //--------------------

    /***
     * 當無法成功轉譯
     * @param i
     */
    @Override
    public void onError(int i) {
        onTextGetFromRecordListener.onTextGetFromRecord(context.getString(R.string.toast_listen_error));
        Toast.makeText(context, context.getString(R.string.toast_listen_error), Toast.LENGTH_SHORT).show();
    }
    //--------------------

    /***
     * 當成功語音轉文字
     * @param bundle
     */
    @Override
    public void onResults(Bundle bundle) {
        String text = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        aiAsyncTask.setQuery(text);
        aiAsyncTask.execute();
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
    //--------------------

    /***
     * 準備連線至Dialogflow
     */

    @Override
    public void onStartToConnectDialogflow() {

    }
    //--------------------

    /***
     * 現正連線至Dialogflow
     */
    @Override
    public void onConnectingDialogflow() {

    }
    //--------------------

    /***
     * 已連線至Dialogflow
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
                break;
            //播號
            case AIActionUtil._EVENT_CALL:
                break;
        }
        if (onTextGetFromRecordListener != null) {
            onTextGetFromRecordListener.onTextGetFromRecord(result.getFulfillment().getSpeech());
        }
    }

    //--------------------

    public interface OnTextGetFromRecordListener {
        void onTextGetFromRecord(String results);
    }

    public void setOnTextGetFromRecordListener(OnTextGetFromRecordListener onTextGetFromRecordListener) {
        this.onTextGetFromRecordListener = onTextGetFromRecordListener;
    }

}
