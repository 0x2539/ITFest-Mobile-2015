package bytes.smart.coolapp.interfaces;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.activties.MainActivity;
import bytes.smart.coolapp.pojos.BasicNotification;
import bytes.smart.coolapp.utils.CameraUtils;
import bytes.smart.coolapp.utils.ScreenUtils;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class CustomNotification {

    private final String TAG = "CustomNotification";

    private Context context;
    private int notificationId;
    private static int notificationIndexer = 2;
    private NotificationCompat.Builder notificationCompat;
    private RemoteViews contentViewNotification;
    private NotificationManager notificationManager;
    private ArrayList<BasicNotification> basicNotifications;

    public CustomNotification(Context context, NotificationManager notificationManager)
    {
        this.context = context;
        this.notificationManager = notificationManager;

        basicNotifications = new ArrayList<>();

        notificationId = notificationIndexer++;
        createNotification();
    }

    public NotificationCompat.Builder getNotificationCompat() {
        return notificationCompat;
    }

    public int getNotificationId() {
        return notificationId;
    }

    private void notifyNotification()
    {
        if(notificationManager == null)
        {
            notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        notificationManager.cancel(getNotificationId());
        notificationManager.notify(getNotificationId(), getNotificationCompat().build());
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void createNotification()
    {
        if(notificationCompat == null) {

            notificationCompat = new NotificationCompat.Builder(getContext());
            notificationCompat.setSmallIcon(R.mipmap.ic_launcher);
            notificationCompat.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher));
            notificationCompat.setColor(context.getResources().getColor(R.color.transparent));

            contentViewNotification = new RemoteViews(getContext().getPackageName(), R.layout.layout_notification);

            contentViewNotification.setTextColor(R.id.layout_notification_message_textview, getContext().getResources().getColor(R.color.black));

            contentViewNotification.setTextViewText(R.id.layout_notification_message_textview, "Hello");

            notificationCompat.setContent(contentViewNotification);

            Intent notificationIntent = new Intent(getContext(), MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, 0);
            notificationCompat.setContentIntent(contentIntent);

            notificationCompat.setOngoing(true);
            notificationCompat.setPriority(Notification.PRIORITY_MAX);
        }

        notifyNotification();
    }

    public void dismissNotification()
    {
        Log.i("CustomNotification", "dismissed notification");
        notificationManager.cancel(getNotificationId());
//        NotificationManagerCompat.from(getContext()).cancel(getNotificationId());
    }

    private void addNewAppToNotification(BasicNotification basicNotification) {

        RemoteViews partialContentViewNotification = new RemoteViews(
                getContext().getPackageName(),
                R.layout.layout_notification_item
        );

        partialContentViewNotification.setInt(
                R.id.layout_notification_item_imageview,
                "setBackgroundColor",
                getContext().getResources().getColor(R.color.custom_color_10));

        try {
            partialContentViewNotification.setOnClickPendingIntent(
                    R.id.layout_notification_item_imageview,
                    basicNotification.getContentIntent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentViewNotification.addView(R.id.layout_notification_container_layout, partialContentViewNotification);

        contentViewNotification.setTextColor(R.id.layout_notification_message_textview, getContext().getResources().getColor(R.color.white));
    }


    public void refreshNotificationLayout(boolean notificationReceived) {

        //remove all the views, because we will be re-adding them
        contentViewNotification.removeAllViews(R.id.layout_notification_container_layout);
        notificationCompat.setSmallIcon(R.mipmap.ic_launcher);
        notificationCompat.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher));
        notificationCompat.setColor(context.getResources().getColor(R.color.green_light));

        for(BasicNotification basicNotification : basicNotifications)
        {
            addNewAppToNotification(basicNotification);
        }

        notificationCompat.setContent(contentViewNotification);
        notifyNotification();

        if(notificationReceived) {
            ScreenUtils.releaseWakeLock(getContext());
            CameraUtils.blinkLed(getContext(), false, 5);

            ScreenUtils.wakeScreen(getContext(), 5);
            CameraUtils.blinkLed(getContext(), true, 5);
        }
    }

    public void newNotificationArrived(BasicNotification basicNotification)
    {
        if(basicNotification.containsContact("buicescu"))
        {
            Log.i(TAG, "found contact");
            basicNotifications.add(basicNotification);
            refreshNotificationLayout(true);
        }
    }

    public void newNotificationRemoved(BasicNotification basicNotification)
    {
        if(basicNotifications.contains(basicNotification))
        {
            Log.i(TAG, "removed notification");
            basicNotifications.remove(basicNotification);
            refreshNotificationLayout(false);
        }
    }

}
