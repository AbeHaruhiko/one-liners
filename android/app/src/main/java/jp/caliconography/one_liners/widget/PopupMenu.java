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

    private final ArrayList<View> mViewList = new ArrayList<View>();

    private boolean isOpened = false;

    public PopupMenu(Context context) {
        super(context);
    }

    public PopupMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

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

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PopupMenu);
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
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mViewList.get(i), TRANSLATION_Y, 0f, -100f * i);
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new AnticipateOvershootInterpolator(2));
            objectAnimator.start();
        }
    }

    public void close() {
        for (int i = 0; i < mViewList.size(); i++) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mViewList.get(i), TRANSLATION_Y, -100f * i, 0f);
            objectAnimator.setDuration(200);
            objectAnimator.start();
        }
    }
}
