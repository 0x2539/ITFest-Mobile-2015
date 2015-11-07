package bytes.smart.coolapp.activties;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.database.DatabaseHelper;
import bytes.smart.coolapp.managers.ContactsManager;
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

        permissions();

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
            Intent intent = new Intent(this, AddNewNotificationBlindActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;

    private void permissions()
    {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else{
            ContactsManager.getContactsManager(this).loadContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ContactsManager.getContactsManager(this).loadContacts();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
