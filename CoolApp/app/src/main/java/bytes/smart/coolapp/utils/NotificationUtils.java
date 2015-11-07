package bytes.smart.coolapp.utils;

import android.os.Bundle;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class NotificationUtils {

    private static final String TAG = "NotificationUtils";

    public static String getNotificationInfo(Bundle extras, String extraID)
    {
        if (extras.getCharSequence(extraID) != null) {
            return extras.getCharSequence(extraID).toString();
        }
        return "";
    }

    public static CharSequence[] getNotificationInfoArray(Bundle extras, String extraID)
    {
        if (extras.getCharSequenceArray(extraID) != null) {
            return extras.getCharSequenceArray(extraID);
        }
        return null;
    }

    public static String[] getNotificationStringArray(Bundle extras, String extraID)
    {
        if (extras.getStringArray(extraID) != null) {
            return extras.getStringArray(extraID);
        }
        return null;
    }

}
