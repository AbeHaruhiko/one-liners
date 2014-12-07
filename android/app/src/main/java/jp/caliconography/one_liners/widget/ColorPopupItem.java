package jp.caliconography.one_liners.widget;

import android.content.Context;

import jp.caliconography.one_liners.model.PaintConfig;

/**
 * Created by abe on 2014/10/24.
 */
public class ColorPopupItem extends PopupMenuItem<PaintConfig.StrokeColor> {
    public ColorPopupItem(Context context, int id, PaintConfig.StrokeColor color, int imageResourceId) {
        super(context, id, color, imageResourceId);
    }
}
