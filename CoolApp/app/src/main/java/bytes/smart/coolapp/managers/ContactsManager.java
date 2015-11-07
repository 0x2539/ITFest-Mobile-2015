package bytes.smart.coolapp.managers;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.io.InputStream;
import java.util.ArrayList;

import bytes.smart.coolapp.pojos.Contact;
import bytes.smart.coolapp.utils.StringUtils;

/**
 * Created by alexbuicescu on 30.10.2015.
 */
public class ContactsManager {

    private static ContactsManager instance;

    private Context context;

    private ArrayList<Contact> contacts;

    private ContactsManager(Context context)
    {
        this.context = context;
        this.contacts = new ArrayList<>();
//        retrieveContacts();
//        loadContacts();
    }

    public static ContactsManager getContactsManager(Context context)
    {
        if(instance == null)
        {
            instance = new ContactsManager(context);
        }
        return instance;
    }

    public void loadContacts()
    {
        if(contacts.size() == 0) {
            LoadContactAsyncTask contactAsyncTask = new LoadContactAsyncTask();
            contactAsyncTask.execute();
        }
    }

    private void retrieveContacts()
    {
//        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                Contact contact = new Contact(id, retrieveContactName(id));

                if(contact.getName() != null &&
                        !StringUtils.trimStartEnd(contact.getName()).equals("")) {

                    LoadPhotoAsyncTask loadPhotoAsyncTask = new LoadPhotoAsyncTask(id, contact);
                    loadPhotoAsyncTask.execute();

                    contacts.add(contact);
                }
//                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                if (Integer.parseInt(cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    Cursor pCur = getContext().getContentResolver().query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
//                            new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////                        Toast.makeText(NativeContentProvider.this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
//                    }
//                    pCur.close();
//                }
            }
        }
    }

    private Drawable retrieveContactPhoto(String contactID) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(photo == null)
        {
            return null;
        }

        return new BitmapDrawable(getContext().getResources(), photo);
    }

    private String retrieveContactName(String contactID) {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{contactID}, null);
//                uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        return contactName;
    }

    private Context getContext() {
        return context;
    }

    public ArrayList<Contact> getContacts()
    {
        return contacts;
    }


    private class LoadPhotoAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private String contactID;
        private Contact contact;

        private LoadPhotoAsyncTask(String contactID, Contact contact)
        {
            this.contactID = contactID;
            this.contact = contact;
        }

        @Override
        protected Void doInBackground(Void... params) {
            contact.setPhoto(retrieveContactPhoto(contactID));
            return null;
        }
    }

    private class LoadContactAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private String contactID;
        private Contact contact;

        private LoadContactAsyncTask()
        {
        }

        @Override
        protected Void doInBackground(Void... params) {
            retrieveContacts();
            return null;
        }
    }
}
