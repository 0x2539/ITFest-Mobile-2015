package bytes.smart.coolapp.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Alexandru on 02-Jun-15.
 */
public class ScreenUtils {

    private static final String TAG = "ScreenUtils";

    private static PowerManager.WakeLock wakeLock;

    public static void wakeScreen(Context context, long time) {
        releaseWakeLock(context);
        CameraUtils.blinkLed(context, false, 0);


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP, "WakeLock");
        wakeLock.acquire(time * 1000 * 60);
    }

    public static void releaseWakeLock(Context context) {
        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
        wakeLock = null;
    }
}
