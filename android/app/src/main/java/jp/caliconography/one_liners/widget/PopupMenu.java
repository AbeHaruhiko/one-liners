package jp.caliconography.one_liners.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.caliconography.one_liners.R;

/**
 * Created by abeharuhiko on 2014/10/23.
 */
public class PopupMenu extends FrameLayout {

    public static final String TRANSLATION_Y = "translationY";
    public static final int ITEM_INTERVAL_DEFAULT_VALUE = 100;
    public static final int OPEN_DURATION_DEFAULT_VALUE = 300;
    public static final int CLOSE_DURATION_DEFAULT_VALUE = 200;
    @InjectView(R.id.popup_menu_base)
    ImageButton mBaseButton;

    private float mItemInterval = ITEM_INTERVAL_DEFAULT_VALUE;
    private int mOpenDuration = OPEN_DURATION_DEFAULT_VALUE;
    private int mCloseDuration = CLOSE_DURATION_DEFAULT_VALUE;

    private List<PopupMenuItem> mItemList = new ArrayList<PopupMenuItem>();

    public boolean isOpened = false;
    private View mLayout;

    public PopupMenu(Context context) {
        super(context);
        init(context, null);
    }

    public PopupMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PopupMenu);
            mItemInterval = attributes.getFloat(R.styleable.PopupMenu_item_interval, ITEM_INTERVAL_DEFAULT_VALUE);
            mOpenDuration = attributes.getInt(R.styleable.PopupMenu_open_duration, OPEN_DURATION_DEFAULT_VALUE);
            mCloseDuration = attributes.getInt(R.styleable.PopupMenu_close_duration, CLOSE_DURATION_DEFAULT_VALUE);
        }

        mLayout = LayoutInflater.from(context).inflate(R.layout.popup_menu, this);

        ButterKnife.inject(this, mLayout);
    }

    public PopupMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @OnClick(R.id.popup_menu_base)
    void onClickBase(View view) {
        if (!isOpened) {
            open();
        } else {
            close();
        }
    }

    public void addItems(List<PopupMenuItem> items) {
        mItemList.addAll(items);

        FrameLayout frameLayout = (FrameLayout) this.findViewById(R.id.popup_menu_root);
        for (PopupMenuItem item : mItemList) {
            frameLayout.addView(item);
        }
        mBaseButton.bringToFront();
    }

    public void open() {
        for (int i = 0; i < mItemList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mItemList.get(i), TRANSLATION_Y, 0f, -mItemInterval * (i + 1));
            objectAnimator.setDuration(mOpenDuration);
            objectAnimator.setInterpolator(new OvershootInterpolator(2));
            objectAnimator.start();
        }
        this.isOpened = true;
    }

    public void close() {
        for (int i = 0; i < mItemList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mItemList.get(i), TRANSLATION_Y, -mItemInterval * (i + 1), 0f);
            objectAnimator.setDuration(mCloseDuration);
            objectAnimator.start();
        }
        this.isOpened = false;
    }
}
