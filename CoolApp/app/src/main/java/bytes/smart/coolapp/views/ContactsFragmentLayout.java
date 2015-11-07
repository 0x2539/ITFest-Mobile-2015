package bytes.smart.coolapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.interfaces.OnChangeListener;
import bytes.smart.coolapp.managers.ContactsManager;
import bytes.smart.coolapp.pojos.models.ContactsFragmentModel;
import bytes.smart.coolapp.views.adapters.ContactsAdapter;

/**
 * Created by alexbuicescu on 19.08.2015.
 */
public class ContactsFragmentLayout extends TabbedFragRootLayout implements OnChangeListener<ContactsFragmentModel> {

    private final String TAG = "ContactsFragmentLayout";

    private ContactsFragmentModel model;
    private ViewListener viewListener;

    private Button saveButton;
    private Button cancelButton;

    private ListView contactsListView;
    private ContactsAdapter contactsAdapter;

    public interface ViewListener {
        void onSaveClicked();
        void onCancelClicked();
        void onContactClicked(int position);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
    }

    private void initLayout()
    {
        saveButton = (Button) findViewById(R.id.fragment_contacts_blind_save_button);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onSaveClicked();
            }
        });

        cancelButton = (Button) findViewById(R.id.fragment_contacts_blind_cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onCancelClicked();
            }
        });

        contactsListView = (ListView) findViewById(R.id.fragment_contacts_blind_listview);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getViewListener().onContactClicked(position);
            }
        });
    }

    private void updateView()
    {
        if(contactsAdapter == null)
        {
            contactsAdapter = new ContactsAdapter(getContext(), ContactsManager.getContactsManager(getContext()).getContacts());
            contactsListView.setAdapter(contactsAdapter);
        }
        else
        {
            contactsAdapter.setCurrentItems(ContactsManager.getContactsManager(getContext()).getContacts());
            contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChange() {
        updateView();
    }

    public ContactsFragmentLayout(Context context) {
        super(context);
    }

    public ContactsFragmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsFragmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public ContactsFragmentModel getModel() {
        return model;
    }

    public void setModel(ContactsFragmentModel model) {
        this.model = model;
        this.model.addListener(this);
        updateView();
    }


}
