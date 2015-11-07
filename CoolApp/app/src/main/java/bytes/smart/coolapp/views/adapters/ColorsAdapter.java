package bytes.smart.coolapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.utils.ViewUtils;

/**
 * Created by Alexandru on 22-May-15.
 */
public class ColorsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<Integer> currentItems;

    private Context context;

    public ColorsAdapter(Context context, List<Integer> items) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.row_color, viewGroup, false);

            holder = new ViewHolder();
            holder.colorButton = (ImageView) view.findViewById(R.id.row_color_color_button);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.colorButton.setBackground(ViewUtils.getColoredCircleDrawableByColor(
                        context,
                        currentItems.get(i))
        );

//        holder.colorButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("colors", event.getAction() + "");
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    float factor = 0.6f;
//                    int red = (int) ((Color.red(currentItems.get(i)) * factor / 255 + 1 - factor) * 255);
//                    int green = (int) ((Color.green(currentItems.get(i)) * factor / 255 + 1 - factor) * 255);
//                    int blue = (int) ((Color.blue(currentItems.get(i)) * factor / 255 + 1 - factor) * 255);
////                                    colorImageButton.setBackground(new ColorDrawable(Color.argb(255, red, green, blue)));
//
//                    holder.colorButton.setBackground(ViewUtils.getColoredCircleDrawableByColor(
//                                    context,
//                                    Color.argb(255, red, green, blue))
//                    );
//                } else if (event.getAction() == MotionEvent.ACTION_UP ||
//                        event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    holder.colorButton.setBackground(ViewUtils.getColoredCircleDrawableByColor(
//                                    context,
//                                    currentItems.get(i))
//                    );
//                }
//                return false;
//            }
//        });

        return view;
    }

    public void setCurrentItems(List<Integer> items) {
        this.currentItems = items;
    }

    private static class ViewHolder {
        ImageView colorButton;
    }
}

