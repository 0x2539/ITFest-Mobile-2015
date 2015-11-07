package bytes.smart.coolapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class NotificationRule implements Parcelable, Serializable {

    private long id;
    private String notificationMessage;
    private String defaultSender;
    private int wakeUpTheScreen;
    private String wakeUpTheScreenAsString;
    private int flashLightTime;
    private String flashLightAsString;
    private ArrayList<Vibration> vibratePattern;
    private int defaultColor;
    private ArrayList<NotificationRuleApp> notificationRuleAppsList;

    public NotificationRule() {
        vibratePattern = new ArrayList<>();
        notificationRuleAppsList = new ArrayList<>();
        this.id = -1;
    }

    public NotificationRule(String notificationMessage, String defaultSender, int wakeUpTheScreen,
                            int flashLightTime, int defaultColor, ArrayList<Vibration> vibratePattern,
                            ArrayList<NotificationRuleApp> notificationRuleAppsList) {
        this.notificationMessage = notificationMessage;
        this.defaultSender = defaultSender;
        this.wakeUpTheScreen = wakeUpTheScreen;
        this.flashLightTime = flashLightTime;
        this.defaultColor = defaultColor;
        this.vibratePattern = vibratePattern;
        this.notificationRuleAppsList = notificationRuleAppsList;
        this.id = -1;
        this.wakeUpTheScreenAsString = "";
        this.flashLightAsString = "";
    }

    public NotificationRule(String notificationMessage, String defaultSender, String wakeUpTheScreenAsString,
                            String flashLightAsString, int defaultColor, ArrayList<Vibration> vibratePattern,
                            ArrayList<NotificationRuleApp> notificationRuleAppsList) {
        this.notificationMessage = notificationMessage;
        this.defaultSender = defaultSender;
        this.wakeUpTheScreenAsString = wakeUpTheScreenAsString;
        this.flashLightAsString = flashLightAsString;
        this.defaultColor = defaultColor;
        this.vibratePattern = vibratePattern;
        this.notificationRuleAppsList = notificationRuleAppsList;
        this.id = -1;
        this.wakeUpTheScreen = 0;
    }

    public NotificationRule(String notificationMessage, String defaultSender, int wakeUpTheScreen,
                            int flashLightTime, int defaultColor, ArrayList<Vibration> vibratePattern,
                            ArrayList<NotificationRuleApp> notificationRuleAppsList, long id) {
        this(notificationMessage, defaultSender, wakeUpTheScreen, flashLightTime,
                defaultColor, vibratePattern, notificationRuleAppsList);

        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getDefaultSender() {
        return defaultSender.toLowerCase();
    }

    public void setDefaultSender(String defaultSender) {
        this.defaultSender = defaultSender;
    }

    public int getWakeUpTheScreen() {
        return wakeUpTheScreen;
    }

    public void setWakeUpTheScreen(int wakeUpTheScreen) {
        this.wakeUpTheScreen = wakeUpTheScreen;
    }

    public ArrayList<Vibration> getVibratePattern() {
        return vibratePattern;
    }

    public void setVibratePattern(ArrayList<Vibration> vibratePattern) {
        if (vibratePattern == null) {
            vibratePattern = new ArrayList<>();
        }
        this.vibratePattern = vibratePattern;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public ArrayList<NotificationRuleApp> getNotificationRuleAppsList() {
        if (notificationRuleAppsList == null) {
            notificationRuleAppsList = new ArrayList<>();
        }
        return notificationRuleAppsList;
    }

    public void setNotificationRuleAppsList(ArrayList<NotificationRuleApp> notificationRuleAppsList) {
        if (notificationRuleAppsList == null) {
            notificationRuleAppsList = new ArrayList<>();
        }
//        Log.i("notiifcation rule", "notif size: " + not)
        this.notificationRuleAppsList = notificationRuleAppsList;
    }

    public static final Creator<NotificationRule> CREATOR = new Creator<NotificationRule>() {
        public NotificationRule createFromParcel(Parcel in) {
            return new NotificationRule(in);
        }

        public NotificationRule[] newArray(int size) {
            return new NotificationRule[size];
        }
    };

    private NotificationRule(Parcel in) {
        //call the constructor
        this();

        this.id = in.readLong();
        this.notificationMessage = in.readString();
        this.defaultSender = in.readString();
        this.wakeUpTheScreenAsString = in.readString();
        this.flashLightAsString = in.readString();
        this.wakeUpTheScreen = in.readInt();
        this.flashLightTime = in.readInt();
        try {
            in.readTypedList(this.vibratePattern, Vibration.CREATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.defaultColor = in.readInt();
        try {
            in.readTypedList(this.notificationRuleAppsList, NotificationRuleApp.CREATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(notificationMessage);
        dest.writeString(defaultSender);
        dest.writeString(wakeUpTheScreenAsString);
        dest.writeString(flashLightAsString);
        dest.writeInt(wakeUpTheScreen);
        dest.writeInt(flashLightTime);
        if (vibratePattern != null)// && defaultVibratePattern.size() > 0)
        {
            dest.writeTypedList(vibratePattern);
        }
        dest.writeInt(defaultColor);
        if (notificationRuleAppsList != null)// && notificationRuleAppsList.size() > 0)
        {
            dest.writeTypedList(notificationRuleAppsList);
        }
    }

    public String getWakeUpTheScreenAsString() {
        return wakeUpTheScreenAsString;
    }

    public int getFlashLightTime() {
        return flashLightTime;
    }

    public void setFlashLightTime(int flashLightTime) {
        this.flashLightTime = flashLightTime;
    }

    public String getFlashLightAsString() {
        return flashLightAsString;
    }

    public void setFlashLightAsString(String flashLightAsString) {
        this.flashLightAsString = flashLightAsString;
    }
}
