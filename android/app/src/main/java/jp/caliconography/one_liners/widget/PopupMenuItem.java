package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * Created by abe on 2014/10/23.
 */
public class PopupMenuItem extends ImageButton {

    public PopupMenuItem(Context context) {
        super(context);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT));
    }

    public PopupMenuItem(Context context, int imageResourceId) {
        this(context);
        setBackgroundResource(imageResourceId);
    }
}