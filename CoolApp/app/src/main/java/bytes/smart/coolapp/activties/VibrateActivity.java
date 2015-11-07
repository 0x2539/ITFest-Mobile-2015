package bytes.smart.coolapp.activties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.pojos.models.VibrateModel;
import bytes.smart.coolapp.utils.Constants;
import bytes.smart.coolapp.utils.VibrateUtils;
import bytes.smart.coolapp.views.VibrateLayout;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class VibrateActivity extends AppCompatActivity {

    private final String TAG = "VibrateActivity";

    private VibrateModel rootModel;
    private VibrateLayout rootLayout;

    private Activity activity;
    private Menu optionsMenu;

    private VibrateLayout.ViewListener viewListener = new VibrateLayout.ViewListener() {

        @Override
        public void onAddNewClicked() {
            rootLayout.addNewVibrateCard();
        }

        @Override
        public void onDoneClicked() {
            if (!rootLayout.clickedDone()) {
                return;
            }

            saveDataToModel();
            Intent intent = new Intent(activity, AddNewNotificationActivity.class);
            intent.putExtra(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST, rootModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        @Override
        public void onPlayClicked() {
            if (rootLayout.clickedDone()) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                VibrateUtils.playStopVibratePattern(activity, vibrator, rootLayout.getVibratePattern(), optionsMenu);
            }
        }

        @Override
        public void saveDataToModel() {
            rootModel.setVibrations(rootLayout.getVibratePattern());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = VibrateActivity.this;

        initModel();

        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null)
            {
                VibrateModel newModel = (VibrateModel) extras.getSerializable(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST);

                if(newModel != null)
                {
                    rootModel = newModel;
                }
                else
                {
                    return;
                }
            }
        }

        if (savedInstanceState != null) {
            getDataFromSavedInstance(savedInstanceState);
        }

        rootLayout = initLayout();
        rootLayout.setViewListener(viewListener);
        setContentView(rootLayout);
        rootLayout.setModel(rootModel);
    }

    private void getDataFromSavedInstance(Bundle savedInstanceState) {
        rootModel = (VibrateModel) savedInstanceState.getSerializable(TAG);
//        rootModel.setVibrations(GeneralManager.getGeneralManager().getVibrateManager().getVibrations());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewListener.saveDataToModel();
        outState.putSerializable(TAG, rootModel);
    }


    @Override
    public void onResume() {
        super.onResume();
        VibrateUtils.setPlayIcon(activity, optionsMenu, R.id.menu_vibrate_play_action);
    }

    @Override
    public void onPause() {
        super.onPause();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrateUtils.stopVibratePattern(activity, optionsMenu, vibrator);
    }

    private VibrateLayout initLayout() {
        VibrateLayout layout = (VibrateLayout) View.inflate(this, R.layout.activity_vibrate, null);

        return layout;
    }

    private void initModel() {
        rootModel = new VibrateModel();
//        rootModel.setVibrations(GeneralManager.getGeneralManager().getVibrateManager().getVibrations());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_vibrate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.menu_vibrate_play_action) {
            viewListener.onPlayClicked();
        }

        if (id == R.id.menu_vibrate_done_action) {
            viewListener.onDoneClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        viewListener.saveDataToModel();
        Intent intent = new Intent(activity, AddNewNotificationActivity.class);
        intent.putExtra(Constants.ADD_OR_EDIT_NOTIFICATION_VIBRATE_LIST_EDITED, rootModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }
}
