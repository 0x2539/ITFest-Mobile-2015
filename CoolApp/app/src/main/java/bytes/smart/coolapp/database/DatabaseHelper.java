package bytes.smart.coolapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import bytes.smart.coolapp.pojos.NotificationRule;
import bytes.smart.coolapp.pojos.NotificationRuleApp;
import bytes.smart.coolapp.pojos.Vibration;
import bytes.smart.coolapp.utils.StringUtils;


/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHelper";

    protected static SQLiteDatabase mydb;
    public static final String DATABASE_NAME = "itfest.db";

    public static final String COLUMN_ID = "COLUMN_ID";

    /**
     * notifications table
     */
    public static final String TABLE_NOTIFICATIONS_NAME = "notifications";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "COLUMN_NOTIFICATION_MESSAGE";
    public static final String COLUMN_NOTIFICATION_CONTACT_NAME = "COLUMN_NOTIFICATION_CONTACT_NAME";
    public static final String COLUMN_NOTIFICATION_WAKE_UP_THE_SCREEN = "COLUMN_NOTIFICATION_WAKE_UP_THE_SCREEN";
    public static final String COLUMN_NOTIFICATION_FLASHLIGHT = "COLUMN_NOTIFICATION_FLASHLIGHT";
    public static final String COLUMN_NOTIFICATION_COLOR = "COLUMN_NOTIFICATION_COLOR";

    /**
     * custom apps table
     */
    public static final String TABLE_NOTIFICATION_APP_NAME = "notification_app";
    public static final String COLUMN_NOTIFICATION_APP_NOTIFICATION_ID = "COLUMN_NOTIFICATION_APP_NOTIFICATION_ID"; //the id of the notification from which it comes from
    public static final String COLUMN_NOTIFICATION_APP_PACKAGE_NAME = "COLUMN_NOTIFICATION_APP_PACKAGE_NAME";
    public static final String COLUMN_NOTIFICATION_APP_CONTACT_NAME = "COLUMN_NOTIFICATION_APP_CONTACT_NAME";
    public static final String COLUMN_NOTIFICATION_APP_COLOR = "COLUMN_NOTIFICATION_APP_COLOR";

    /**
     * vibrate patterns table
     */
    public static final String TABLE_VIBRATE_PATTERNS = "table_vibrate_patterns";
    public static final String COLUMN_VIBRATE_PATTERN_VIBRATE_TIME = "COLUMN_VIBRATE_PATTERN_VIBRATE_TIME";
    public static final String COLUMN_VIBRATE_PATTERN_PAUSE_TIME = "COLUMN_VIBRATE_PATTERN_PAUSE_TIME";
    public static final String COLUMN_VIBRATE_PATTERN_ORDER_INDEX = "COLUMN_VIBRATE_PATTERN_ORDER_INDEX";
    public static final String COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID = "COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID"; //the id of the notification from where it comes


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        initDB();
    }

    private void initDB() {
        mydb = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NOTIFICATIONS_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_NOTIFICATION_MESSAGE + " text, " +
                        COLUMN_NOTIFICATION_CONTACT_NAME + " text, " +
                        COLUMN_NOTIFICATION_WAKE_UP_THE_SCREEN + " integer, " +
                        COLUMN_NOTIFICATION_FLASHLIGHT + " integer, " +
                        COLUMN_NOTIFICATION_COLOR + " integer)"
        );

        db.execSQL(
                "create table " + TABLE_NOTIFICATION_APP_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_NOTIFICATION_APP_NOTIFICATION_ID + " long, " +
                        COLUMN_NOTIFICATION_APP_PACKAGE_NAME + " text, " +
                        COLUMN_NOTIFICATION_APP_CONTACT_NAME + " text, " +
                        COLUMN_NOTIFICATION_APP_COLOR + " integer)"
        );

        db.execSQL(
                "create table " + TABLE_VIBRATE_PATTERNS + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_VIBRATE_PATTERN_VIBRATE_TIME + " integer, " +
                        COLUMN_VIBRATE_PATTERN_PAUSE_TIME + " integer, " +
                        COLUMN_VIBRATE_PATTERN_ORDER_INDEX + " integer, " +
                        COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID + " integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS_NAME);
        onCreate(db);
    }

    public void dropDatabase() {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            // TODO Auto-generated method stub
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS_NAME);
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_APP_NAME);
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_VIBRATE_PATTERNS);

            onCreate(mydb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean insertVibrate(long notificationId, ArrayList<Vibration> vibratePattern) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            if (vibratePattern != null) {
                for (Vibration vibration : vibratePattern) {
                    ContentValues contentValues = getVibrationContentValue(notificationId, vibration);
                    mydb.insert(TABLE_VIBRATE_PATTERNS, null, contentValues);
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return false;
    }

    public ArrayList<Vibration> getVibratePattern(long notificationId) {
        if (!mydb.isOpen()) {
            initDB();
        }

        ArrayList<Vibration> vibrationArrayList = new ArrayList<>();
        try {
            Cursor cursor = mydb.query(TABLE_VIBRATE_PATTERNS,
                    null,
                    COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID + " = ?",
                    new String[]{String.valueOf(notificationId)},
                    null, null, COLUMN_VIBRATE_PATTERN_ORDER_INDEX); //order by index

            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
            Integer columnVibrateTime = cursor.getColumnIndex(COLUMN_VIBRATE_PATTERN_VIBRATE_TIME);
            Integer columnPauseTime = cursor.getColumnIndex(COLUMN_VIBRATE_PATTERN_PAUSE_TIME);
            Integer columnOrderIndex = cursor.getColumnIndex(COLUMN_VIBRATE_PATTERN_ORDER_INDEX);
//            Integer columnNotificationId = cursor.getColumnIndex(COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(columnIdIndex);
                    int vibrateTime = cursor.getInt(columnVibrateTime);
                    int pauseTime = cursor.getInt(columnPauseTime);
                    int orderIndex = cursor.getInt(columnOrderIndex);
//                    int notificationId = cursor.getInt(columnNotificationId);

                    vibrationArrayList.add(new Vibration(orderIndex, vibrateTime, pauseTime, notificationId, id));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }

        return vibrationArrayList;
    }

    private ContentValues getVibrationContentValue(long notificationId, Vibration vibration) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID, notificationId);
        contentValues.put(COLUMN_VIBRATE_PATTERN_ORDER_INDEX, vibration.getIndex());
        contentValues.put(COLUMN_VIBRATE_PATTERN_VIBRATE_TIME, vibration.getVibrateTime());
        contentValues.put(COLUMN_VIBRATE_PATTERN_PAUSE_TIME, vibration.getPauseTime());

        return contentValues;
    }

    public Integer deleteVibratePattern(long notificationId) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            mydb.delete(TABLE_VIBRATE_PATTERNS,
                    COLUMN_VIBRATE_PATTERN_NOTIFICATION_ID + " = ? ",
                    new String[]{String.valueOf(notificationId)});
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return -1;
    }

    public long insertNotification(NotificationRule notificationRule) {

        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            ContentValues contentValues = getNotificationRuleContentValue(notificationRule);

            long rowId = mydb.insert(TABLE_NOTIFICATIONS_NAME, null, contentValues);

            insertVibrate(rowId, notificationRule.getVibratePattern());

            if (notificationRule.getNotificationRuleAppsList() != null) {
                for (NotificationRuleApp notificationRuleApp : notificationRule.getNotificationRuleAppsList()) {
                    insertNotificationApp(rowId, notificationRuleApp);
                }
            }

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return -1;
    }

    public NotificationRule getNotificationRule(long notificationId) {
        if (!mydb.isOpen()) {
            initDB();
        }

        try {
            Cursor cursor;
                cursor = mydb.query(TABLE_NOTIFICATIONS_NAME,
                        null,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(notificationId)
                        },
                        null, null, null);

            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
            Integer columnMessageIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_MESSAGE);
            Integer columnSenderNameIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_CONTACT_NAME);
            Integer columnWakeUpIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_WAKE_UP_THE_SCREEN);
            Integer columnFlashlightIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_FLASHLIGHT);
            Integer columnColorIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_COLOR);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(columnIdIndex);
                    String message = cursor.getString(columnMessageIndex);
                    String sender = cursor.getString(columnSenderNameIndex);
                    int wakeUp = cursor.getInt(columnWakeUpIndex);
                    int flashlight = cursor.getInt(columnFlashlightIndex);
                    int color = cursor.getInt(columnColorIndex);

                    return new NotificationRule(message, sender, wakeUp, flashlight, 
                            color, getVibratePattern(id),
                            getAllNotificationRulesApp(id), id);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }

        return null;
    }

    public ArrayList<NotificationRule> getAllNotificationRules() {
        if (!mydb.isOpen()) {
            initDB();
        }

        ArrayList<NotificationRule> notificationRules = new ArrayList<>();
        try {
            Cursor cursor = mydb.query(TABLE_NOTIFICATIONS_NAME,
                    null, null, null, null, null, null);

            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
            Integer columnMessageIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_MESSAGE);
            Integer columnSenderNameIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_CONTACT_NAME);
            Integer columnWakeUpIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_WAKE_UP_THE_SCREEN);
            Integer flashlightIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_FLASHLIGHT);
            Integer columnColorIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_COLOR);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(columnIdIndex);
                    String message = cursor.getString(columnMessageIndex);
                    String sender = cursor.getString(columnSenderNameIndex);
                    int wakeUp = cursor.getInt(columnWakeUpIndex);
                    int flashlight = cursor.getInt(flashlightIndex);
                    int color = cursor.getInt(columnColorIndex);

                    notificationRules.add(new NotificationRule(message, sender, wakeUp, flashlight,
                            color, getVibratePattern(id),
                            getAllNotificationRulesApp(id), id));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }

        return notificationRules;
    }

    public boolean updateNotificationRule(long notificationId, NotificationRule newNotificationRule) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            ContentValues contentValues = getNotificationRuleContentValue(newNotificationRule);

            boolean success = true;
            if (mydb.update(TABLE_NOTIFICATIONS_NAME, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(notificationId)}) <= 0) {
                Log.e("database", newNotificationRule.getDefaultSender() + " not found: " + notificationId);
                success = false;
                return false;
            }
            deleteVibratePattern(notificationId);
            insertVibrate(notificationId, newNotificationRule.getVibratePattern());


            if (newNotificationRule.getNotificationRuleAppsList() != null) {
                for (NotificationRuleApp notificationRuleApp : newNotificationRule.getNotificationRuleAppsList()) {
                    ContentValues contentValuesApp = getNotificationRuleAppContentValue(notificationId, notificationRuleApp);

                    if (mydb.update(TABLE_NOTIFICATION_APP_NAME, contentValuesApp, COLUMN_ID + " = ? ", new String[]{String.valueOf(notificationRuleApp.getId())}) <= 0) {
                        Log.e("database", notificationRuleApp.getSender() + " doesn't exist, inserting now " + notificationRuleApp.getId());
                        insertNotificationApp(notificationId, notificationRuleApp);
                    }
                }
            }
            return success;

        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return false;
    }

    private ContentValues getNotificationRuleContentValue(NotificationRule notificationRule) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOTIFICATION_MESSAGE, StringUtils.trimStartEnd(notificationRule.getNotificationMessage()));
        contentValues.put(COLUMN_NOTIFICATION_CONTACT_NAME, StringUtils.trimStartEnd(notificationRule.getDefaultSender()).toLowerCase());
        contentValues.put(COLUMN_NOTIFICATION_WAKE_UP_THE_SCREEN, notificationRule.getWakeUpTheScreen());
        contentValues.put(COLUMN_NOTIFICATION_FLASHLIGHT, notificationRule.getFlashLightTime());
        contentValues.put(COLUMN_NOTIFICATION_COLOR, notificationRule.getDefaultColor());

        return contentValues;
    }

    public Integer deleteNotificationRule(NotificationRule notificationRule) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            mydb.delete(TABLE_NOTIFICATIONS_NAME,
                    COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(notificationRule.getId())});

            deleteVibratePattern(notificationRule.getId());
            deleteNotificationRuleApp(notificationRule);

        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return -1;
    }

    public boolean insertNotificationApp(long rowId, NotificationRuleApp notificationRuleApp) {

        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            ContentValues contentValues = getNotificationRuleAppContentValue(rowId, notificationRuleApp);

            long rowAppId = mydb.insert(TABLE_NOTIFICATION_APP_NAME, null, contentValues);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return false;
    }

    public ArrayList<NotificationRuleApp> getAllNotificationRulesApp(long notificationRuleId) {
        if (!mydb.isOpen()) {
            initDB();
        }

        ArrayList<NotificationRuleApp> notificationRuleApps = new ArrayList<>();
        try {
            Cursor cursor = mydb.query(TABLE_NOTIFICATION_APP_NAME,
                    null,
                    COLUMN_NOTIFICATION_APP_NOTIFICATION_ID + " = ?",
                    new String[]{String.valueOf(notificationRuleId)},
                    null, null, null);

            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
            Integer columnSenderNameIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_APP_CONTACT_NAME);
            Integer columnPackageNameIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_APP_PACKAGE_NAME);
            Integer columnColorIndex = cursor.getColumnIndex(COLUMN_NOTIFICATION_APP_COLOR);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(columnIdIndex);
                    String sender = cursor.getString(columnSenderNameIndex);
                    String packageName = cursor.getString(columnPackageNameIndex);
                    int color = cursor.getInt(columnColorIndex);

                    notificationRuleApps.add(new NotificationRuleApp(color, sender, packageName, id, notificationRuleId));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }

        return notificationRuleApps;
    }

    private ContentValues getNotificationRuleAppContentValue(long rowId, NotificationRuleApp notificationRuleApp) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOTIFICATION_APP_NOTIFICATION_ID, rowId);
        contentValues.put(COLUMN_NOTIFICATION_APP_PACKAGE_NAME, notificationRuleApp.getPackageName());
        contentValues.put(COLUMN_NOTIFICATION_APP_CONTACT_NAME, StringUtils.trimStartEnd(notificationRuleApp.getSender()).toLowerCase());
        contentValues.put(COLUMN_NOTIFICATION_APP_COLOR, notificationRuleApp.getColor());

        return contentValues;
    }

    public Integer deleteNotificationRuleApp(NotificationRule notificationRule) {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            mydb.delete(TABLE_NOTIFICATION_APP_NAME,
                    COLUMN_NOTIFICATION_APP_NOTIFICATION_ID + " = ? ",
                    new String[]{String.valueOf(notificationRule.getId())});

        } catch (Exception e) {
            e.printStackTrace();
            mydb.close();
        }
        return -1;
    }
}
