package bytes.smart.coolapp.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

import bytes.smart.coolapp.activties.AddNewNotificationActivity;
import bytes.smart.coolapp.interfaces.CustomNotification;
import bytes.smart.coolapp.pojos.BasicNotification;
import bytes.smart.coolapp.pojos.NotificationRule;


/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class AppNotificationService extends NotificationListenerService {

    private final String TAG = "AppNotificationService";
    public static boolean isNotificationAccessEnabled = false;
    public static boolean finishedCheckingForPermissions = false;
    private static NotificationManager notificationManager;
    private static ArrayList<CustomNotification> customNotifications;

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        isNotificationAccessEnabled = true;
        finishedCheckingForPermissions = true;

        Log.i(TAG, "onBind()");
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        isNotificationAccessEnabled = false;
        Log.i(TAG, "onUnbind()");
        return mOnUnbind;
    }

    @Override
    public void onCreate() {

        Log.i(TAG, "onCreate()");
        super.onCreate();
        finishedCheckingForPermissions = false;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        customNotifications = new ArrayList<>();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();

        if (pack != null && !pack.equals("") && !pack.equals(getApplicationContext().getPackageName())) {
            Log.i(TAG, "Notification Posted with package: " + pack);

            Bundle extras = NotificationCompat.getExtras(sbn.getNotification());

            if (extras != null) {

                BasicNotification newBasicNotification = new BasicNotification(sbn, extras);

                Log.i(TAG, "notification title: " + newBasicNotification.getNotificationTitle());
                Log.i(TAG, "notification big title: " + newBasicNotification.getBigNotificationTitle());
                Log.i(TAG, "notification text: " + newBasicNotification.getNotificationText());
                Log.i(TAG, "notification big text: " + newBasicNotification.getBigNotificationText());

                for(CustomNotification customNotification : customNotifications) {
                    customNotification.newNotificationArrived(newBasicNotification);
                }

            } else {
                Log.i(TAG, "notification's extras is null");
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();

        if (pack != null && !pack.equals("") && !pack.equals(getApplicationContext().getPackageName())) {
            Log.i(TAG, "Notification Removed with package: " + pack);

            Bundle extras = NotificationCompat.getExtras(sbn.getNotification());

            if (extras != null) {

                BasicNotification newBasicNotification = new BasicNotification(sbn, extras);

                Log.i(TAG, "notification title: " + newBasicNotification.getNotificationTitle());
                Log.i(TAG, "notification big title: " + newBasicNotification.getBigNotificationTitle());
                Log.i(TAG, "notification text: " + newBasicNotification.getNotificationText());
                Log.i(TAG, "notification big text: " + newBasicNotification.getBigNotificationText());

                for(CustomNotification customNotification : customNotifications) {
                    customNotification.newNotificationRemoved(newBasicNotification);
                }
            } else {
                Log.i(TAG, "removed notification's extras is null");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        for(CustomNotification customNotification : customNotifications) {
            customNotification.dismissNotification();
        }
    }

    public static void notificationListenerUpdated(Context context, NotificationRule notificationRule) {
        for(CustomNotification customNotification : customNotifications) {
            if(customNotification.getNotificationRule().getId() == notificationRule.getId())
            {
                customNotification.dismissNotification();
                customNotifications.remove(customNotification);
                break;
            }
        }
        customNotifications.add(new CustomNotification(context, notificationManager, notificationRule));
    }

    public static void newNotificationListenerCreated(Context context, NotificationRule notificationRule) {
        customNotifications.add(new CustomNotification(context, notificationManager, notificationRule));
    }

    public static void notificationListenerRemoved(NotificationRule notificationRule) {
        for(CustomNotification customNotification : customNotifications) {
            if(customNotification.getNotificationRule().getId() == notificationRule.getId())
            {
                customNotification.dismissNotification();
                customNotifications.remove(customNotification);
                return;
            }
        }
    }
}

