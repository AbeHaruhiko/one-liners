package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by abe on 2014/10/23.
 */
public class PopupMenuItem extends ImageButton {
    public PopupMenuItem(Context context) {
        super(context);

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        setLayoutParams(params);
    }

    public PopupMenuItem(Context context, int imageResourceId) {
        super(context);
        setImageResource(imageResourceId);
    }

    public PopupMenuItem(Context context, Drawable image) {
        super(context);
        setImageDrawable(image);
    }

    public PopupMenuItem(Context context, Bitmap bitmap) {
        super(context);
        setImageBitmap(bitmap);
    }
}