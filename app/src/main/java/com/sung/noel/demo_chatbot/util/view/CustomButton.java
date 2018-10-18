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
    private LayoutSizeUtil layoutSizeUtil;
    private int phoneHeight;
    private int phoneWidth;
    private AnimationUtil animationUtil;
    private Handler handler;
    private Runnable runnable;
    private boolean isSwiping = false;
    private int lastX;
    private int lastY;

    //----------

    public CustomButton(@NonNull Context context) {
        super(context);
        this.context = context;
        post(this);
    }
    //----------

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs) {
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
        mainButton = new Button(context);
        layoutSizeUtil = new LayoutSizeUtil(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                animationUtil.hide(CustomButton.this);
            }
        };

        mainButton.setBackground(getShape());
        int[] phoneSize = layoutSizeUtil.getPhoneSize();
        phoneHeight = phoneSize[1];
        phoneWidth = phoneSize[0];
        handler.postDelayed(runnable, _DELAY_SCALE);
        addMainButton();
    }
    //-------

    private GradientDrawable getShape() {
        int centerColor = Color.WHITE;
        int startColor = Color.WHITE;
        int endColor= Color.BLACK;
        int strokeWidth = 3;
        int strokeColor = Color.BLACK;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientRadius(50);
        gradientDrawable.setColors(new int[]{centerColor,startColor,endColor});
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

                    int centerX = x - (phoneWidth / 2) - (getWidth() / 2);
                    int centerY = y - (phoneHeight / 2) - (getHeight() / 2);
                    onMainButtonSwipeListener.onMainButtonSwipe(centerX, centerY);
                    if (Math.abs(lastX - x) > 15 || Math.abs(lastY - y) > 15) {
                        isSwiping = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSwiping = false;
                handler.postDelayed(runnable, _DELAY_SCALE);
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


    @Override
    public boolean onLongClick(View view) {
        if (onMainButtonLongClickListener != null) {
            if (!isSwiping) {
                onMainButtonLongClickListener.onMainButtonLongClicked();
                return true;
            }
        }

        return false;
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

    //------------
    public interface OnMainButtonLongClickListener {
        void onMainButtonLongClicked();
    }

    public void setOnMainButtonLongClickListener(OnMainButtonLongClickListener onMainButtonLongClickListener) {
        this.onMainButtonLongClickListener = onMainButtonLongClickListener;
    }
}
