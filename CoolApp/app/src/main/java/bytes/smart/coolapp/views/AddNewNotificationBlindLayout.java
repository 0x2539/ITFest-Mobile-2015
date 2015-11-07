package bytes.smart.coolapp.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.pojos.models.MVCModel;
import bytes.smart.coolapp.pojos.models.MainModel;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class AddNewNotificationBlindLayout extends RelativeLayout implements OnChangeListener<MVCModel> {

    private final String TAG = "MainLayout";

    private MVCModel model;
    private ViewListener viewListener;

    private ViewPager viewPager;
    private TabsPagerAdapter viewPagerAdapter;

    public interface ViewListener {

    }

    public AddNewNotificationBlindLayout(Context context) {
        super(context);
    }

    public AddNewNotificationBlindLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddNewNotificationBlindLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initLayout();
        initToolbar();
    }

    private void initLayout() {
        viewPager = (ViewPager) findViewById(R.id.activity_add_new_notification_blind_viewpager);
        viewPagerAdapter = new TabsPagerAdapter(((AppCompatActivity)getContext()).getSupportFragmentManager(), getContext());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("main", "page: " + position);
//                if(getViewListener() != null)
                {
                    ((ContactsFragment)viewPagerAdapter.getItem(0)).save();
                    ((VibrateFragment)viewPagerAdapter.getItem(1)).save();
//                    getViewListener().onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initToolbar() {

    }

    private void updateView() {

    }

    @Override
    public void onChange() {
        updateView();
    }

    public MVCModel getModel() {
        return model;
    }

    public void setModel(MVCModel model) {
        this.model = model;
        this.model.addListener(this);
        updateView();
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

}
