package com.sung.noel.demo_chatbot.bubble;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.sung.noel.demo_chatbot.MainActivity;
import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.util.notification.BubbleNotification;
import com.sung.noel.demo_chatbot.util.view.FlowerButton;


public class BubbleService extends Service implements FlowerButton.OnMainButtonSwipeListener {
    private final int _VIEW_SIZE = 150;

    private WindowManager windowManager;
    private BubbleNotification bubbleNotification;
    private WindowManager.LayoutParams params;
    private boolean isAdded = false;
    private FlowerButton flowerButton;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //--------


    @Override
    public void onCreate() {
        super.onCreate();
        bubbleNotification = new BubbleNotification(this, MainActivity.class, null);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        initFlowerButton();
        initFloatWindow();
    }

    //----------

    /***
     * FlowerButton初始設定
     */
    private void initFlowerButton() {
        flowerButton = new FlowerButton(getApplicationContext());
        flowerButton.setOnMainButtonSwipeListener(this);
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
                        windowManager.addView(flowerButton, params);
                    }
                    bubbleNotification.displayNotification(R.drawable.ic_bot);
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    //----------

    @Override
    public void onDestroy() {
        super.onDestroy();
        bubbleNotification.removeNotification();
        if (flowerButton.getParent() != null) {
            windowManager.removeView(flowerButton);//移除窗口
        }
    }
    //----------

    /***
     * 當主紐被拖拉
     */
    @Override
    public void onMainButtonSwipe(int x, int y) {

        params.x = x;
        params.y = y;
        windowManager.updateViewLayout(flowerButton, params);
    }
}
