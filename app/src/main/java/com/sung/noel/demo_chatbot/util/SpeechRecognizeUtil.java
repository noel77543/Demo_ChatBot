package com.sung.noel.demo_chatbot.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.R;

import java.util.ArrayList;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;

public class SpeechRecognizeUtil implements RecognitionListener,AIListener {
    private SpeechRecognizer speechRecognizer;
    private OnTextGetFromRecordListener onTextGetFromRecordListener;
    private Context context;

    public SpeechRecognizeUtil(Context context) {
        this.context = context;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);






    }

    //--------------------

    /***
     * 開始錄音
     */
    public void startListen() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.startListening(intent);
    }

    //----------------------

    /***
     *停止錄音
     */
    public void stopListen() {
        speechRecognizer.stopListening();
    }


    //------------------

    /***
     * 移除 SpeechRecognizer
     */
    public void removeRecognizer() {
        speechRecognizer.destroy();
    }


    //--------------------
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

    @Override
    public void onError(int i) {
        Toast.makeText(context, context.getString(R.string.toast_listen_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResults(Bundle bundle) {
        if (onTextGetFromRecordListener != null) {
            onTextGetFromRecordListener.onTextGetFromRecord(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
        }
    }

    //--------------------

    public interface OnTextGetFromRecordListener {
        void onTextGetFromRecord(ArrayList<String> results);
    }

    public void setOnTextGetFromRecordListener(OnTextGetFromRecordListener onTextGetFromRecordListener) {
        this.onTextGetFromRecordListener = onTextGetFromRecordListener;
    }


    //-----------------





    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void onResult(ai.api.model.AIResponse result) {

    }

    @Override
    public void onError(ai.api.model.AIError error) {

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
