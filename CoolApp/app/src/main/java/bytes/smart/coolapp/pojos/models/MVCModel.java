package bytes.smart.coolapp.pojos.models;

import bytes.smart.coolapp.interfaces.SimpleObservable;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MVCModel extends SimpleObservable<MVCModel> {

    public void update(boolean... update) {
        if ((update.length > 0 && update[0]) || update.length == 0) {
            notifyObservers();
        }
    }
}
