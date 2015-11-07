package bytes.smart.coolapp.pojos;

import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import bytes.smart.coolapp.utils.NotificationUtils;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class BasicNotification
{
    private int notificationId;
    private String packageName;
    private String notificationTitle;
    private String bigNotificationTitle;
    private String notificationText;
    private String bigNotificationText;
    private String notificationSubText;
    private String notificationInfoText;
    private String notificationSummaryText;
    private CharSequence[] notificationTextLines;
    private String notificationTemplate;
    private String[] notificationPeople;

    public BasicNotification(String packageName,
                             String notificationTitle, String bigNotificationTitle,
                             String notificationText, String bigNotificationText,
                             String notificationSubText, String notificationInfoText,
                             String notificationSummaryText, CharSequence[] notificationTextLines,
                             String notificationTemplate, String[] notificationPeople,
                             int notificationId)
    {
        this.packageName = packageName.toLowerCase();
        this.notificationTitle = notificationTitle;
        this.bigNotificationTitle = bigNotificationTitle;
        this.notificationText = notificationText;
        this.bigNotificationText = bigNotificationText;
        this.notificationSubText = notificationSubText.toLowerCase();
        this.notificationInfoText = notificationInfoText.toLowerCase();
        this.notificationSummaryText = notificationSummaryText.toLowerCase();
        this.notificationTextLines = notificationTextLines;
        this.notificationTemplate = notificationTemplate;
        this.notificationPeople = notificationPeople;
        this.notificationId = notificationId;
    }

    public BasicNotification(StatusBarNotification sbn, Bundle extras)
    {
        this(sbn.getPackageName(),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_TITLE),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_TITLE_BIG),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_TEXT),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_BIG_TEXT),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_SUB_TEXT),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_INFO_TEXT),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_SUMMARY_TEXT),
                NotificationUtils.getNotificationInfoArray(extras, NotificationCompat.EXTRA_TEXT_LINES),
                NotificationUtils.getNotificationInfo(extras, NotificationCompat.EXTRA_TEMPLATE),
                NotificationUtils.getNotificationStringArray(extras, NotificationCompat.EXTRA_PEOPLE),
                sbn.getId());
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getBigNotificationTitle() {
        return bigNotificationTitle;
    }

    public void setBigNotificationTitle(String bigNotificationTitle) {
        this.bigNotificationTitle = bigNotificationTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getBigNotificationText() {
        return bigNotificationText;
    }

    public void setBigNotificationText(String bigNotificationText) {
        this.bigNotificationText = bigNotificationText;
    }

    public String getNotificationSubText() {
        return notificationSubText;
    }

    public void setNotificationSubText(String notificationSubText) {
        this.notificationSubText = notificationSubText;
    }

    public String getNotificationInfoText() {
        return notificationInfoText;
    }

    public void setNotificationInfoText(String notificationInfoText) {
        this.notificationInfoText = notificationInfoText;
    }

    public String getNotificationSummaryText() {
        return notificationSummaryText;
    }

    public void setNotificationSummaryText(String notificationSummaryText) {
        this.notificationSummaryText = notificationSummaryText;
    }

    public CharSequence[] getNotificationTextLines() {
        return notificationTextLines;
    }

    public void setNotificationTextLines(CharSequence[] notificationTextLines) {
        this.notificationTextLines = notificationTextLines;
    }

    public String getNotificationTemplate() {
        return notificationTemplate;
    }

    public void setNotificationTemplate(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }

    public String[] getNotificationPeople() {
        return notificationPeople;
    }

    public void setNotificationPeople(String[] notificationPeople) {
        this.notificationPeople = notificationPeople;
    }

    @Override
    public boolean equals(Object v) {

        if (v instanceof BasicNotification){

            BasicNotification ptr = (BasicNotification) v;

            if(ptr.getPackageName().equals(this.getPackageName()) &&
                    ptr.getNotificationId() == this.getNotificationId())
            {
                return true;
            }
            return false;
        }

        return false;
    }

}
