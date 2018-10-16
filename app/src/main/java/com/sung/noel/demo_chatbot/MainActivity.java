package com.sung.noel.demo_chatbot;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sung.noel.demo_chatbot.bubble.BubbleService;
import com.sung.noel.demo_chatbot.util.notification.BubbleNotification;
import com.sung.noel.demo_chatbot.util.notification.NormalNotification;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private NormalNotification normalNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        normalNotification = new NormalNotification(this);

        //service未啓動
        if (!isServiceRunning(BubbleService.class)) {

            //目標版本號23以上 且 //不具備顯示於其他應用上層之權限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                normalNotification.displayNotification(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), getString(R.string.notification_permission_draw));
                finish();
            } else {
                MainActivityPermissionsDispatcher.onAllowedRecordWithCheck(this);
            }
        }
        //Service已啟動 則關閉
        else {
            intent = new Intent(this, BubbleService.class);
            intent.putExtra(BubbleNotification.KEY_ACTION, BubbleNotification.VALUE_CLOSE);
            startService(intent);
            finish();
        }
    }


    //------------

    /***
     * 檢查背景服務是否正在執行
     * @param serviceClass
     * @return
     */
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //-------------

    /***
     * 當允許 錄音
     */
    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void onAllowedRecord() {
        intent = new Intent(this, BubbleService.class);
        intent.putExtra(BubbleNotification.KEY_ACTION, BubbleNotification.VALUE_START);
        startService(intent);
        finish();
    }
    //-------------


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    //-------------

    /***
     * 當 錄音
     */
    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    void onShowRationaleRecord(final PermissionRequest request) {
        request.proceed();
    }

    //-------------

    /***
     * 當拒絕  錄音
     */
    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    void onPermissionDeniedRecord() {
        normalNotification.displayNotification(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())), getString(R.string.notification_permission_record));
        finish();
    }
    //-------------

    /***
     * 當不再詢問  錄音
     */
    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    void onNeverAskAgainRecord() {
        normalNotification.displayNotification(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())), getString(R.string.notification_permission_record));
        finish();
    }
}
