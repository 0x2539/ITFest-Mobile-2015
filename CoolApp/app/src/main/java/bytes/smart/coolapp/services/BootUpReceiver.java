package bytes.smart.coolapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /****** For Start Activity *****/
//        Intent i = new Intent(context, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);

        /***** For start Service  ****/
        Intent myIntent = new Intent(context, AppNotificationService.class);
        context.startService(myIntent);
    }

}
