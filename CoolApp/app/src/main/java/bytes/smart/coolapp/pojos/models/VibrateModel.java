package bytes.smart.coolapp.pojos.models;

import java.io.Serializable;
import java.util.ArrayList;

import bytes.smart.coolapp.interfaces.SimpleObservable;
import bytes.smart.coolapp.pojos.Vibration;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class VibrateModel extends SimpleObservable<VibrateModel> implements Serializable {

    private ArrayList<Vibration> vibrations;


    public ArrayList<Vibration> getVibrations() {
        return vibrations;
    }

    public void setVibrations(ArrayList<Vibration> vibrations, boolean... isNotificationRequired) {
        this.vibrations = vibrations;
        if (isNotificationRequired.length > 0 && isNotificationRequired[0]) notifyObservers();
    }

    public void update()
    {
        notifyObservers();
    }
}
