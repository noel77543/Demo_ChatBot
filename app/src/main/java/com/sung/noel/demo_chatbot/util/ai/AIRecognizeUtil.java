package com.sung.noel.demo_chatbot.util.ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.TimeUtil;
import com.sung.noel.demo_chatbot.util.data.DataBaseHelper;
import com.sung.noel.demo_chatbot.util.window.talk.model.Talk;

import ai.api.model.AIResponse;
import ai.api.model.Result;

public class AIRecognizeUtil implements RecognitionListener, AIAsyncTask.OnConnectToDialogflowListener {

    private OnTextGetFromRecordListener onTextGetFromRecordListener;
    private OnDialogflowConnectStateChangeListener onDialogflowConnectStateChangeListener;

    private AIActionUtil aiActionUtil;
    private Context context;
    private SpeechRecognizer speechRecognizer;
    //是否正在聆聽
    private boolean isListening = false;
    private DataBaseHelper dataBaseHelper;

    public AIRecognizeUtil(Context context) {
        this.context = context;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);
        aiActionUtil = new AIActionUtil(context);
        dataBaseHelper = new DataBaseHelper(context, DataBaseHelper._DB_NAME, null, DataBaseHelper._DB_VERSION);
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
        addData(text,Talk._TYPE_USER);
//        sharedPreferenceUtil.addTalk(talk);
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
     * dialogflow 連線狀態
     * @param state
     */
    @Override
    public void onDialogflowConnectStateChanged(int state) {
        onDialogflowConnectStateChangeListener.onDialogflowConnectStateChanged(state);
    }
    //--------------------

    /***
     * 取得Dialogflow回傳
     */
    @Override
    public void onRespondFromDialogflow(AIResponse aiResponse) {

        Result result = aiResponse.getResult();
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

        addData(result.getFulfillment().getSpeech(),Talk._TYPE_BOT);
        if (onTextGetFromRecordListener != null) {
            onTextGetFromRecordListener.onTextGetFromRecord(result.getFulfillment().getSpeech());
        }
    }

    //------------------

    /***
     * 加入資料庫
     */
    private void addData(String message, @Talk.HistoryType int type){
        Talk talk = new Talk();
        talk.setMessage(message);
        talk.setType(type);
        dataBaseHelper.insert(DataBaseHelper._DB_TABLE_TALK,talk.toContentValues());
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
    //--------------------
    public interface OnDialogflowConnectStateChangeListener{
        void onDialogflowConnectStateChanged(@AIAsyncTask.DialogflowConnectState int state);
    }
    public void setOnDialogflowConnectStateChangeListener(OnDialogflowConnectStateChangeListener onDialogflowConnectStateChangeListener) {
        this.onDialogflowConnectStateChangeListener = onDialogflowConnectStateChangeListener;
    }
}
