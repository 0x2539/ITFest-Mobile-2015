package bytes.smart.coolapp.managers;

import java.util.ArrayList;

import bytes.smart.coolapp.pojos.Vibration;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class BlindManager {
    private static BlindManager blindManager;

    private ArrayList<Vibration> vibrations;
    private String contact;

    private BlindManager()
    {
        vibrations = new ArrayList<>();
    }

    public static BlindManager getBlindManager()
    {
        if(blindManager == null)
        {
            blindManager = new BlindManager();
        }

        return blindManager;
    }

    public String getContact() {
        return contact;
    }

    public void addVibration(Vibration vibration)
    {
        vibrations.add(vibration);
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ArrayList<Vibration> getVibrations() {
        return vibrations;
    }

    public void setVibrations(ArrayList<Vibration> vibrations) {
        this.vibrations = vibrations;
    }
}
