package bytes.smart.coolapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexandru on 01-Jun-15.
 */
public class NotificationRule implements Parcelable, Serializable {

    private long id;
    private String notificationMessage;
    private String defaultSender;
    private int wakeUpTheScreen;
    private String wakeUpTheScreenAsString;
    private int flashLightTime;
    private String flashLightAsString;
    private int lookIntoTitle;
    private int lookIntoMessage;
    private ArrayList<Vibration> vibratePattern;
    private int defaultColor;
    private ArrayList<NotificationRuleApp> notificationRuleAppsList;
    private int isEnabled;
    private long databaseId;

    public NotificationRule() {
        vibratePattern = new ArrayList<>();
        notificationRuleAppsList = new ArrayList<>();
        this.id = -1;
        this.databaseId = -1;
    }

    public NotificationRule(String notificationMessage, String defaultSender, int wakeUpTheScreen,
                            int flashLightTime, int lookIntoTitle, int lookIntoMessage,
                            int defaultColor, ArrayList<Vibration> vibratePattern,
                            ArrayList<NotificationRuleApp> notificationRuleAppsList,
                            int isEnabled) {
        this.notificationMessage = notificationMessage;
        this.defaultSender = defaultSender;
        this.wakeUpTheScreen = wakeUpTheScreen;
        this.flashLightTime = flashLightTime;
        this.lookIntoTitle = lookIntoTitle;
        this.lookIntoMessage = lookIntoMessage;
        this.defaultColor = defaultColor;
        this.vibratePattern = vibratePattern;
        this.notificationRuleAppsList = notificationRuleAppsList;
        this.id = -1;
        this.databaseId = -1;
        this.wakeUpTheScreenAsString = "";
        this.flashLightAsString = "";
        setIsEnabled(isEnabled);
    }

    public NotificationRule(String notificationMessage, String defaultSender, String wakeUpTheScreenAsString,
                            String flashLightAsString, int lookIntoTitle, int lookIntoMessage,
                            int defaultColor, ArrayList<Vibration> vibratePattern,
                            ArrayList<NotificationRuleApp> notificationRuleAppsList,
                            int isEnabled) {
        this.notificationMessage = notificationMessage;
        this.defaultSender = defaultSender;
        this.wakeUpTheScreenAsString = wakeUpTheScreenAsString;
        this.flashLightAsString = flashLightAsString;
        this.lookIntoTitle = lookIntoTitle;
        this.lookIntoMessage = lookIntoMessage;
        this.defaultColor = defaultColor;
        this.vibratePattern = vibratePattern;
        this.notificationRuleAppsList = notificationRuleAppsList;
        this.id = -1;
        this.databaseId = -1;
        this.wakeUpTheScreen = 0;
        setIsEnabled(isEnabled);
    }

    public NotificationRule(String notificationMessage, String defaultSender, int wakeUpTheScreen,
                            int flashLightTime, int lookIntoTitle, int lookIntoMessage,
                            int defaultColor, ArrayList<Vibration> vibratePattern,
                            ArrayList<NotificationRuleApp> notificationRuleAppsList,
                            int isEnabled, long id, long databaseId) {
        this(notificationMessage, defaultSender, wakeUpTheScreen, flashLightTime, lookIntoTitle,
                lookIntoMessage, defaultColor, vibratePattern, notificationRuleAppsList, isEnabled);

        this.id = id;
        this.databaseId = databaseId;
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

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getLookIntoTitle() {
        return lookIntoTitle;
    }

    public void setLookIntoTitle(int lookIntoTitle) {
        this.lookIntoTitle = lookIntoTitle;
    }

    public int getLookIntoMessage() {
        return lookIntoMessage;
    }

    public void setLookIntoMessage(int lookIntoMessage) {
        this.lookIntoMessage = lookIntoMessage;
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
        this.lookIntoTitle = in.readInt();
        this.lookIntoMessage = in.readInt();
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
        this.isEnabled = in.readInt();
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
        dest.writeInt(lookIntoTitle);
        dest.writeInt(lookIntoMessage);
        if (vibratePattern != null)// && defaultVibratePattern.size() > 0)
        {
            dest.writeTypedList(vibratePattern);
        }
        dest.writeInt(defaultColor);
        if (notificationRuleAppsList != null)// && notificationRuleAppsList.size() > 0)
        {
            dest.writeTypedList(notificationRuleAppsList);
        }
        dest.writeInt(isEnabled);
    }

    public String getWakeUpTheScreenAsString() {
        return wakeUpTheScreenAsString;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
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
