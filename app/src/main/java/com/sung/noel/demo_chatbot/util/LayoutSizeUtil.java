package com.sung.noel.demo_chatbot.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by noel on 2018/3/21.
 */

public class LayoutSizeUtil {
    private Context context;

    public LayoutSizeUtil(Context context) {
        this.context = context;

    }

    //--------------

    /***
     *  取得手機寬高
     * @return
     */
    public int[] getPhoneSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int phoneWidth = metrics.widthPixels;
        int phoneHeight = metrics.heightPixels;
        return new int[]{phoneWidth, phoneHeight};
    }

    //-------------

    /***
     * 取得狀態欄高度
     */
    public int getStatusHeight() {
        int statusHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    //--------------------

    /***
     *  取得actionbar高度
     */
    public int getActionBarHeight() {
        int actionbarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionbarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionbarHeight;
    }
}
