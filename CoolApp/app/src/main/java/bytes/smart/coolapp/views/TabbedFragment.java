package bytes.smart.coolapp.views;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public interface TabbedFragment {

    boolean consumeOnBackPressed();
    void manageOrientationChange(boolean isInPortrait);
    void onWindowFocusChanged(boolean hasFocus);
    void save();
    void cancel();
}
