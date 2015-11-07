package bytes.smart.coolapp.views;

import android.content.Context;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.Vibration;
import bytes.smart.coolapp.pojos.models.VibrateModel;
import bytes.smart.coolapp.utils.Constants;
import bytes.smart.coolapp.utils.ViewUtils;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class VibrateLayout extends RelativeLayout implements OnChangeListener<VibrateModel> {

    private final static String TAG = "VibrateLayout";

    /**
     * The main model.
     */
    private VibrateModel rootModel;
    /**
     * The listener.
     */
    private ViewListener viewListener;
    private LayoutInflater inflater;
    private Vibrator vibrator;
    private FloatingActionButton addVibrationButton;
    private ScrollView vibrationsScrollView;

    private LinearLayout emptyLayout;

    private Toolbar toolbar;
    private LinearLayout vibratePatternContainer;
    private ArrayList<VibratePatternLayout> vibratePatternLayouts = new ArrayList<>();
    private ArrayList<Vibration> vibrationArrayList;

    private int totalValidVibrationLayouts;


    public VibrateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        totalValidVibrationLayouts = 1;
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        inflater = (LayoutInflater)getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        addVibrationButton = (FloatingActionButton) findViewById(R.id.activity_vibrate_add_vibration_button);
        addVibrationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onAddNewClicked();

                emptyLayout.setVisibility(GONE);
            }
        });

        emptyLayout = (LinearLayout) findViewById(R.id.activity_vibrate_empty_layout);

        vibratePatternContainer = (LinearLayout) findViewById(R.id.activity_vibrate_vibrations_container);

        vibrationsScrollView = (ScrollView) findViewById(R.id.activity_vibrate_vibrations_scrollview);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.activity_vibrate_toolbar_layout);
        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(getContext(), "Vibrate Pattern");
    }

    public VibrateModel getModel() {
        return rootModel;
    }

    public void setModel(VibrateModel model) {
        this.rootModel = model;
        this.rootModel.addListener(this);
        updateView();
    }

    public void updateView() {

        vibrationArrayList = getModel().getVibrations();//GeneralManager.getGeneralManager().getVibrateManager().getVibrations();
        if(vibrationArrayList == null || vibrationArrayList.size() == 0)
        {
            emptyLayout.setVisibility(VISIBLE);
        }
        else{
            for (Vibration vibration : vibrationArrayList) {
                addNewVibrate(vibration.getVibrateTime(), vibration.getPauseTime());
            }
            emptyLayout.setVisibility(GONE);
        }
    }

    public void scrollVibrationsListToBottom()
    {
        //we need to run the scroll on the ui thread because we would scroll
        //to bottom before the view was added, and this will result in an
        //unsuccessful scroll
        vibrationsScrollView.post(new Runnable() {
            @Override
            public void run() {
                vibrationsScrollView.fullScroll(FOCUS_DOWN);
                vibratePatternLayouts.get(vibratePatternLayouts.size() - 1).getVibrateTimeEditText().requestFocus();
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(vibratePatternLayouts.get(vibratePatternLayouts.size() - 1).getVibrateTimeEditText(), InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
    private void addNewVibrate(long vibrateTime, long pauseTime)
    {
        vibratePatternLayouts.add(new VibratePatternLayout(vibrateTime, pauseTime));
    }

    public void addNewVibrateCard()
    {
        vibratePatternLayouts.add(new VibratePatternLayout());
        scrollVibrationsListToBottom();
    }

    public boolean clickedDone()
    {
        return validateInput();
    }

    private boolean validateInput() {
        boolean valid = true;
        for (VibratePatternLayout vibratePatternLayout : vibratePatternLayouts) {
            //send !valid as parameter because valid will be true until we find a problematic
            //edittext, and when we find it we will tell it that we want to show the
            //error message (because valid is true, and only after we get the time, valid will
            //become false)
            if (vibratePatternLayout.getVibrateTime(valid) == -1) {
                valid = false;
            }
            //we do separate ifs, because we want to show the error for both edittexts, in case
            //both of them have errors
            if (vibratePatternLayout.getPauseTime(valid) == -1) {
                valid = false;
            }
        }
        return valid;
    }

    public ArrayList<Vibration> getVibratePattern()
    {
        ArrayList<Vibration> vibrationArrayList = new ArrayList<>();
        for(int i = 0; i < vibratePatternLayouts.size(); i++) {
            vibrationArrayList.add(
                    new Vibration(
                            i,
                            vibratePatternLayouts.get(i).getVibrateTime(),
                            vibratePatternLayouts.get(i).getPauseTime()
                    )
            );
        }
        return vibrationArrayList;
    }

    private void refreshVibrationIndexesTextViews()
    {
        totalValidVibrationLayouts = 1;
        for(VibratePatternLayout vibratePatternLayout : vibratePatternLayouts) {
            vibratePatternLayout.setVibrationIndex(totalValidVibrationLayouts++);
        }
        if(vibratePatternLayouts.size() != 0)
        {
            emptyLayout.setVisibility(GONE);
        }
        else{
            emptyLayout.setVisibility(VISIBLE);
        }
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    private class VibratePatternLayout {
        private TextInputLayout vibrateTimeTextInputLayout;
        private EditText vibrateTimeEditText;
        private TextInputLayout pauseTimeTextInputLayout;
        private EditText pauseTimeEditText;
        private ImageButton removeButton;
        private TextView indexTextView;
        private View vibratePatternView;

        private final int[] ERRORS = {
                0,
                R.string.gen_edittext_cant_be_empty,
                R.string.gen_vibration_cant_be_negative,
                R.string.gen_vibration_cant_be_higher_than
        };
        private final int ERROR_CANT_BE_EMPTY = -1;
        private final int ERROR_CANT_BE_NEGATIVE = -2;
        private final int ERROR_CANT_BE_HIGHER = -3;


        public VibratePatternLayout()
        {
            vibratePatternView = inflater.inflate(R.layout.vibrate_item_layout, null);
            vibratePatternContainer.addView(vibratePatternView);

            vibrateTimeTextInputLayout = (TextInputLayout) vibratePatternView.findViewById(R.id.vibrate_item_layout_vibrate_textinputlayout);;
            vibrateTimeTextInputLayout.setErrorEnabled(true);
            vibrateTimeEditText = (EditText) vibratePatternView.findViewById(R.id.vibrate_item_layout_vibrate_edittext);
            vibrateTimeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            vibrateTimeEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

            pauseTimeTextInputLayout = (TextInputLayout) vibratePatternView.findViewById(R.id.vibrate_item_layout_pause_textinputlayout);;
            pauseTimeTextInputLayout.setErrorEnabled(true);
            pauseTimeEditText = (EditText) vibratePatternView.findViewById(R.id.vibrate_item_layout_pause_edittext);
            pauseTimeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            pauseTimeEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

            indexTextView = (TextView) vibratePatternView.findViewById(R.id.vibrate_item_layout_index_textview);
            indexTextView.setText(totalValidVibrationLayouts++ + "");

            removeButton = (ImageButton) vibratePatternView.findViewById(R.id.vibrate_item_layout_remove_button);
            final VibratePatternLayout currentObject = this;
            removeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibratePatternContainer.removeView(vibratePatternView);

                    vibratePatternLayouts.remove(currentObject);

                    refreshVibrationIndexesTextViews();
                }
            });
        }

        public VibratePatternLayout(final long vibrateTime, final long pauseTime) {
            this();
            this.vibrateTimeEditText.post(new Runnable() {
                @Override
                public void run() {
                    vibrateTimeEditText.setText(vibrateTime + "");
                }
            });
            this.pauseTimeEditText.post(new Runnable() {
                @Override
                public void run() {
                    pauseTimeEditText.setText(pauseTime + "");
                }
            });
        }
        public EditText getVibrateTimeEditText() {
            return vibrateTimeEditText;
        }

        public void setVibrateTimeEditText(EditText vibrateTimeEditText) {
            this.vibrateTimeEditText = vibrateTimeEditText;
        }

        public EditText getPauseTimeEditText() {
            return pauseTimeEditText;
        }

        public void setPauseTimeEditText(EditText pauseTimeEditText) {
            this.pauseTimeEditText = pauseTimeEditText;
        }

        public ImageButton getRemoveButton() {
            return removeButton;
        }

        public void setRemoveButton(ImageButton removeButton) {
            this.removeButton = removeButton;
        }

        public int getVibrateTime(boolean... requestFocus) {
            int time = getIntFromEditText(getVibrateTimeEditText());
            if(time < 0)
            {
                vibrateTimeTextInputLayout.setError(getContext().getResources().getString(ERRORS[-time]));
                if(requestFocus.length > 0 && requestFocus[0])
                {
                    getVibrateTimeEditText().requestFocus();
                }
                return -1;
            }
            return time;
        }

        public int getPauseTime(boolean... requestFocus) {
            int time = getIntFromEditText(getPauseTimeEditText());
            if(time < 0)
            {
                pauseTimeTextInputLayout.setError(getContext().getResources().getString(ERRORS[-time]));
                if(requestFocus.length > 0 && requestFocus[0])
                {
                    getPauseTimeEditText().requestFocus();
                }
                return -1;
            }
            return time;
        }

        private int getIntFromEditText(EditText editText) {
            if (editText != null && editText.getText().toString().length() > 0) {
                int time = -1;

                //the user may write a number that is higher then int
                try {
                    time = Integer.parseInt(editText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (time >= 0 && time <= 100000) {
                    return time;
                } else {
                    if (time < 0) {
                        return ERROR_CANT_BE_NEGATIVE;
//                        editText.setError(getContext().getString(R.string.gen_vibration_cant_be_negative));
                    } else if (time > 100000) {
                        return ERROR_CANT_BE_HIGHER;
//                        editText.setError(getContext().getString(R.string.gen_vibration_cant_be_higher_than));
                    }
                }
            } else {
                if (editText != null) {
                    return ERROR_CANT_BE_EMPTY;
//                    editText.setError(getContext().getResources().getString(R.string.gen_edittext_cant_be_empty));
                }
            }
            return ERROR_CANT_BE_EMPTY;
        }

        public void setVibrationIndex(int index)
        {
            indexTextView.setText(index + "");
        }
    }

    @Override
    public void onChange() {
        updateView();
    }

    /**
     * The listener interface for receiving view events. The class that is interested in processing a view event
     * implements this interface, and the object created with that class is registered with a component using the
     * component's <code>addViewListener<code> method. When
     * the view event occurs, that object's appropriate
     * method is invoked.
     */
    public interface ViewListener {
        void onAddNewClicked();
        void onDoneClicked();
        void onPlayClicked();
        void saveDataToModel();
    }
}
