package com.sung.noel.demo_chatbot.util;

import android.view.View;

public class AnimationUtil {

    private final int _DURATION = 1000;
    private final float _SCALE_SIZE = 0.6f;
    private final float _ALPHA = 0.5f;

    public AnimationUtil() {

    }
    //------------

    /***
     * 隱藏
     */
    public void hide(View view) {
        view
                .animate()
                .alpha(_ALPHA)
                .scaleY(_SCALE_SIZE)
                .scaleX(_SCALE_SIZE)
                .setDuration(_DURATION)
                .start();
    }

    //--------------

    /***
     * 顯示
     */
    public void show(View view) {
        view
                .animate()
                .alpha(1)
                .scaleY(1)
                .scaleX(1)
                .setDuration(0)
                .start();
    }
}
