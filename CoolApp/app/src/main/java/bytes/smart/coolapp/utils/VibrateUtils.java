package bytes.smart.coolapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import java.util.ArrayList;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.pojos.Vibration;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class VibrateUtils {

    private static final String TAG = "VibrateUtils";

    private static boolean isPlayingVibrate = false;
//    private static Thread vibrateThread;
    private static int playedTimes = 0;
    private static Context context;
    private static Menu optionsMenu;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if(msg.what != playedTimes)
                {
                    return;
                }
                isPlayingVibrate = false;
                if(optionsMenu != null)
                {
                    setPlayIcon(context, optionsMenu, R.id.menu_vibrate_play_action);
                }
                else
                {
                    Log.i("VibrateUtils", "options menu is null");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public static void playStopVibratePattern(
            final Context context,
            final Vibrator vibrator,
            final ArrayList<Vibration> vibrations) {
        playStopVibratePattern(context, vibrator, vibrations, null);
    }

    public static void playStopVibratePattern(
            final Context context,
            final Vibrator vibrator,
            final ArrayList<Vibration> vibrations,
            final Menu optionsMenu) {
        isPlayingVibrate = !isPlayingVibrate;

        if (isPlayingVibrate) {
            playVibratePattern(context, vibrator, vibrations, optionsMenu);
        }
        else
        {
            stopVibratePattern(context, optionsMenu, vibrator);
        }
    }

    public static void playVibratePattern(
            final Context context,
            final Vibrator vibrator,
            final ArrayList<Vibration> vibrations)
    {
        playVibratePattern(context, vibrator, vibrations, null);
    }

    public static void playVibratePattern(
            final Context context,
            final Vibrator vibrator,
            final ArrayList<Vibration> vibrations,
            final Menu optionsMenu)
    {
        VibrateUtils.context = context;
        VibrateUtils.optionsMenu = optionsMenu;

        if(vibrations == null)
        {
            return;
        }
        isPlayingVibrate = true;
        if(optionsMenu != null)
        {
            setStopIcon(context, optionsMenu, R.id.menu_vibrate_play_action);
        }
        long[] vibratePattern = new long[vibrations.size() * 2 + 1];

        //start immediately
        vibratePattern[0] = 0;
        int k = 1;
        long time = 0;
        for(Vibration vibration : vibrations)
        {
            vibratePattern[k++] = vibration.getVibrateTime();
            vibratePattern[k++] = vibration.getPauseTime();
            time += vibration.getVibrateTime();
            time += vibration.getPauseTime();
        }

        vibrator.vibrate(vibratePattern, -1);

        playedTimes++;
        handler.sendEmptyMessageDelayed(playedTimes, time);

//        vibrateThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
////                updateButtonText(context, updateButton, "STOP");
//                setStopIcon(context, optionsMenu, R.id.menu_vibrate_play_action);
//
//                for (final Vibration vibration : vibrations) {
//
//                    if (!isPlayingVibrate) {
//                        return;
//                    }
//
//                    try {
//                        vibrator..vibrate(vibration.getVibrateTime());
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        Thread.sleep(vibration.getVibrateTime() + vibration.getPauseTime());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        //it enters here when I call vibrateThread.interrupt();
//                        return;
//                    }
//                }
//                isPlayingVibrate = false;
////                updateButtonText(context, updateButton, "PLAY");
//                setPlayIcon(context, optionsMenu, R.id.menu_vibrate_play_action);
//            }
//        });
//        vibrateThread.start();
    }

    public static void stopVibratePattern(Context context, Menu optionsMenu, final Vibrator vibrator)
    {
        isPlayingVibrate = false;

//        if(vibrateThread != null)
//        {
//            vibrateThread.interrupt();
//            vibrateThread = null;
//        }
        vibrator.cancel();
//        updateButtonText(context, updateButton, "PLAY");
        if(optionsMenu != null)
        {
            setPlayIcon(context, optionsMenu, R.id.menu_vibrate_play_action);
        }
    }

    public static boolean isIsPlayingVibrate()
    {
        return isPlayingVibrate;
    }

    private static void updateButtonText(Context context, final Button button, final String text)
    {
        try {
            if (button != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setText(text);
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setStopIcon(Context context, final Menu optionsMenu, final int actionId)
    {
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        optionsMenu.findItem(actionId).setIcon(R.drawable.ic_stop_white_24dp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setPlayIcon(Context context, final Menu optionsMenu, final int actionId)
    {
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        optionsMenu.findItem(actionId).setIcon(R.drawable.ic_play_circle_filled_white_36dp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean checkForPermissionToBindToNotificationListenerService(Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = context.getPackageName();

        // check to see if the enabledNotificationListeners String contains our package name
        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName))
        {
            return false;
        }
        return true;
    }

    public static long[] getVibratePatternAsArray(ArrayList<Vibration> vibrations)
    {
        long[] vibratePattern = new long[vibrations.size() * 2 + 1];

        //start immediately
        vibratePattern[0] = 0;
        int k = 1;
        long time = 0;
        for(Vibration vibration : vibrations)
        {
            vibratePattern[k++] = vibration.getVibrateTime();
            vibratePattern[k++] = vibration.getPauseTime();
            time += vibration.getVibrateTime();
            time += vibration.getPauseTime();
        }

        return vibratePattern;
    }
}
