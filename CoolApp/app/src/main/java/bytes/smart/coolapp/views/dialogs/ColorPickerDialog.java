package bytes.smart.coolapp.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import bytes.smart.coolapp.R;
import bytes.smart.coolapp.utils.ViewUtils;
import bytes.smart.coolapp.views.adapters.ColorsAdapter;


public class ColorPickerDialog extends Dialog {

	private static boolean isDialogShown = false;

	private boolean dismissDialogOnBackPressed = true;

	public void show()
	{

//		if (!isDialogShown)
		{
			super.show();
			isDialogShown = true;
		}

	}

	public void dismiss()
	{
		super.dismiss();
		isDialogShown = false;
	}

	public static void unsetDialog()
	{
		isDialogShown = false;
	}

	public static boolean getIsDialogShown()
	{
		return isDialogShown;
	}

	@Override
	public void onBackPressed()
	{
        super.onBackPressed();
	}

	public boolean isDismissDialogOnBackPressed()
	{
		return dismissDialogOnBackPressed;
	}

	public void setDismissDialogOnBackPressed(boolean dismissDialogOnBackPressed)
	{
		this.dismissDialogOnBackPressed = dismissDialogOnBackPressed;
	}

	/**
	 * Instantiates a new pause dialog.
	 *
	 * @param context
	 *            the context
	 */
	public ColorPickerDialog(Context context) {
		super(context, R.style.AppThemeDialog);
	}

    /**
     * Instantiates a new pause dialog.
     *
     * @param context
     *            the context
     * @param theme
     *            the theme
     */
    public ColorPickerDialog(Context context, int theme) {
        super(context, theme);
    }

	/**
	 * The Class Builder.
	 */
	public static class Builder {

		/** The context. */
		private final Context context;
		private boolean dismissDialogOnBackPressed = true;

        private Runnable onColorClickedRunnable;

        private GridView colorsGridView;
		private ColorsAdapter colorsAdapter;
		private ArrayList<Integer> colors;

		private int pickedColor;

		/**
		 * Instantiates a new builder.
		 *
		 * @param context
		 *            the context
		 */
		public Builder(Context context) {
			this.context = context;
		}

		public Builder setDismissDialogOnBackPressed(boolean dismissDialogOnBackPressed)
		{
			this.dismissDialogOnBackPressed = dismissDialogOnBackPressed;
			return this;
		}

		/**
		 * Builds and returns the dialog.
		 *
		 * @return the dialog
		 */
		public ColorPickerDialog build()
		{
			final ColorPickerDialog result = new ColorPickerDialog(getContext());
			result.setDismissDialogOnBackPressed(dismissDialogOnBackPressed);
			// request that dialog has no title
			result.requestWindowFeature(Window.FEATURE_NO_TITLE);
			WindowManager.LayoutParams WMLP = result.getWindow().getAttributes();

            WMLP.gravity = Gravity.CENTER;

			// set the position of the dialog
			result.getWindow().setAttributes(WMLP);

			// remove the dialog border drawable
            result.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			// set the layout of the dialog

			result.setContentView(R.layout.dialog_color_picker);

			result.setCanceledOnTouchOutside(true);

            initViews(result);

			return result;
		}

        private void initViews(final ColorPickerDialog dialogLayout)
        {
            colorsGridView = (GridView) dialogLayout.findViewById(R.id.dialog_color_picker_colors_gridview);
            colors = ViewUtils.getCustomColorsArrayList(context);
            colorsAdapter = new ColorsAdapter(context, colors);
            colorsGridView.setAdapter(colorsAdapter);
            colorsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					pickedColor = colors.get(position);
					if (onColorClickedRunnable != null) {
						onColorClickedRunnable.run();
					}
					dialogLayout.dismiss();
				}
			});
        }

		private Context getContext()
		{
			return context;
		}

        public int getPickedColor()
        {
            return pickedColor;
        }

        /**
         * Sets the click on color button.
         *
         * @param onColorClickedRunnable
         *            the runnable that will be ran when the color is clicked. If it is set to null
         *            then the dialog will be dismissed without running any action.
         */
        public Builder setOnColorClicked(Runnable onColorClickedRunnable)
        {
            this.onColorClickedRunnable = onColorClickedRunnable;

            return this;
        }
    }

}
