package bytes.smart.coolapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.MainModel;
import bytes.smart.coolapp.utils.ViewUtils;
import bytes.smart.coolapp.views.adapters.NotificationsAdapter;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MainLayout extends RelativeLayout implements OnChangeListener<MainModel> {

    private final String TAG = "MainLayout";

    private MainModel model;
    private ViewListener viewListener;

    private RelativeLayout notificationsRelativeLayout;
    private FloatingActionButton addNewNotificationFAB;

    private RelativeLayout emptyRelativeLayout;

    private RelativeLayout permissionRelativeLayout;
    private Button grantPermissionButton;
    private Button exitButton;

    private ListView notificationsListView;
    private NotificationsAdapter notificationsAdapter;

    private Toolbar toolbar;

    public interface ViewListener {
        void onAddNewNotificationsClicked();
        void onGrantPermissionClicked();
        void onExitClicked();
    }

    public MainLayout(Context context) {
        super(context);
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initLayout();
        initToolbar();
    }

    private void initLayout() {

        notificationsRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main_notifications_relativelayout);

        addNewNotificationFAB = (FloatingActionButton) findViewById(R.id.activity_main_add_new_notification_button);
        addNewNotificationFAB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onAddNewNotificationsClicked();
            }
        });

        emptyRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main_empty_layout);

        permissionRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main_permission_relative_layout);

        grantPermissionButton = (Button) findViewById(R.id.activity_main_grant_permission_button);
        grantPermissionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onGrantPermissionClicked();
            }
        });

        exitButton = (Button) findViewById(R.id.activity_main_exit_button);
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onExitClicked();
            }
        });

        notificationsListView = (ListView) findViewById(R.id.activity_main_notifications_listview);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar_layout);
        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(getContext(), getResources().getString(R.string.app_name), false);
    }

    private void updateView() {
        if(!getModel().isNotificationAccessEnabled())
        {
            permissionRelativeLayout.setVisibility(VISIBLE);
            notificationsRelativeLayout.setVisibility(GONE);
        }
        else
        {
            permissionRelativeLayout.setVisibility(GONE);
            notificationsRelativeLayout.setVisibility(VISIBLE);
        }

        if(notificationsAdapter == null)
        {
            notificationsAdapter = new NotificationsAdapter(getContext(), getModel().getNotificationRules());
            notificationsListView.setAdapter(notificationsAdapter);
        }
        else
        {
            notificationsAdapter.setCurrentItems(getModel().getNotificationRules());
            notificationsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChange() {
        updateView();
    }

    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
        this.model.addListener(this);
        updateView();
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

}
