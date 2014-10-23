package jp.caliconography.one_liners.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

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
    public static final int OPEN_DURATION_DEFAULT_VALUE = 500;
    public static final int CLOSE_DURATION_DEFAULT_VALUE = 200;
    @InjectView(R.id.popup_menu_base)
    Button mBaseButton;
    @InjectView(R.id.menu1)
    ImageView mMenu1;
    @InjectView(R.id.menu2)
    ImageView mMenu2;
    @InjectView(R.id.menu3)
    ImageView mMenu3;
    @InjectView(R.id.menu4)
    ImageView mMenu4;
    @InjectView(R.id.menu5)
    ImageView mMenu5;

    private float mItemInterval = ITEM_INTERVAL_DEFAULT_VALUE;
    private int mOpenDuration = OPEN_DURATION_DEFAULT_VALUE;
    private int mCloseDuration = CLOSE_DURATION_DEFAULT_VALUE;

    private final ArrayList<View> mViewList = new ArrayList<View>();

    private boolean isOpened = false;

    public PopupMenu(Context context) {
        super(context);
    }

    public PopupMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PopupMenu);
        mItemInterval = attributes.getFloat(R.styleable.PopupMenu_item_interval, ITEM_INTERVAL_DEFAULT_VALUE);
        mOpenDuration = attributes.getInt(R.styleable.PopupMenu_open_duration, OPEN_DURATION_DEFAULT_VALUE);
        mCloseDuration = attributes.getInt(R.styleable.PopupMenu_close_duration, CLOSE_DURATION_DEFAULT_VALUE);

        // LayoutInflaterでレイアウトxmlの内容でViewを作る
        // LayoutInflater#inflate()の第2引数ではルートとなるViewとして自分自身を指定する
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_menu, this);

        ButterKnife.inject(this, layout);

        mViewList.add(mMenu1);
        mViewList.add(mMenu2);
        mViewList.add(mMenu3);
        mViewList.add(mMenu4);
        mViewList.add(mMenu5);
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
        isOpened = !isOpened;
    }

    public void open() {
        for (int i = 0; i < mViewList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mViewList.get(i), TRANSLATION_Y, 0f, -mItemInterval * i);
            objectAnimator.setDuration(mOpenDuration);
            objectAnimator.setInterpolator(new AnticipateOvershootInterpolator(2));
            objectAnimator.start();
        }
    }

    public void close() {
        for (int i = 0; i < mViewList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mViewList.get(i), TRANSLATION_Y, -mItemInterval * i, 0f);
            objectAnimator.setDuration(mCloseDuration);
            objectAnimator.start();
        }
    }
}
