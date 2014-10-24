package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import jp.caliconography.one_liners.event.PopupMenuItemClickedEvent;
import jp.caliconography.one_liners.util.BusHolder;

/**
 * Created by abe on 2014/10/23.
 */
public class PopupMenuItem extends ImageButton {

    int mId;

    public PopupMenuItem(Context context) {
        super(context);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT));
    }

    public PopupMenuItem(Context context, int id, int imageResourceId) {
        this(context);
        mId = id;
        setBackgroundResource(imageResourceId);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                BusHolder.get().post(new PopupMenuItemClickedEvent(mId));
            }
        });
    }

}