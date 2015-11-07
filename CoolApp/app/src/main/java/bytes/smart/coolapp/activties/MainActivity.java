package bytes.smart.coolapp.activties;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.database.DatabaseHelper;
import bytes.smart.coolapp.managers.NotificationRulesManager;
import bytes.smart.coolapp.pojos.models.MainModel;
import bytes.smart.coolapp.services.AppNotificationService;
import bytes.smart.coolapp.utils.Constants;
import bytes.smart.coolapp.views.MainLayout;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private MainModel model;
    private MainLayout layout;
    private MainLayout.ViewListener viewListener = new MainLayout.ViewListener() {
        @Override
        public void onAddNewNotificationsClicked() {
            Intent intent = new Intent(MainActivity.this, AddNewNotificationActivity.class);
            startActivity(intent);
        }

        @Override
        public void onGrantPermissionClicked() {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        @Override
        public void onExitClicked() {
            finish();
        }

        @Override
        public void onNotificationClicked(int position) {
            Log.e(TAG, "on notification clicked: " + position);
            Intent intent = new Intent(MainActivity.this, AddNewNotificationActivity.class);
            intent.putExtra(Constants.ADD_OR_EDIT_NOTIFICATION, (Serializable) model.getNotificationRules().get(position));
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initModel();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new MainModel();
    }

    private void initLayout() {
        layout = (MainLayout) View.inflate(this, R.layout.activity_main, null);

        layout.setViewListener(viewListener);
        layout.setModel(model);
    }

    private void setStatusBarColorToLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void setStatusBarColorToDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppNotificationService.isNotificationAccessEnabled)
        {
//            layout.hideGrantPermissionDialog();
            setStatusBarColorToDark();
        }
        else{
            setStatusBarColorToLight();
        }

        NotificationRulesManager.getNotificationRulesManager().setNotificationRules(new DatabaseHelper(this).getAllNotificationRules());

        model.setNotificationRules(NotificationRulesManager.getNotificationRulesManager().getNotificationRules());

        model.setNotificationAccessEnabled(AppNotificationService.isNotificationAccessEnabled, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
