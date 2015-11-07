package bytes.smart.coolapp.pojos;

import android.graphics.drawable.Drawable;

/**
 * Created by alexbuicescu on 30.10.2015.
 */
public class Contact {

    private String contactID;
    private String name;
    private Drawable photo;

    public Contact()
    {

    }

    public Contact(String contactID, String name)
    {
        this.contactID = contactID;
        this.name = name;
    }

    public Contact(String contactID, String name, Drawable photo)
    {
        this(contactID, name);
        this.photo = photo;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
