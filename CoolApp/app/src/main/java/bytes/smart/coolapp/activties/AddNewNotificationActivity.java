package bytes.smart.coolapp.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.database.DatabaseHelper;
import bytes.smart.coolapp.managers.NotificationRulesManager;
import bytes.smart.coolapp.pojos.NotificationRule;
import bytes.smart.coolapp.pojos.models.AddNewNotificationModel;
import bytes.smart.coolapp.pojos.models.MVCModel;
import bytes.smart.coolapp.pojos.models.VibrateModel;
import bytes.smart.coolapp.services.AppNotificationService;
import bytes.smart.coolapp.utils.Constants;
import bytes.smart.coolapp.utils.Utils;
import bytes.smart.coolapp.views.AddNewNotificationLayout;
import bytes.smart.coolapp.views.MVCLayout;
import bytes.smart.coolapp.views.dialogs.ColorPickerDialog;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class AddNewNotificationActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private AddNewNotificationModel model;
    private AddNewNotificationLayout layout;
    private AddNewNotificationLayout.ViewListener viewListener = new AddNewNotificationLayout.ViewListener() {
        @Override
        public void onDoneClicked() {
            if (!layout.validateInput()) {
                return;
            }
            saveDataToModel();

            DatabaseHelper databaseHelper = new DatabaseHelper(AddNewNotificationActivity.this);

            NotificationRule notificationRule = new NotificationRule(
                    model.getMessage(), model.getContact(),
                    Integer.parseInt(model.getWakeUp()),
                    Integer.parseInt(model.getFlashlight()), model.getColor(),
                    model.getVibratePattern(), model.getRulesList());

            //in case we updated the notification while we are in the edit screen
            NotificationRule updatedNotificationRule = databaseHelper.getNotificationRule(model.getId());

            if (!model.isInEditMode() || updatedNotificationRule == null) {
                long newRowId = databaseHelper.insertNotification(notificationRule);
                if (newRowId != -1) {
                    notificationRule.setId(newRowId);
                    AppNotificationService.newNotificationListenerCreated(AddNewNotificationActivity.this, notificationRule);//.updateNotificationRules(AddNewNotificationActivity.this);

                    AddNewNotificationActivity.this.finish();
                } else {
                    Toast.makeText(AddNewNotificationActivity.this, "an error occured while saving", Toast.LENGTH_LONG).show();
                }
            } else {
                notificationRule.setId(model.getId());

                if (databaseHelper.updateNotificationRule(model.getId(), notificationRule)) {
                    AppNotificationService.notificationListenerUpdated(AddNewNotificationActivity.this, notificationRule);//.updateNotificationRules(AddNewNotificationActivity.this);

                    AddNewNotificationActivity.this.finish();
                } else {
                    Toast.makeText(AddNewNotificationActivity.this, "an error occured while updating", Toast.LENGTH_LONG).show();
                }
            }

            NotificationRulesManager.getNotificationRulesManager().getNotificationRules().clear();
            NotificationRulesManager.getNotificationRulesManager().setNotificationRules(databaseHelper.getAllNotificationRules());

        }

        @Override
        public void onDeleteClicked() {
            saveDataToModel();

//            final NotificationRule deletedNotificationRule = GeneralManager.getGeneralManager().getBasicNotificationsManager(activity).getNotificationRules().get(rootModel.getIndexOfNotification());

            DatabaseHelper databaseHelper = new DatabaseHelper(AddNewNotificationActivity.this);
            final NotificationRule deletedNotificationRule = databaseHelper.getNotificationRule(model.getId());
            AppNotificationService.notificationListenerRemoved(deletedNotificationRule);
            databaseHelper.deleteNotificationRule(deletedNotificationRule);

            Intent intent = new Intent(AddNewNotificationActivity.this, MainActivity.class);
//            intent.putExtra(Constants.DELETED_NOTIFICATION_OBJECT, (Serializable) deletedNotificationRule);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        @Override
        public void saveDataToModel() {
            model.setMessage(layout.getMessage());
            model.setContact(layout.getContact());
            model.setWakeUp(layout.getWakeUp());
            model.setFlashlight(layout.getFlashlight());

            Log.e(TAG, "model data: " + layout.getMessage() + " " + layout.getContact() + " " + layout.getWakeUp() + " " + layout.getFlashlight());
        }

        @Override
        public void onVibratePatternClicked() {

            saveDataToModel();

//            GeneralManager.getGeneralManager().getVibrateManager().setVibrations(rootModel.getVibratePattern());
            Intent intent = new Intent(AddNewNotificationActivity.this, VibrateActivity.class);
            VibrateModel vibrateModel = new VibrateModel();
            vibrateModel.setVibrations(model.getVibratePattern());
            intent.putExtra(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST, vibrateModel);
            startActivity(intent);
        }

        @Override
        public void onGoBackToEditVibrateClicked(VibrateModel vibrateModel) {

            saveDataToModel();

//            GeneralManager.getGeneralManager().getVibrateManager().setVibrations(rootModel.getVibratePattern());
            Intent intent = new Intent(AddNewNotificationActivity.this, VibrateActivity.class);
            intent.putExtra(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST, vibrateModel);
            startActivity(intent);
        }

        @Override
        public void onPickDefaultColorClicked() {

            saveDataToModel();

            final ColorPickerDialog.Builder colorPickerDialog = new ColorPickerDialog.Builder(AddNewNotificationActivity.this);
            colorPickerDialog.setOnColorClicked(new Runnable() {
                @Override
                public void run() {
                    model.setColor(colorPickerDialog.getPickedColor(), true);
                }
            });
            colorPickerDialog.build().show();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initModel();
        getExtras();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new AddNewNotificationModel();
    }

    private void initLayout() {
        layout = (AddNewNotificationLayout) View.inflate(this, R.layout.activity_add_new_notification, null);

        layout.setViewListener(viewListener);
        layout.setModel(model);
    }

    private void getExtras()
    {
        Intent intent = getIntent();
        Log.e(TAG, "get extras");

        if(intent.getExtras() != null)
        {
            Log.e(TAG, "extras not null");
            NotificationRule notificationRule = (NotificationRule) intent.getExtras().getSerializable(Constants.ADD_OR_EDIT_NOTIFICATION);

            if(notificationRule != null) {
                Log.e(TAG, "notification rule not null");
                model.populateModel(notificationRule);
                model.setIsInEditMode(notificationRule.getId() != -1);
                return;
            }
        }

        model.setIsInEditMode(false);
        model.setColor(getResources().getColor(R.color.custom_color_1));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (null != intent) {
            Bundle extras = intent.getExtras();
            if(extras != null)
            {
                AddNewNotificationModel newModel = (AddNewNotificationModel) extras.getSerializable(Constants.ADD_OR_EDIT_NOTIFICATION);
                NotificationRule notificationRule = (NotificationRule) extras.getSerializable(Constants.ADD_OR_EDIT_NOTIFICATION_RULES_LIST);
                VibrateModel vibrateModel = (VibrateModel) extras.getSerializable(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST);
                final VibrateModel vibrateModelEdited = (VibrateModel) extras.getSerializable(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST_EDITED);

                if(newModel != null)
                {
                    Log.i(TAG, "extra: new model");
                    model = newModel;
                }
                if(notificationRule != null)
                {
                    Log.i(TAG, "extra: notification rule " + notificationRule.getNotificationRuleAppsList().size());
                    model.setRulesList(NotificationRulesManager.getNotificationRulesManager().getNotificationRuleApps());
//                    rootModel.setRulesList(notificationRule.getNotificationRuleAppsList());
                }
                if(vibrateModel != null)
                {
                    Log.i(TAG, "extra: vibrate model");
                    model.setVibratePattern(vibrateModel.getVibrations(), true);
                }
                else
                if(vibrateModelEdited != null)
                {
                    Log.i(TAG, "extra: vibrate edited");
                    //do it on post, because the showing animation will be smoother
                    layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.showSnackBar("Changes were not saved", "GO BACK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewListener.onGoBackToEditVibrateClicked(vibrateModelEdited);
                                }
                            });
                        }
                    }, 300);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_notification, menu);

        MenuItem deleteButton = menu.findItem(R.id.action_add_new_notification_delete);
        if(model.isInEditMode())
        {
            deleteButton.setVisible(true);
        }
        else
        {
            deleteButton.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.action_add_new_notification_done) {
            viewListener.onDoneClicked();
        }
        if (id == R.id.action_add_new_notification_delete) {
            viewListener.onDeleteClicked();
        }

        return super.onOptionsItemSelected(item);
    }
}
