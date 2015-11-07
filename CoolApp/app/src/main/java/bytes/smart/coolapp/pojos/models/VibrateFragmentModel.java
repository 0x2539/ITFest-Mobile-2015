package bytes.smart.coolapp.pojos.models;

import bytes.smart.coolapp.interfaces.SimpleObservable;
import bytes.smart.coolapp.pojos.Vibration;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public class VibrateFragmentModel extends SimpleObservable<VibrateFragmentModel> {
    private int vibrationTime;

    public int getVibrationTime() {
        return vibrationTime;
    }

    public void setVibrationTime(int vibrationTime) {
        this.vibrationTime = vibrationTime;
    }
}
