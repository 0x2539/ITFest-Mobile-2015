package bytes.smart.coolapp.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public abstract class TabbedRootFragment<T extends TabbedFragRootLayout> extends Fragment implements TabbedFragment {

    protected abstract T getLayout();
    protected boolean isInView;
    protected boolean updateModelOnShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}
