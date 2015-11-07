package bytes.smart.coolapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Alexandru on 01-Jun-15.
 */
public class NotificationRuleApp implements Parcelable, Serializable {

    private long id;
    private long notificationRuleId;
    private String sender;
    private String packageName;
    private int color;
    private String appName;

    public NotificationRuleApp() {

    }

    public NotificationRuleApp(int color, String sender, String appName, String packageName) {
        this.color = color;
        this.sender = sender;
        this.appName = appName;
        this.packageName = packageName;
    }

    public NotificationRuleApp(int color, String sender, String packageName, long id,
                               long notificationRuleId) {
        this.color = color;
        this.sender = sender;
        this.packageName = packageName;
        this.id = id;
        this.notificationRuleId = notificationRuleId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNotificationRuleId() {
        return notificationRuleId;
    }

    public void setNotificationRuleId(long notificationRuleId) {
        this.notificationRuleId = notificationRuleId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public static final Creator<NotificationRuleApp> CREATOR = new Creator<NotificationRuleApp>() {
        public NotificationRuleApp createFromParcel(Parcel in) {
            return new NotificationRuleApp(in);
        }

        public NotificationRuleApp[] newArray(int size) {
            return new NotificationRuleApp[size];
        }
    };

    private NotificationRuleApp(Parcel in) {
        //call the constructor
        this();

        this.id = in.readLong();
        this.notificationRuleId = in.readLong();
        this.sender = in.readString();
        this.packageName = in.readString();
        this.color = in.readInt();
        this.appName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(notificationRuleId);
        dest.writeString(sender);
        dest.writeString(packageName);
        dest.writeInt(color);
        dest.writeString(appName);
    }
}
