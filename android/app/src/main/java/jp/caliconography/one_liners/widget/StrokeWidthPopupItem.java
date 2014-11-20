package jp.caliconography.one_liners.widget;

import android.content.Context;

import jp.caliconography.one_liners.model.PaintConfig;

/**
 * Created by abe on 2014/10/24.
 */
public class StrokeWidthPopupItem extends PopupMenuItem<PaintConfig.StrokeWidth> {
    public StrokeWidthPopupItem(Context context, int id, PaintConfig.StrokeWidth width, int imageResourceId) {
        super(context, id, width, imageResourceId);
    }
}
