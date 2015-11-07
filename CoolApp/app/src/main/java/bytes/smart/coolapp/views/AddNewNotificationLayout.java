package bytes.smart.coolapp.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.AddNewNotificationModel;
import bytes.smart.coolapp.pojos.models.VibrateModel;
import bytes.smart.coolapp.utils.StringUtils;
import bytes.smart.coolapp.utils.ViewUtils;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class AddNewNotificationLayout extends RelativeLayout implements OnChangeListener<AddNewNotificationModel> {

    private final String TAG = "MainLayout";

    private AddNewNotificationModel model;
    private ViewListener viewListener;

    private TextInputLayout notificationTextInputLayout;
    private EditText notificationTextEditText;
    private TextInputLayout contactTextInputLayout;
    private AutoCompleteTextView contactAutoCompleteTextView;
    private TextInputLayout wakeUpTextInputLayout;
    private EditText wakeUpEditText;
    private TextInputLayout flashLightTextInputLayout;
    private EditText flashLightEditText;
    private LinearLayout vibratePatternLinearLayout;
    private TextView vibratePatternSetTextView;
    private LinearLayout defaultColorLinearLayout;
    private ImageView defaultColorImageView;

    private Toolbar toolbar;

    public interface ViewListener {
        void onDoneClicked();
        void onDeleteClicked();
        void saveDataToModel();
        void onVibratePatternClicked();
        void onGoBackToEditVibrateClicked(VibrateModel vibrateModel);
        void onPickDefaultColorClicked();
    }

    public AddNewNotificationLayout(Context context) {
        super(context);
    }

    public AddNewNotificationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddNewNotificationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initLayout();
        initToolbar();
    }

    private void initLayout() {
        notificationTextInputLayout = (TextInputLayout) findViewById(R.id.activity_add_new_notification_message_textinputlayout);
        notificationTextEditText = (EditText) findViewById(R.id.activity_add_new_notification_message_edittext);

        contactTextInputLayout = (TextInputLayout) findViewById(R.id.activity_add_new_notification_contact_textinputlayout);
        contactAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.activity_add_new_notification_contact_autocompletetextview);

        wakeUpTextInputLayout = (TextInputLayout) findViewById(R.id.activity_add_new_notification_wake_screen_textinputlayout);
        wakeUpEditText = (EditText) findViewById(R.id.activity_add_new_notification_wake_screen_edittext);

        flashLightTextInputLayout = (TextInputLayout) findViewById(R.id.activity_add_new_notification_flashlight_textinputlayout);
        flashLightEditText = (EditText) findViewById(R.id.activity_add_new_notification_flashlight_edittext);

        vibratePatternLinearLayout = (LinearLayout) findViewById(R.id.activity_add_new_notification_vibrate_layout);
        vibratePatternLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onVibratePatternClicked();
            }
        });
        vibratePatternSetTextView = (TextView) findViewById(R.id.activity_add_new_notification_vibrate_sub_textview);

        defaultColorLinearLayout = (LinearLayout) findViewById(R.id.activity_add_new_notification_pick_default_color_textview);
        defaultColorLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onPickDefaultColorClicked();
            }
        });
        defaultColorImageView = (ImageView) findViewById(R.id.activity_add_new_notification_choose_color_imageview);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_add_new_notification_toolbar_layout);
        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(getContext(), "New Notification", true);
    }

    private void updateView() {

        notificationTextEditText.setText(getModel().getMessage());
        contactAutoCompleteTextView.setText(getModel().getContact());
        wakeUpEditText.setText(getModel().getWakeUp());
        flashLightEditText.setText(getModel().getFlashlight());

        if(getModel().getVibratePattern() != null && getModel().getVibratePattern().size() > 0)
        {
            vibratePatternSetTextView.setText("Active");
        }
        else
        {
            vibratePatternSetTextView.setText("Not set");
        }

        defaultColorImageView.setColorFilter(getModel().getColor(), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onChange() {
        updateView();
    }

    public boolean validateInput() {
        boolean ok = true;
        if (StringUtils.trimStartEnd(notificationTextEditText.getText().toString()).equals("")) {
            notificationTextInputLayout.setError("Can't be empty");
            ok = false;
        }
        if (StringUtils.trimStartEnd(contactAutoCompleteTextView.getText().toString()).equals("")) {
            contactTextInputLayout.setError("Can't be empty");
            ok = false;
        }
        if (StringUtils.trimStartEnd(wakeUpEditText.getText().toString()).equals("")) {
            wakeUpTextInputLayout.setError("Can't be empty");
            ok = false;
        }
        if (StringUtils.trimStartEnd(flashLightEditText.getText().toString()).equals("")) {
            flashLightTextInputLayout.setError("Can't be empty");
            ok = false;
        }

        return ok;
    }

    public void showSnackBar(String message, String buttonText, final OnClickListener onClickListener)
    {
        Snackbar snack = Snackbar.make(vibratePatternLinearLayout, message, Snackbar.LENGTH_LONG);
        snack.setAction(buttonText, onClickListener);
        snack.setActionTextColor(getContext().getResources().getColor(R.color.snackbar_action_color));
        snack.show();
    }

    public String getMessage()
    {
        return notificationTextEditText.getText().toString();
    }

    public String getContact()
    {
        return contactAutoCompleteTextView.getText().toString();
    }

    public String getWakeUp()
    {
        return wakeUpEditText.getText().toString();
    }

    public String getFlashlight()
    {
        return flashLightEditText.getText().toString();
    }

    public AddNewNotificationModel getModel() {
        return model;
    }

    public void setModel(AddNewNotificationModel model) {
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
