package bytes.smart.coolapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.database.DatabaseHelper;
import bytes.smart.coolapp.managers.BlindManager;
import bytes.smart.coolapp.managers.ContactsManager;
import bytes.smart.coolapp.managers.NotificationRulesManager;
import bytes.smart.coolapp.pojos.NotificationRule;
import bytes.smart.coolapp.pojos.NotificationRuleApp;
import bytes.smart.coolapp.pojos.models.ContactsFragmentModel;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public class ContactsFragment extends TabbedRootFragment<ContactsFragmentLayout> implements TabbedFragment {

    private final String TAG = "ContactsFragment";

    private ContactsFragmentLayout layout;
    private ContactsFragmentModel model;
    private ContactsFragmentLayout.ViewListener viewListener = new ContactsFragmentLayout.ViewListener() {
        @Override
        public void onSaveClicked() {
            NotificationRule notificationRule = new NotificationRule("Hello",
                    BlindManager.getBlindManager().getContact(), 0, 0, 0,
                    BlindManager.getBlindManager().getVibrations(),
                    new ArrayList<NotificationRuleApp>());
            new DatabaseHelper(getContext()).insertNotification(notificationRule);
            NotificationRulesManager.getNotificationRulesManager().getNotificationRules().clear();
            NotificationRulesManager.getNotificationRulesManager().setNotificationRules(new DatabaseHelper(getContext()).getAllNotificationRules());

            getActivity().finish();
        }

        @Override
        public void onCancelClicked() {
            getActivity().finish();
        }

        @Override
        public void onContactClicked(int position) {
            BlindManager.getBlindManager().setContact(ContactsManager.getContactsManager(activity).getContacts().get(position).getName());
            Log.e(TAG, "selected: " + BlindManager.getBlindManager().getContact());
        }
    };

    private Activity activity;

    public ContactsFragment() {
//        initModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        activity = getActivity();
        initModel();

    }

    @Override
    public void onResume()
    {
        setHasOptionsMenu(false);
        super.onResume();
    }

    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected ContactsFragmentLayout getLayout() {
        return layout;
    }

    @Override
    public boolean consumeOnBackPressed() {
        return false;
    }

    @Override
    public void manageOrientationChange(boolean isInPortrait) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    public void save() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Log.v(TAG, "28.10.2014    Personal Frag onCreate started");
        layout = (ContactsFragmentLayout) inflater.inflate(R.layout.fragment_contacts_blind, container, false);
        layout.setViewListener(viewListener);
        layout.setModel(model);

        Log.v(TAG, "28.10.2014    Personal Frag onCreate finished");

        return layout;
    }

    private void initModel()
    {
        model = new ContactsFragmentModel();
    }

}
