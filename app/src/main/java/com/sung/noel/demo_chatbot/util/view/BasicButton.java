package com.sung.noel.demo_chatbot.util.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;

/**
 * Created by noel on 2018/8/4.
 */

public class BasicButton extends Button {


    public BasicButton(Context context) {
        super(context);
        init();
    }


    //--------
    private void init() {
        int strokeWidth = 3;
        int strokeColor = Color.BLACK;
        int fillColor = Color.WHITE;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
    }
}
