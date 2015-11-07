package bytes.smart.coolapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.MainModel;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MainLayout extends RelativeLayout implements OnChangeListener<MainModel> {

    private final String TAG = "MainLayout";

    private MainModel model;
    private ViewListener viewListener;

    public interface ViewListener {

    }

    public MainLayout(Context context) {
        super(context);
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initLayout();
        initToolbar();
    }

    private void initLayout() {

    }

    private void initToolbar() {

    }

    private void updateView() {

    }

    @Override
    public void onChange() {

    }

    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

}
