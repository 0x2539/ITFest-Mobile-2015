package bytes.smart.coolapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Alexandru on 01-Jun-15.
 */
public class Vibration implements Parcelable, Serializable {

    private long id;
    private long notificationId;
    private int index;
    private long vibrateTime;
    private long pauseTime;

    public Vibration(int index, long vibrateTime, long pauseTime)
    {
        this.index = index;
        this.vibrateTime = vibrateTime;
        this.pauseTime = pauseTime;
    }

    public Vibration(int index, long vibrateTime, long pauseTime, long notificationId)
    {
        this(index, vibrateTime, pauseTime);
        this.notificationId = notificationId;
    }

    public Vibration(int index, long vibrateTime, long pauseTime, long notificationId, long id)
    {
        this(index, vibrateTime, pauseTime, notificationId);
        this.id = id;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getVibrateTime() {
        return vibrateTime;
    }

    public void setVibrateTime(long vibrateTime) {
        this.vibrateTime = vibrateTime;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final Creator<Vibration> CREATOR = new Creator<Vibration>()
    {
        public Vibration createFromParcel(Parcel in)
        {
            return new Vibration(in);
        }

        public Vibration[] newArray(int size)
        {
            return new Vibration[size];
        }
    };

    private Vibration(Parcel in) {
        this.id = in.readLong();
        this.notificationId = in.readLong();
        this.index = in.readInt();
        this.vibrateTime = in.readLong();
        this.pauseTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(notificationId);
        dest.writeInt(index);
        dest.writeLong(vibrateTime);
        dest.writeLong(pauseTime);
    }
}
