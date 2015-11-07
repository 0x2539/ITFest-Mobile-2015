package bytes.smart.coolapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.database.DatabaseHelper;
import bytes.smart.coolapp.pojos.NotificationRule;

/**
 * Created by alexbuicescu on 07.11.2015.
 */
public class NotificationsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<NotificationRule> currentItems;

    private Context context;
    private DatabaseHelper databaseHelper;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public NotificationsAdapter(Context context, List<NotificationRule> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentItems = items;
        this.context = context;

        this.onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
            }
        };

        this.databaseHelper = new DatabaseHelper(context);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.row_custom_notification, viewGroup, false);

            holder = new ViewHolder();
            holder.notificationSenderTextView = (TextView) view.findViewById(R.id.row_custom_notification_title_textview);
            holder.notificationMessageTextView = (TextView) view.findViewById(R.id.row_custom_notification_message_textview);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.notificationSenderTextView.setText(currentItems.get(i).getDefaultSender());
        holder.notificationMessageTextView.setText(currentItems.get(i).getNotificationMessage());


        return view;
    }

    public void setCurrentItems(List<NotificationRule> items) {
        this.currentItems = items;
    }

    private static class ViewHolder {
        TextView notificationSenderTextView;
        TextView notificationMessageTextView;
    }
}

