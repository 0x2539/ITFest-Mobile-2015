package bytes.smart.coolapp.managers;

import java.util.ArrayList;

import bytes.smart.coolapp.pojos.NotificationRule;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class NotificationRulesManager {

    private static NotificationRulesManager notificationRulesManager;

    private ArrayList<NotificationRule> notificationRules;

    private NotificationRulesManager(){
        notificationRules = new ArrayList<>();
    }

    public static NotificationRulesManager getNotificationRulesManager() {
        if(notificationRulesManager == null)
        {
            notificationRulesManager = new NotificationRulesManager();
        }
        return notificationRulesManager;
    }

    public ArrayList<NotificationRule> getNotificationRules() {
        return notificationRules;
    }

    public void setNotificationRules(ArrayList<NotificationRule> notificationRules) {
        this.notificationRules = notificationRules;
    }
}
