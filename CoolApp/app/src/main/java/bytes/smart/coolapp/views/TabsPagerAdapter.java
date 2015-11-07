package bytes.smart.coolapp.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.utils.Constants;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
//    private static final int NUMBER_OF_TABS = 2;

    private FragmentManager fm;

    private String tabTitles[] = new String[] { "contact", "vibrations"};
    private Context context;

    private ContactsFragment contactsFragment;
    private VibrateFragment vibrateFragment;

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (contactsFragment == null)
                {
                    Fragment page = fm.findFragmentByTag("android:switcher:" + R.id.activity_add_new_notification_blind_viewpager+ ":" + 0);
                    if ((page != null) && (page instanceof ContactsFragment))
                    {
                        contactsFragment = (ContactsFragment) page;
                    }
                    else
                    {
                        contactsFragment = new ContactsFragment();
                    }
                }

                return contactsFragment;
            case 1:
                if (vibrateFragment == null)
                {
                    Fragment page = fm.findFragmentByTag("android:switcher:" + R.id.activity_add_new_notification_blind_viewpager+ ":" + 1);
                    if ((page != null) && (page instanceof VibrateFragment))
                    {
                        vibrateFragment = (VibrateFragment) page;
                    }
                    else
                    {
                        vibrateFragment = new VibrateFragment();
                    }
                }
                return vibrateFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
