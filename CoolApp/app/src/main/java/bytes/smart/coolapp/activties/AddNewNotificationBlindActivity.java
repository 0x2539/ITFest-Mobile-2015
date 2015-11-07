package bytes.smart.coolapp.activties;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.managers.BlindManager;
import bytes.smart.coolapp.pojos.Vibration;
import bytes.smart.coolapp.pojos.models.MVCModel;
import bytes.smart.coolapp.views.AddNewNotificationBlindLayout;
import bytes.smart.coolapp.views.MVCLayout;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class AddNewNotificationBlindActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private MVCModel model;
    private AddNewNotificationBlindLayout layout;
    private AddNewNotificationBlindLayout.ViewListener viewListener = new AddNewNotificationBlindLayout.ViewListener() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BlindManager.getBlindManager().setVibrations(new ArrayList<Vibration>());
        BlindManager.getBlindManager().setContact("");

        initModel();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new MVCModel();
    }

    private void initLayout() {
        layout = (AddNewNotificationBlindLayout) View.inflate(this, R.layout.activity_add_new_notification_blind, null);

        layout.setViewListener(viewListener);
        layout.setModel(model);
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
