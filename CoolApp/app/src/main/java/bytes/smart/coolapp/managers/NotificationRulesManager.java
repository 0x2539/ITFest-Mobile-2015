package bytes.smart.coolapp.managers;

import java.util.ArrayList;

import bytes.smart.coolapp.pojos.NotificationRule;
import bytes.smart.coolapp.pojos.NotificationRuleApp;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class NotificationRulesManager {

    private static NotificationRulesManager notificationRulesManager;

    private ArrayList<NotificationRule> notificationRules;
    private ArrayList<NotificationRuleApp> notificationRuleApps;

    private NotificationRulesManager(){
        notificationRules = new ArrayList<>();
        notificationRuleApps = new ArrayList<>();
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

    public ArrayList<NotificationRuleApp> getNotificationRuleApps() {
        return notificationRuleApps;
    }

    public void setNotificationRuleApps(ArrayList<NotificationRuleApp> notificationRuleApps) {
        this.notificationRuleApps = notificationRuleApps;
    }
}
