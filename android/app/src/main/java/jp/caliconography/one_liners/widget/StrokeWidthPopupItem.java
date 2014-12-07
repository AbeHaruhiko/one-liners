package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.model.PaintConfig;

/**
 * Created by abe on 2014/10/24.
 */
public class StrokeWidthPopupItem extends PopupMenuItem<PaintConfig.StrokeWidth> {
    public StrokeWidthPopupItem(Context context, int id, PaintConfig.StrokeWidth strokeWidth, int imageResourceId) {
        super(context, id, strokeWidth, imageResourceId);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(strokeWidth.getWidthInt() / 2);
        drawable.setColor(Color.GRAY);
        drawable.setSize(getResources().getDimensionPixelSize(R.dimen.popup_menu_item_width) * 3 / 4,
                strokeWidth.getWidthInt());

        this.setImageDrawable(drawable);
    }
}
