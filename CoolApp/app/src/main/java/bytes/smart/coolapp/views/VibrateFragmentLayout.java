package bytes.smart.coolapp.views;

import android.content.Context;
import android.util.AttributeSet;

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

    public interface ViewListener {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
    }

    private void initLayout()
    {
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
