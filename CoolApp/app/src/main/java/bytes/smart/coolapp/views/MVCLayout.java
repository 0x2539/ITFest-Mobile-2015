package bytes.smart.coolapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.MVCModel;
import bytes.smart.coolapp.pojos.models.MainModel;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class MVCLayout extends RelativeLayout implements OnChangeListener<MVCModel> {

    private final String TAG = "MainLayout";

    private MVCModel model;
    private ViewListener viewListener;

    public interface ViewListener {

    }

    public MVCLayout(Context context) {
        super(context);
    }

    public MVCLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MVCLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public MVCModel getModel() {
        return model;
    }

    public void setModel(MVCModel model) {
        this.model = model;
        this.model.addListener(this);
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

}
