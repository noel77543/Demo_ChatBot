package com.sung.noel.demo_chatbot.util.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


import com.sung.noel.demo_chatbot.R;
import com.sung.noel.demo_chatbot.bubble.BubbleService;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BubbleNotification extends Notification {

    public final static int NOTIFICATION_ID = 9487;
    public final static String CHANNEL_ID = "BubbleWindow";
    public final static String CHANNEL_NAME = "泡泡框";
    public final static String CHANNEL_DESCRIPTION = "泡泡框控制台";

    public final static String KEY_ACTION = "action";

    //不做事
    public final static int VALUE_NONE = -1;
    //close
    public final static int VALUE_CLOSE = 11;
    //start
    public final static int VALUE_START = 12;


    private Class targetClass;
    private Context context;
    // 開啟另一個Activity的Intent
    private Intent intentNotification;
    private PendingIntent pendingIntent;
    private Bundle bundle;
    private int flags;

    private Uri defaultSoundUri;
    private PendingIntent pendingIntentClose;

    //8.0以下適用
    private NotificationCompat.Builder notificationCompatBuilder;
    //8.0以上適用
    private Builder notificationBuilder;


    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;


    /***
     * @param targetClass 打算開啟的 activity
     * @param bundle  是否攜帶資訊 可null
     */
    public BubbleNotification(Context context, Class targetClass, @Nullable Bundle bundle) {
        this.targetClass = targetClass;
        this.context = context;
        this.bundle = bundle;
        init();
    }


    //--------

    /***
     * init..
     */
    private void init() {
        intentNotification = new Intent(context.getApplicationContext(), targetClass);

        if (bundle != null) {
            intentNotification.putExtras(bundle);
        }
        intentNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentNotification.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        flags = PendingIntent.FLAG_CANCEL_CURRENT;


        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intentNotification, flags);
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }


    //-----------

    /***
     * 建立推播
     */
    public void displayNotification(int smallIconRes) {
        Notification notification;
        RemoteViews remoteViews = getRemoteViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);

            notificationBuilder = new Builder(context, CHANNEL_ID)
                    //狀態欄的icon
                    .setSmallIcon(smallIconRes)
                    //通知聲音
                    .setSound(defaultSoundUri)
                    //點了之後自動消失
                    .setAutoCancel(true)
                    //指定客製化view
                    .setCustomContentView(remoteViews)
                    .setContentIntent(pendingIntent);

            notification = notificationBuilder.build();
        } else {
            notificationCompatBuilder = new NotificationCompat.Builder(context)
                    //狀態欄的icon
                    .setSmallIcon(smallIconRes)
                    //使可以向下彈出
                    .setPriority(Notification.PRIORITY_HIGH)
                    //通知聲音
                    .setSound(defaultSoundUri)
                    //點了之後自動消失
                    .setAutoCancel(true)
                    //指定客製化view
                    .setCustomContentView(remoteViews)
                    .setContentIntent(pendingIntent);

            notification = notificationCompatBuilder.build();
        }

        //使無法被滑除
        notification.flags = Notification.FLAG_NO_CLEAR;
        //發送通知
        notificationManager.notify(NOTIFICATION_ID, notification);
    }


    //-------------

    /***
     * 移除通知
     */
    public void removeNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }


    //-------------

    /***
     *  定義通知中的按鈕行為
     * @return
     */
    private RemoteViews getRemoteViews() {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_controller);

        //關閉
        Intent intentClose = new Intent(context, BubbleService.class);
        intentClose.putExtra(KEY_ACTION, VALUE_CLOSE);
        pendingIntentClose = PendingIntent.getService(context, 0, intentClose, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_close, pendingIntentClose);
        return remoteViews;
    }
}
