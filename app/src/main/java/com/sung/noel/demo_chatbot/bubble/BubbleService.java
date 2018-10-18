package com.sung.noel.demo_chatbot.bubble;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.MainActivity;
import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.AIRecognizeUtil;
import com.sung.noel.demo_chatbot.util.TextToSpeechUtil;
import com.sung.noel.demo_chatbot.util.notification.BubbleNotification;
import com.sung.noel.demo_chatbot.util.view.CustomButton;

import java.util.ArrayList;


public class BubbleService extends Service implements CustomButton.OnMainButtonSwipeListener, CustomButton.OnMainButtonLongClickListener, AIRecognizeUtil.OnTextGetFromRecordListener {
    private final int _VIEW_SIZE = 150;

    private WindowManager windowManager;
    private BubbleNotification bubbleNotification;
    private WindowManager.LayoutParams params;
    private boolean isAdded = false;
    private CustomButton customButton;
    private AIRecognizeUtil AIRecognizeUtil;
    private TextToSpeechUtil textToSpeechUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //--------


    @Override
    public void onCreate() {
        super.onCreate();
        textToSpeechUtil = new TextToSpeechUtil(this);
        AIRecognizeUtil = new AIRecognizeUtil(this);
        AIRecognizeUtil.setOnTextGetFromRecordListener(this);
        bubbleNotification = new BubbleNotification(this, MainActivity.class, null);
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        initCustomButton();
        initFloatWindow();
    }
    //----------

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //垂直
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            customButton.setOrientationChanged(false);
        }
        //水平
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            customButton.setOrientationChanged(true);
        }
    }

    //----------

    /***
     * FlowerButton初始設定
     */
    private void initCustomButton() {
        customButton = new CustomButton(getApplicationContext());
        customButton.setOnMainButtonSwipeListener(this);
        customButton.setOnMainButtonLongClickListener(this);
    }
    //----------

    /***
     * 懸浮視窗初始設定
     */
    private void initFloatWindow() {
        params = new WindowManager.LayoutParams();
        params.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 支持透明
        params.format = PixelFormat.TRANSLUCENT;
        //焦點
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = _VIEW_SIZE;
        params.height = _VIEW_SIZE;

        //起始點偏移量  中心點為(0,0)
        params.x = 0;
        params.y = 0;
    }


    //----------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            int action = intent.getIntExtra(BubbleNotification.KEY_ACTION, BubbleNotification.VALUE_NONE);
            switch (action) {
                case BubbleNotification.VALUE_CLOSE:
                    stopSelf();
                    bubbleNotification.removeNotification();
                    break;
                case BubbleNotification.VALUE_START:
                    if (!isAdded) {
                        isAdded = true;
                        windowManager.addView(customButton, params);
                        bubbleNotification.displayNotification(R.drawable.ic_bot);
                    }
                    break;
            }
        }
        return START_STICKY;
    }
    //----------

    @Override
    public void onDestroy() {
        super.onDestroy();
        bubbleNotification.removeNotification();
        textToSpeechUtil.shutDown();
        //移除窗口
        if (customButton.getParent() != null) {
            windowManager.removeView(customButton);
            isAdded=false;
        }
    }

    //----------

    /***
     * 當被拖拉
     */
    @Override
    public void onMainButtonSwipe(int x, int y) {
        params.x = x;
        params.y = y;
        windowManager.updateViewLayout(customButton, params);
    }

    //------------

    /***
     * 當長按 開啟語音辨識
     */
    @Override
    public void onMainButtonLongClicked() {
        AIRecognizeUtil.startListen();
        Toast.makeText(this, getString(R.string.toast_listen), Toast.LENGTH_SHORT).show();
    }

    //------------

    /***
     * 當 語音轉文字 取得
     * @param results
     */
    @Override
    public void onTextGetFromRecord(String results) {
        textToSpeechUtil.speak(results);
    }
}
