package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import jp.caliconography.one_liners.R;
import jp.caliconography.one_liners.model.PaintConfig;

/**
 * Created by abe on 2014/10/24.
 */
public class StrokeColorPopupItem extends PopupMenuItem<PaintConfig.StrokeColor> {
    public StrokeColorPopupItem(Context context, int id, PaintConfig.StrokeColor color, int imageResourceId) {
        super(context, id, color, imageResourceId);


        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(1, R.color.btn_common_stroke);
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color.getColorInt());
        // -2が一番ピッタリ（4.1.1）。backgroundのstroke分なのか？？
        drawable.setSize(getResources().getDimensionPixelSize(R.dimen.popup_menu_item_width) - 2,
                getResources().getDimensionPixelSize(R.dimen.popup_menu_item_height) - 2);

        this.setImageDrawable(drawable);
    }
}
