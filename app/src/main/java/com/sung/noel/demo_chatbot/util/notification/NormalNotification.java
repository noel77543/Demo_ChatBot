package com.sung.noel.demo_chatbot.util.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import com.sung.noel.demo_chatbot.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NormalNotification extends Notification {

    public final static int NOTIFICATION_ID = 9488;
    public final static String CHANNEL_ID = "NormalNotification";
    public final static String CHANNEL_NAME = "一般通知";
    public final static String CHANNEL_DESCRIPTION = "一般通知";

    //8.0以下適用
    private NotificationCompat.Builder notificationCompatBuilder;
    //8.0以上適用
    private Builder notificationBuilder;
    private Notification notification;

    private Context context;
    // 開啟另一個Activity的Intent
    private Intent intent;
    private PendingIntent pendingIntent;
    private int flags;
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private Uri defaultSoundUri;


    //-----------

    public NormalNotification(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        init();
    }

    //----------
    private void init() {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        flags = PendingIntent.FLAG_CANCEL_CURRENT;


        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, flags);
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    //----------------

    public void displayNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);

            notificationBuilder = new Builder(context, CHANNEL_ID)
                    //狀態欄的icon
                    .setSmallIcon(R.drawable.ic_bot)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_bot))
                    //通知聲音
                    .setSound(defaultSoundUri)
                    //點了之後自動消失
                    .setAutoCancel(true)
                    .setContentText( String.format(context.getString(R.string.permission_message), context.getString(R.string.app_name)))
                    .setContentIntent(pendingIntent);

            notification = notificationBuilder.build();
        } else {
            notificationCompatBuilder = new NotificationCompat.Builder(context)
                    //狀態欄的icon
                    .setSmallIcon(R.drawable.ic_bot)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_bot))
                    //使可以向下彈出
                    .setPriority(Notification.PRIORITY_HIGH)
                    //通知聲音
                    .setSound(defaultSoundUri)
                    //點了之後自動消失
                    .setAutoCancel(true)
                    .setContentText( String.format(context.getString(R.string.permission_message), context.getString(R.string.app_name)))
                    .setContentIntent(pendingIntent);

            notification = notificationCompatBuilder.build();
        }
        //發送通知
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    //----------

    /***
     * 移除通知
     */
    public void removeNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
