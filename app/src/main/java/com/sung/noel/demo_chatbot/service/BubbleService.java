package com.sung.noel.demo_chatbot.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sung.noel.demo_chatbot.MainActivity;
import com.sung.noel.demo_chatbot.R;

import com.sung.noel.demo_chatbot.util.ai.AIRecognizeUtil;
import com.sung.noel.demo_chatbot.util.LayoutSizeUtil;
import com.sung.noel.demo_chatbot.util.ai.TextToSpeechUtil;
import com.sung.noel.demo_chatbot.util.notification.BubbleNotification;
import com.sung.noel.demo_chatbot.util.view.CustomButton;
import com.sung.noel.demo_chatbot.util.window.talk.TalkPopupWindow;


public class BubbleService extends Service implements CustomButton.OnMainButtonSwipeListener, CustomButton.OnMainButtonLongClickListener, CustomButton.OnMainButtonClickListener, PopupWindow.OnDismissListener, AIRecognizeUtil.OnTextGetFromRecordListener {
    private final int _VIEW_SIZE = 150;

    private WindowManager windowManager;
    private BubbleNotification bubbleNotification;
    private WindowManager.LayoutParams params;
    private boolean isAdded = false;
    private CustomButton customButton;
    private AIRecognizeUtil aiRecognizeUtil;
    private TextToSpeechUtil textToSpeechUtil;
    private TalkPopupWindow talkPopupWindow;
    private LayoutSizeUtil layoutSizeUtil;
    private int phoneHeight;
    private int phoneWidth;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //--------


    @Override
    public void onCreate() {
        super.onCreate();
        aiRecognizeUtil = new AIRecognizeUtil(this);
        layoutSizeUtil = new LayoutSizeUtil(this);
        int[] phoneSize = layoutSizeUtil.getPhoneSize();
        phoneHeight = phoneSize[1];
        phoneWidth = phoneSize[0];
        talkPopupWindow = new TalkPopupWindow(this, aiRecognizeUtil, phoneWidth, (int) (phoneHeight - (0.6 * _VIEW_SIZE)), layoutSizeUtil.getKeyboardHeight());
        textToSpeechUtil = new TextToSpeechUtil(this);
        bubbleNotification = new BubbleNotification(this, MainActivity.class, null);
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        talkPopupWindow.setOnDismissListener(this);
        aiRecognizeUtil.setOnTextGetFromRecordListener(this);

        initCustomButton();
        initFloatWindow();
    }
    //----------

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //垂直
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            customButton.setOrientationChanged(false);
        }
        //水平
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            customButton.setOrientationChanged(true);
        }

        //輸入框出現
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Log.e("TTT", "HARDKEYBOARDHIDDEN_NO");
            talkPopupWindow.updateSize(true);
        }
        //輸入框消失
        else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Log.e("TTT", "HARDKEYBOARDHIDDEN_YES");
            talkPopupWindow.updateSize(false);
        }
    }

    //----------

    /***
     * FlowerButton初始設定
     */
    private void initCustomButton() {
        customButton = new CustomButton(getApplicationContext(), phoneHeight, phoneWidth);
        customButton.setOnMainButtonSwipeListener(this);
        customButton.setOnMainButtonLongClickListener(this);
        customButton.setOnMainButtonClickListener(this);
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

        //起始點  (0,0)為螢幕正中心點
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
        aiRecognizeUtil.destroy();
        //移除窗口
        if (customButton.getParent() != null) {
            windowManager.removeView(customButton);
            isAdded = false;
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
     * 當點擊 顯示記錄
     */
    @Override
    public void onMainButtonClicked() {
        customButton.setVisibility(View.INVISIBLE);
        params.width = phoneWidth;
        params.height = phoneHeight;
        windowManager.updateViewLayout(customButton, params);
        talkPopupWindow.showPopupWindow(customButton);
    }


    //------------

    /***
     * 當長按  開啟語音辨識
     */
    @Override
    public void onMainButtonLongClicked() {
        aiRecognizeUtil.startListen();
        Toast.makeText(this, getString(R.string.toast_listen), Toast.LENGTH_SHORT).show();
    }

    //------------

    /***
     * 當對話紀錄window消失
     */
    @Override
    public void onDismiss() {
        customButton.setVisibility(View.VISIBLE);
        params.x = phoneWidth;
        params.y = -(phoneHeight);
        params.width = _VIEW_SIZE;
        params.height = _VIEW_SIZE;
        windowManager.updateViewLayout(customButton, params);
    }
    //------------

    /***
     * 當 語音轉文字 取得
     * @param results
     */
    @Override
    public void onTextGetFromRecord(String results) {
        talkPopupWindow.notifyData();
        textToSpeechUtil.speak(results);
    }
}
