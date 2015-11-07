package bytes.smart.coolapp.pojos.models;

import android.util.Log;

import java.util.ArrayList;

import bytes.smart.coolapp.interfaces.SimpleObservable;
import bytes.smart.coolapp.pojos.NotificationRule;
import bytes.smart.coolapp.pojos.NotificationRuleApp;
import bytes.smart.coolapp.pojos.Vibration;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class AddNewNotificationModel extends SimpleObservable<AddNewNotificationModel> {

    private long id;
    private String message;
    private String contact;
    private String wakeUp;
    private String flashlight;
    private int color;
    private boolean isInEditMode;
    private ArrayList<Vibration> vibratePattern;
    private ArrayList<NotificationRuleApp> rulesList;

    public void update(boolean... update) {
        if ((update.length > 0 && update[0]) || update.length == 0) {
            notifyObservers();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id, boolean... update) {
        this.id = id;
        update(update);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message, boolean... update) {
        this.message = message;
        update(update);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact, boolean... update) {
        this.contact = contact;
        update(update);
    }

    public String getWakeUp() {
        return wakeUp;
    }

    public void setWakeUp(String wakeUp, boolean... update) {
        this.wakeUp = wakeUp;
        update(update);
    }

    public String getFlashlight() {
        return flashlight;
    }

    public void setFlashlight(String flashlight, boolean... update) {
        this.flashlight = flashlight;
        update(update);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color, boolean... update) {
        this.color = color;
        update(update);
    }

    public boolean isInEditMode() {
        return isInEditMode;
    }

    public void setIsInEditMode(boolean isInEditMode, boolean... update) {
        this.isInEditMode = isInEditMode;
        update(update);
    }

    public ArrayList<Vibration> getVibratePattern() {
        if (vibratePattern == null) {
            vibratePattern = new ArrayList<>();
        }
        return vibratePattern;
    }

    public void setVibratePattern(ArrayList<Vibration> vibratePattern, boolean... update) {
        this.vibratePattern = vibratePattern;
        update(update);
    }

    public ArrayList<NotificationRuleApp> getRulesList() {
        if (rulesList == null) {
            rulesList = new ArrayList<>();
        }
        return rulesList;
    }

    public void setRulesList(ArrayList<NotificationRuleApp> rulesList, boolean... update) {
        this.rulesList = rulesList;
        update(update);
    }
    public void populateModel(NotificationRule notificationRule)
    {
        Log.i("database", notificationRule.getId() + " id");
        setId(notificationRule.getId());
        setMessage(notificationRule.getNotificationMessage());
        setContact(notificationRule.getDefaultSender());
        setWakeUp(notificationRule.getWakeUpTheScreen() + "");
        setFlashlight(notificationRule.getFlashLightTime() + "");
        setColor(notificationRule.getDefaultColor());
        setVibratePattern(notificationRule.getVibratePattern());
        setRulesList(notificationRule.getNotificationRuleAppsList());
    }

}
