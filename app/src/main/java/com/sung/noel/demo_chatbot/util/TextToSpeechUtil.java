package com.sung.noel.demo_chatbot.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.R;

import java.util.Locale;

public class TextToSpeechUtil implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private Context context;
    //音調
    private final float PITCH = 1.5f;
    //語速
    private final float RATE = 1.5f;


    public TextToSpeechUtil(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, this);
    }

    //---------------
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.TAIWAN);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, context.getString(R.string.toast_speech_error), Toast.LENGTH_SHORT).show();
            } else {
                if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                    textToSpeech.setPitch(PITCH);
                    textToSpeech.setSpeechRate(RATE);
                }
            }
        }
    }


    //-----------------

    /***
     * 說話
     * @param text
     */
    public void speak(String text) {
        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    //-----------------

    /***
     * 停止
     */
    public void shutDown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
