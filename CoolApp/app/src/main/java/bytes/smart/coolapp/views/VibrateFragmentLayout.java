package bytes.smart.coolapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.ContactsFragmentModel;
import bytes.smart.coolapp.pojos.models.VibrateFragmentModel;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public class VibrateFragmentLayout extends TabbedFragRootLayout implements OnChangeListener<VibrateFragmentModel> {

    private final String TAG = "VibrateFragmentLayout";

    private VibrateFragmentModel model;
    private ViewListener viewListener;

    private Button vibratePlusButton;
    private Button vibrateMinusButton;
    private Button previousButton;
    private Button nextButton;

    public interface ViewListener {
        void onPlusClicked();
        void onMinusClicked();
        void onNextClicked();
        void onPreviousClicked();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
    }

    private void initLayout()
    {
        vibratePlusButton = (Button) findViewById(R.id.fragment_vibrate_blind_plus_button);
        vibratePlusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onPlusClicked();
            }
        });

        vibrateMinusButton = (Button) findViewById(R.id.fragment_vibrate_blind_minus_button);
        vibrateMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onMinusClicked();
            }
        });

        previousButton = (Button) findViewById(R.id.fragment_vibrate_blind_previous_button);
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onPreviousClicked();
            }
        });

        nextButton = (Button) findViewById(R.id.fragment_vibrate_blind_next_button);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onNextClicked();
            }
        });
    }

    private void updateView()
    {
    }

    @Override
    public void onChange() {
        updateView();
    }

    public VibrateFragmentLayout(Context context) {
        super(context);
    }

    public VibrateFragmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VibrateFragmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public VibrateFragmentModel getModel() {
        return model;
    }

    public void setModel(VibrateFragmentModel model) {
        this.model = model;
        this.model.addListener(this);
        updateView();
    }


}
