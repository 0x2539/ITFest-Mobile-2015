package bytes.smart.coolapp.interfaces;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Iterator;

import example.com.androidnotifier.Activities.MainActivity;
import example.com.androidnotifier.Pojos.BasicNotification;
import example.com.androidnotifier.Pojos.NotificationRule;
import example.com.androidnotifier.Pojos.NotificationRuleApp;
import example.com.androidnotifier.R;
import example.com.androidnotifier.Utils.CameraUtils;
import example.com.androidnotifier.Utils.Constants;
import example.com.androidnotifier.Utils.PrefUtils;
import example.com.androidnotifier.Utils.ScreenUtils;
import example.com.androidnotifier.Utils.VibrateUtils;

/**
 * Created by Alexandru on 02-Jun-15.
 */
public class CustomNotification {

    private Context context;
    private int notificationId;
    private static int notificationIndexer = 2;
    private NotificationRule notificationRule;
    private NotificationCompat.Builder notificationCompat;
    private int backgroundColor;
    private ArrayList<BasicNotification> basicNotifications;
    private RemoteViews contentViewNotification;
    private NotificationRuleApp defaultNnotificationRuleApp;
    private NotificationManager notificationManager;

    public CustomNotification(Context context, NotificationRule notifcationRule, NotificationManager notificationManager)
    {
        this.context = context;
        this.notificationRule = notifcationRule;
        this.notificationManager = notificationManager;

        notificationId = notificationIndexer++;
        defaultNnotificationRuleApp = new NotificationRuleApp();
        basicNotifications = new ArrayList<>();

        createNotification();
    }

    public NotificationRule getNotificationRule() {
        return notificationRule;
    }

    public NotificationCompat.Builder getNotificationCompat() {
        return notificationCompat;
    }

    public int getTextColor() {
        if(PrefUtils.getBooleanFromPrefs(getContext(), Constants.PREF_NOTIFICATION_THEME_IS_DARK, false))
        {
            return getContext().getResources().getColor(R.color.dark_theme_notification_text_color);
        }

        return getContext().getResources().getColor(R.color.light_theme_notification_text_color);
    }

//    private void setNotificationTextColor()
//    {
//        if(notificationCompat != null && isEmpty())
//        {
//            RemoteViews remoteViews = notificationCompat.build().contentViewNotification;
//            remoteViews.setTextColor(R.id.layout_notification_message_textview, getTextColor());
//
//            notificationCompat.setContent(remoteViews);
//
//            notifyNotification();
//        }
//    }

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
//        NotificationManagerCompat.from(getContext()).notify(getNotificationId(), getNotificationCompat().getNotification());
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
            notificationCompat.setSmallIcon(R.mipmap.notification_white);
            notificationCompat.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.notification_white));
            notificationCompat.setColor(context.getResources().getColor(R.color.transparent));

            contentViewNotification = new RemoteViews(getContext().getPackageName(), R.layout.layout_notification);

            contentViewNotification.setTextColor(R.id.layout_notification_message_textview, getTextColor());

            contentViewNotification.setTextViewText(R.id.layout_notification_message_textview, getNotificationRule().getNotificationMessage());

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

    private void addNewAppToNotification(BasicNotification basicNotification, NotificationRuleApp notificationRuleApp) {

        RemoteViews partialContentViewNotification = new RemoteViews(
                getContext().getPackageName(),
                R.layout.layout_notification_item
        );

        if (notificationRuleApp != defaultNnotificationRuleApp) {
            partialContentViewNotification.setInt(
                    R.id.layout_notification_item_imageview,
                    Constants.METHOD_SET_BACKGROUND_COLOR,
                    notificationRuleApp.getColor());
        } else {
            partialContentViewNotification.setInt(
                    R.id.layout_notification_item_imageview,
                    Constants.METHOD_SET_BACKGROUND_COLOR,
                    notificationRule.getDefaultColor());
        }

        try {
            partialContentViewNotification.setOnClickPendingIntent(
                    R.id.layout_notification_item_imageview,
                    basicNotification.getOriginalAppNotification().contentIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentViewNotification.addView(R.id.layout_notification_container_layout, partialContentViewNotification);

        contentViewNotification.setTextColor(R.id.layout_notification_message_textview, getContext().getResources().getColor(R.color.white));
    }

    public void newNotificationArrived(BasicNotification pBbasicNotification)
    {
        if(basicNotificationIsValid(pBbasicNotification) &&
                notificationCanBeAdded(pBbasicNotification) != null)
        {
            //update the list of notification if necessary
            updateNotificationsList(pBbasicNotification);

            //if the app notification hasn't been processed before, then add it to the list and
            //play the vibrate pattern
            if (!appNotificationAlreadyHere(pBbasicNotification)) {

                basicNotifications.add(pBbasicNotification);

//                vibrateNotification();
                refreshNotificationLayout();
            }
        }
    }

    public void newNotificationRemoved(BasicNotification basicNotification)
    {
        //remove the app from the list
        boolean removedNotification = basicNotifications.remove(basicNotification);

        if(removedNotification)
        {
            refreshNotificationLayout();
            vibrateNotification(true);
        }
    }

    public void refreshNotificationLayout() {
        ScreenUtils.releaseWakeLock(getContext());
        CameraUtils.blinkLed(getContext(), false, getNotificationRule().getFlashLightTime());


        //remove all the views, because we will be re-adding them
        contentViewNotification.removeAllViews(R.id.layout_notification_container_layout);
        notificationCompat.setSmallIcon(R.mipmap.notification_green);
        notificationCompat.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.notification_green));
        notificationCompat.setColor(context.getResources().getColor(R.color.green_light));

        //remove the empty notifications
//        Iterator iterator = basicNotifications.iterator();
//        while (iterator.hasNext()) {
//            BasicNotification basicNotification = (BasicNotification) iterator.next();
//
//            if (basicNotification.getOriginalAppNotification() == null ||
//                    basicNotification.getOriginalAppNotification().contentIntent == null) {
//                iterator.remove();
//            }
//        }

        if (basicNotifications.size() > 0) {
            Iterator iterator = basicNotifications.iterator();
//            iterator = basicNotifications.iterator();
            while (iterator.hasNext()) {
                BasicNotification basicNotification = (BasicNotification) iterator.next();
                NotificationRuleApp customNotificationRuleApp = notificationCanBeAdded(basicNotification);
//            for (BasicNotification basicNotification : basicNotifications) {

                //if it is a basic notification with no special customization
//                if (basicNotification.doesNotificationContainSender(getNotificationRule().getDefaultSender())) {
//
//                    customNotificationRuleApp = defaultNnotificationRuleApp;
//                }

                //we also check if it is a customized app
//                for (NotificationRuleApp notificationRuleApp : notificationRule.getNotificationRuleAppsList()) {
//
//                    //check if the package names match and if the title contains the sender's name
//                    if (basicNotification.getPackageName().equals(notificationRuleApp.getPackageName()) &&
//                            basicNotification.doesNotificationContainSender(notificationRuleApp.getSender())) {
//
//                        customNotificationRuleApp = notificationRuleApp;
//                        break;
//                    }
//                }

                //if it is either a basic notification or customized app, then add the app to the notification
                //TODO commented the if else, because it is redundant, it will never enter on the "else"
                //TODO not sure if this will create a bug
                //if(customNotificationRuleApp != null) {

                addNewAppToNotification(basicNotification, customNotificationRuleApp);
//                }
//                else
//                {
//                    Log.e("CustomNotification", "custom notification not matching anything");
//                    Log.e("CustomNotification", basicNotification.toString());
//                    iterator.remove();
//                }
            }

            //TODO add setSound to play when adding notification
            if(getNotificationRule().getVibratePattern() != null)
            {
                notificationCompat.setVibrate(VibrateUtils.getVibratePatternAsArray(getNotificationRule().getVibratePattern()));
            }

            notificationCompat.setContent(contentViewNotification);
            notifyNotification();
            if (getNotificationRule().getWakeUpTheScreen() > 0) {
                ScreenUtils.wakeScreen(getContext(), getNotificationRule().getWakeUpTheScreen());
            }
            if (getNotificationRule().getFlashLightTime() > 0) {
                CameraUtils.blinkLed(getContext(), true, getNotificationRule().getFlashLightTime());
            }
        } else
            //there are no apps to show in notification
            //don't use an else for the above if, because some notifications may be removed in
            //the above if statement
            if (basicNotifications.size() == 0) {
                contentViewNotification.setTextColor(R.id.layout_notification_message_textview, getTextColor());
                notificationCompat.setContent(contentViewNotification);
                notificationCompat.setSmallIcon(R.mipmap.notification_white);
                notificationCompat.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.notification_white));
                notificationCompat.setColor(context.getResources().getColor(R.color.transparent));

                notifyNotification();
                //close the screen if there are no more notifications (in case they were read on another
                //device)
            }
    }

    private boolean appNotificationAlreadyHere(BasicNotification basicNotification)
    {
        if(basicNotifications != null)
        {
            for(BasicNotification tempBasicNotification : basicNotifications)
            {
                if(basicNotification.getPackageName().equals(tempBasicNotification.getPackageName()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean basicNotificationIsValid(BasicNotification basicNotification)
    {
        if(basicNotification != null &&
                basicNotification.getOriginalAppNotification() != null &&
                basicNotification.getOriginalAppNotification().contentIntent != null)
        {
            return true;
        }

        return false;
    }

    /**
     *
     * @param basicNotification
     * @return the notification rule app of the app that matches the given basic notification; if no match is found, then it will return the default one
     */
    private NotificationRuleApp notificationCanBeAdded(BasicNotification basicNotification)
    {
        for (NotificationRuleApp notificationRuleApp : notificationRule.getNotificationRuleAppsList()) {

            //check if the package names match and if the title contains the sender's name
            if (basicNotification.getPackageName().equals(notificationRuleApp.getPackageName()) &&
                    basicNotification.doesNotificationContainSender(notificationRuleApp.getSender())) {
                return notificationRuleApp;
            }
        }

        if (basicNotification.doesNotificationContainSender(getNotificationRule().getDefaultSender())) {
            return defaultNnotificationRuleApp;
        }

        return null;
    }

    private void updateNotificationsList(BasicNotification basicNotification)
    {
        for (int i = 0; i < basicNotifications.size(); i++) {
            if (basicNotifications.get(i).getPackageName().equals(basicNotification.getPackageName()) &&
                    basicNotifications.get(i).getNotificationId() == basicNotification.getNotificationId()) {
                basicNotifications.set(i, basicNotification);
                break;
            }
        }
    }

    /**
     * If no parameters are given, then the current vibrate pattern will be played
     * @param cancel if true, then the vibrate pattern will be stopped if it is currently playing
     */
    private void vibrateNotification(boolean... cancel)
    {
        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        if(cancel.length > 0 && cancel[0]) {
            vibrator.cancel();
        }
        else {
            if (getNotificationRule().getVibratePattern() != null &&
                    getNotificationRule().getVibratePattern().size() > 0) {
                VibrateUtils.playVibratePattern(context, vibrator, getNotificationRule().getVibratePattern());
            }
        }
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof CustomNotification){
            CustomNotification ptr = (CustomNotification) v;
            retVal = ptr.getNotificationRule().getId() == this.getNotificationRule().getId();
        }

        return retVal;
    }

    public ArrayList<BasicNotification> getBasicNotifications() {
        return basicNotifications;
    }
}
