package bytes.smart.coolapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class Utils {

    public static boolean hasNotificationListenerPermission(Context context)
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

}
