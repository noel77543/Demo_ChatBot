package com.sung.noel.demo_chatbot.util.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.sung.noel.demo_chatbot.util.AnimationUtil;
import com.sung.noel.demo_chatbot.util.LayoutSizeUtil;


/**
 * Created by noel on 2018/8/4.
 */

public class CustomButton extends FrameLayout implements Runnable, View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

    //當手指抬起  經過自訂時間後 縮小
    private final int _DELAY_SCALE = 1000;


    private int layoutWidth;
    private int layoutHeight;

    private OnMainButtonClickListener onMainButtonClickListener;
    private OnMainButtonSwipeListener onMainButtonSwipeListener;
    private OnMainButtonLongClickListener onMainButtonLongClickListener;
    private LayoutParams mainButtonParams;
    private Button mainButton;
    private int mainButtonSize;

    private Context context;
    private int phoneHeight;
    private int phoneWidth;
    private AnimationUtil animationUtil;
    private Handler handler;
    private Runnable runnable;
    private boolean isSwiping = false;
    private int lastX;
    private int lastY;
    private boolean isLandscape = false;
    //觸發長按的時候
    private boolean isLongClicked=false;
    //----------

    public CustomButton(@NonNull Context context, int phoneHeight, int phoneWidth) {
        super(context);
        this.context = context;
        this.phoneHeight = phoneHeight;
        this.phoneWidth = phoneWidth;
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
        mainButton = new Button(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                animationUtil.hide(CustomButton.this);
            }
        };

        mainButton.setBackground(getShape());
        handler.postDelayed(runnable, _DELAY_SCALE);
        addMainButton();
    }
    //-------

    private GradientDrawable getShape() {
        int centerColor = Color.WHITE;
        int startColor = Color.WHITE;
        int endColor = Color.BLACK;
        int strokeWidth = 3;
        int strokeColor = Color.BLACK;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientRadius(50);
        gradientDrawable.setColors(new int[]{centerColor, startColor, endColor});
        return gradientDrawable;
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
        mainButton.setOnLongClickListener(this);
        addView(mainButton);
    }


    //----------

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getRawX();
        int y = (int) motionEvent.getRawY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                handler.removeCallbacks(runnable);
                animationUtil.show(this);
                break;
            case MotionEvent.ACTION_MOVE:
                if (onMainButtonSwipeListener != null) {
                    // TODO   WindowManager的(0,0)起始點是定位於螢幕中心
                    // TODO   所以X座標需要減少手機寬的1/2 後再減少球的寬度的1/2   同理Y座標需要減少手機高的1/2後再減少球的高度的1/2

                    int centerX;
                    int centerY;
                    //重直時
                    if (!isLandscape) {
                        centerX = x - (phoneWidth / 2) - (getWidth() / 2);
                        centerY = y - (phoneHeight / 2) - (getHeight() / 2);
                    }
                    //水平時
                    else {
                        centerX = x - (phoneHeight / 2) - (getHeight() / 2);
                        centerY = y - (phoneWidth / 2) - (getWidth() / 2);
                    }
                    if(!isLongClicked){
                        onMainButtonSwipeListener.onMainButtonSwipe(centerX, centerY);
                    }
                    //如果位移量超過10px  則
                    if (Math.abs(lastX - x) > 10 || Math.abs(lastY - y) > 10) {
                        isSwiping = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSwiping = false;
                isLongClicked = false;
                handler.postDelayed(runnable, _DELAY_SCALE);

                //如果位移量超過10px  則return true 不讓事件到下層的onClickListener
                return (Math.abs(lastX - x) > 10 || Math.abs(lastY - y) > 10);
        }
        return false;
    }
    //----------

    /***
     * 垂直　／　水平　位置的判斷有相反的處理
     * @param isLandscape
     */
    public void setOrientationChanged(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }


    //----------


    @Override
    public void onClick(View v) {
        if (onMainButtonClickListener != null) {
            onMainButtonClickListener.onMainButtonClicked();
        }
    }
    //--------


    @Override
    public boolean onLongClick(View view) {
        if (onMainButtonLongClickListener != null) {
            if (!isSwiping) {
                isLongClicked = true;
                onMainButtonLongClickListener.onMainButtonLongClicked();
                return true;
            }
        }
        return false;
    }

    //-------
    public interface OnMainButtonClickListener {
        void onMainButtonClicked();
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

    //------------
    public interface OnMainButtonLongClickListener {
        void onMainButtonLongClicked();
    }

    public void setOnMainButtonLongClickListener(OnMainButtonLongClickListener onMainButtonLongClickListener) {
        this.onMainButtonLongClickListener = onMainButtonLongClickListener;
    }
}
