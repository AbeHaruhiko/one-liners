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


//        setImageResource(R.drawable.btn_oval_common_front);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(1, R.color.btn_common_stroke);
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color.getColorInt());
        drawable.setSize(getResources().getDimensionPixelSize(R.dimen.popup_menu_item_width),
                getResources().getDimensionPixelSize(R.dimen.popup_menu_item_height));

//        this.setBackground(drawable);
        this.setImageDrawable(drawable);
//        this.setScaleType(ScaleType.CENTER);
    }
}
