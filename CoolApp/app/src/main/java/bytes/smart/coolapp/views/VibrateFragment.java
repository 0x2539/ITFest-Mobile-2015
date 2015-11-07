package bytes.smart.coolapp.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.managers.BlindManager;
import bytes.smart.coolapp.pojos.Vibration;
import bytes.smart.coolapp.pojos.models.ContactsFragmentModel;
import bytes.smart.coolapp.pojos.models.VibrateFragmentModel;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public class VibrateFragment extends TabbedRootFragment<VibrateFragmentLayout> implements TabbedFragment {

    private final String TAG = "ContactsFragment";

    int index = 0;

    private VibrateFragmentLayout layout;
    private VibrateFragmentModel model;
    private VibrateFragmentLayout.ViewListener viewListener = new VibrateFragmentLayout.ViewListener() {
        @Override
        public void onPlusClicked() {
            if(model.getVibrationTime() < 10000) {
                model.setVibrationTime(model.getVibrationTime() + 100);
            }

            Toast.makeText(getContext(), model.getVibrationTime() + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMinusClicked() {
            if(model.getVibrationTime() >= 100) {
                model.setVibrationTime(model.getVibrationTime() - 100);
            }

            Toast.makeText(getContext(), model.getVibrationTime() + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNextClicked() {
            model.setVibrationTime(0);
            BlindManager.getBlindManager().addVibration(new Vibration(index++, model.getVibrationTime(), 100));
        }

        @Override
        public void onPreviousClicked() {
//            BlindManager.getBlindManager().addVibration(new Vibration(index, model.getVibrationTime(), 100));
        }
    };

    private Activity activity;

    public VibrateFragment() {
//        initModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        activity = getActivity();
        initModel();

    }

    @Override
    public void onResume()
    {
        setHasOptionsMenu(false);
        super.onResume();
    }

    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected VibrateFragmentLayout getLayout() {
        return layout;
    }

    @Override
    public boolean consumeOnBackPressed() {
        return false;
    }

    @Override
    public void manageOrientationChange(boolean isInPortrait) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    public void save() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Log.v(TAG, "28.10.2014    Personal Frag onCreate started");
        layout = (VibrateFragmentLayout) inflater.inflate(R.layout.fragment_vibrate_blind, container, false);
        layout.setViewListener(viewListener);
        layout.setModel(model);

        Log.v(TAG, "28.10.2014    Personal Frag onCreate finished");

        return layout;
    }

    private void initModel()
    {
        model = new VibrateFragmentModel();
    }

}
