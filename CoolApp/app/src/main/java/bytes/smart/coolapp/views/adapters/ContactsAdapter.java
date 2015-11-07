package bytes.smart.coolapp.views.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.managers.ContactsManager;
import bytes.smart.coolapp.pojos.Contact;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alexandru on 22-May-15.
 */
public class ContactsAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater layoutInflater;

    private List<Contact> currentItems;

    private Context context;

    public ContactsAdapter(Context context, List<Contact> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentItems = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return currentItems.size();
    }

    @Override
    public Object getItem(int i) {
        return currentItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.row_contact, viewGroup, false);

            holder = new ViewHolder();
            holder.contactNameTextView = (TextView) view.findViewById(R.id.row_contact_name_textview);
            holder.contactLetterNameTextView = (TextView) view.findViewById(R.id.row_contact_name_letter_textview);
            holder.contactPhotoImageView = (CircleImageView) view.findViewById(R.id.row_contact_avatar_imageview);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.contactNameTextView.setText(currentItems.get(i).getName());

        if(currentItems.get(i).getPhoto() != null)
        {
            holder.contactPhotoImageView.setImageDrawable(currentItems.get(i).getPhoto());
            holder.contactLetterNameTextView.setVisibility(View.GONE);
        }
        else{
            holder.contactPhotoImageView.setImageDrawable(new ColorDrawable(context.getResources().getColor(R.color.gray)));
//            holder.contactPhotoImageView.setColorFilter(context.getResources().getColor(R.color.gray));
            holder.contactLetterNameTextView.setText(currentItems.get(i).getName().charAt(0) + "");
            holder.contactLetterNameTextView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void setCurrentItems(List<Contact> items) {
        this.currentItems = items;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                if (results.count == 0)
                    notifyDataSetInvalidated();
                else {
                    currentItems = (List<Contact>) results.values;
                    notifyDataSetChanged();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = ContactsManager.getContactsManager(context).getContacts();
                    results.count = ContactsManager.getContactsManager(context).getContacts().size();
                }
                else {
                    // We perform filtering operation
                    List<Contact> nPlanetList = new ArrayList<>();

                    for (Contact p : ContactsManager.getContactsManager(context).getContacts()) {
                        if (p.getName().toUpperCase().contains(constraint.toString().toUpperCase()))
                            nPlanetList.add(p);
                    }

                    results.values = nPlanetList;
                    results.count = nPlanetList.size();

                }
                return results;
            }
        };
    }

    private static class ViewHolder {
        TextView contactNameTextView;
        TextView contactLetterNameTextView;
        CircleImageView contactPhotoImageView;
    }
}

