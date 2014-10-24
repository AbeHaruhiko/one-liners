package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by abe on 2014/10/23.
 */
public class PopupMenuItem extends ImageView {

    public PopupMenuItem(Context context) {
        super(context);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT));
    }

    public PopupMenuItem(Context context, int imageResourceId) {
        this(context);
        setImageResource(imageResourceId);
    }

    public PopupMenuItem(Context context, Drawable image) {
        this(context);
        setImageDrawable(image);
    }

    public PopupMenuItem(Context context, Bitmap bitmap) {
        this(context);
        setImageBitmap(bitmap);
    }
}