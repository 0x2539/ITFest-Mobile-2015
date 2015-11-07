package bytes.smart.coolapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.MainModel;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MainLayout extends RelativeLayout implements OnChangeListener<MainModel> {

    private final String TAG = "MainLayout";

    private MainModel model;
    private ViewListener viewListener;

    private RelativeLayout permissionRelativeLayout;
    private Button grantPermissionButton;
    private Button exitButton;

    private AlertDialog grantPermissionDialog;

    public interface ViewListener {
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
    }

    private void initToolbar() {

    }

    private void updateView() {
        if(!getModel().isNotificationAccessEnabled())
        {
            permissionRelativeLayout.setVisibility(VISIBLE);
        }
        else
        {
            permissionRelativeLayout.setVisibility(GONE);
        }
    }

    public void showGrantPermissionDialog()
    {
        if(grantPermissionDialog != null &&
                grantPermissionDialog.isShowing())
        {
            return;
        }

        AlertDialog.Builder notificationPermissionDialogBuilder;
        notificationPermissionDialogBuilder = new AlertDialog.Builder(getContext());

        notificationPermissionDialogBuilder.setMessage(getResources().getString(R.string.grant_permission_dialog_message));
        notificationPermissionDialogBuilder.setTitle(getResources().getString(R.string.grant_permission_dialog_title));

        notificationPermissionDialogBuilder.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                getViewListener().onGrantPermissionDialogOkClicked();
                dialog.dismiss();
            }
        });

        notificationPermissionDialogBuilder.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        grantPermissionDialog = notificationPermissionDialogBuilder.create();
        grantPermissionDialog.show();
    }

    public boolean isGrantPermissionDialogVisible()
    {
        if(grantPermissionDialog != null) {
            return grantPermissionDialog.isShowing();
        }
        return false;
    }

    public void hideGrantPermissionDialog()
    {
        if(grantPermissionDialog != null) {
            grantPermissionDialog.dismiss();
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
