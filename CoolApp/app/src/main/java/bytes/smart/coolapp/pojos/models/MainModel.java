package bytes.smart.coolapp.pojos.models;

import java.util.ArrayList;

import bytes.smart.coolapp.interfaces.SimpleObservable;
import bytes.smart.coolapp.pojos.NotificationRule;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MainModel extends SimpleObservable<MainModel> {

    private boolean notificationAccessEnabled;
    private ArrayList<NotificationRule> notificationRules;

    public void update(boolean... update) {
        if ((update.length > 0 && update[0]) || update.length == 0) {
            notifyObservers();
        }
    }

    public boolean isNotificationAccessEnabled() {
        return notificationAccessEnabled;
    }

    public void setNotificationAccessEnabled(boolean notificationAccessEnabled, boolean... update) {
        this.notificationAccessEnabled = notificationAccessEnabled;
        update(update);
    }

    public ArrayList<NotificationRule> getNotificationRules() {
        if(notificationRules == null)
        {
            notificationRules = new ArrayList<>();
        }
        return notificationRules;
    }

    public void setNotificationRules(ArrayList<NotificationRule> notificationRules, boolean... update) {
        this.notificationRules = notificationRules;
        update(update);
    }
}
