package jp.caliconography.one_liners.widget;

import android.content.Context;

import jp.caliconography.one_liners.model.PaintConfig;

/**
 * Created by abe on 2014/12/7.
 */
public class StorokeColorPopupItem extends PopupMenuItem<PaintConfig.StrokeColor> {
    public StorokeColorPopupItem(Context context, int id, PaintConfig.StrokeColor color, int imageResourceId) {
        super(context, id, color, imageResourceId);
    }
}
