package com.sung.noel.demo_chatbot.util.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sung.noel.demo_chatbot.util.AnimationUtil;
import com.sung.noel.demo_chatbot.util.LayoutSizeUtil;


/**
 * Created by noel on 2018/8/4.
 */

public class FlowerButton extends FrameLayout implements Runnable, View.OnClickListener, View.OnTouchListener {

    //當手指抬起  經過自訂時間後 縮小
    private final int _DELAY_SCALE = 1000;
    private int layoutWidth;
    private int layoutHeight;

    private OnMainButtonClickListener onMainButtonClickListener;
    private OnMainButtonSwipeListener onMainButtonSwipeListener;
    private LayoutParams mainButtonParams;
    private BasicButton mainButton;
    private int mainButtonSize;


    private Context context;
    private LayoutSizeUtil layoutSizeUtil;
    private int phoneHeight;
    private int phoneWidth;
    private AnimationUtil animationUtil;
    private Handler handler;
    private Runnable runnable;


    //----------

    public FlowerButton(@NonNull Context context) {
        super(context);
        this.context = context;
        post(this);
    }
    //----------

    public FlowerButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        post(this);
    }
    //----------

    @Override
    public void run() {
        init();
    }

    //----------
    private void init() {
        animationUtil = new AnimationUtil();
        mainButton = new BasicButton(context);
        layoutSizeUtil = new LayoutSizeUtil(context);
        int[] phoneSize = layoutSizeUtil.getPhoneSize();
        phoneHeight = phoneSize[1];
        phoneWidth = phoneSize[0];
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                    animationUtil.hide(FlowerButton.this);
            }
        };
        handler.postDelayed(runnable,_DELAY_SCALE);
        addMainButton();
    }

    //-------

    /***
     *加入主button
     */
    private void addMainButton() {
        layoutWidth = getWidth();
        layoutHeight = getHeight();
        mainButtonSize = layoutWidth < layoutHeight ? layoutWidth : layoutHeight;
        mainButtonParams = new LayoutParams(mainButtonSize, mainButtonSize);
        mainButtonParams.gravity = Gravity.CENTER;
        mainButton.setId(View.generateViewId());
        mainButton.setLayoutParams(mainButtonParams);
        mainButton.setOnClickListener(this);
        mainButton.setOnTouchListener(this);
        addView(mainButton);
    }


    //----------

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getRawX();
        int y = (int) motionEvent.getRawY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(runnable);
                animationUtil.show(this);
                break;
            case MotionEvent.ACTION_MOVE:
                if (onMainButtonSwipeListener != null) {
                    // TODO   WindowManager的(0,0)起始點是定位於螢幕中心
                    // TODO   所以X座標需要減少寬的1/2  Y座標需要減少高的1/2
                    onMainButtonSwipeListener.onMainButtonSwipe(x - (phoneWidth / 2), y - (phoneHeight / 2));
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.postDelayed(runnable,_DELAY_SCALE);
                break;

        }
        return false;
    }

    //----------


    @Override
    public void onClick(View v) {
            if (onMainButtonClickListener != null) {
                onMainButtonClickListener.onMainButtonCLicked();
            }
    }



    //--------

    /***
     * 主紐設置文字
     */
    public void setText(String text) {
        mainButton.setText(text);
    }

    /***
     * 主紐設置文字  子紐設置文字
     */
    public void setText(String text, String[] textArray) {
        mainButton.setText(text);
    }
    //-----

    /***
     * 主紐設置文字顏色
     */
    public void setTextColor(int colorRes) {
        mainButton.setTextColor(colorRes);
    }
    //--------

    /***
     * 主紐設置顏色
     */
    public void setBackgroundColor(int colorRes) {
        mainButton.setBackgroundColor(colorRes);
    }

    //--------

    /***
     * 主紐設置背景 for drawable
     */
    public void setBackground(Drawable drawable) {
        mainButton.setBackground(drawable);
    }

    //-----

    /***
     * 主紐設置背景 for resource
     */
    public void setBackgroundResource(int imgRes) {
        mainButton.setBackgroundResource(imgRes);
    }

    //-------
    public interface OnMainButtonClickListener {
        void onMainButtonCLicked();
    }

    public void setOnMainButtonClickListener(OnMainButtonClickListener onMainButtonClickListener) {
        this.onMainButtonClickListener = onMainButtonClickListener;
    }


    //--------------
    public interface OnMainButtonSwipeListener {
        void onMainButtonSwipe(int x, int y);
    }

    public void setOnMainButtonSwipeListener(OnMainButtonSwipeListener onMainButtonSwipeListener) {
        this.onMainButtonSwipeListener = onMainButtonSwipeListener;
    }
}
