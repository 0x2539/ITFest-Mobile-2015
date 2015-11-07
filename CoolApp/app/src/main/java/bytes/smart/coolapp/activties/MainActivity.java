package bytes.smart.coolapp.activties;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.pojos.models.MainModel;
import bytes.smart.coolapp.views.MainLayout;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private MainModel model;
    private MainLayout layout;
    private MainLayout.ViewListener viewListener = new MainLayout.ViewListener() {
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